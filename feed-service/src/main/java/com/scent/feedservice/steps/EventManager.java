package com.scent.feedservice.steps;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.alcs.data.RequestData;
import com.alcs.data.ResponseData;
import com.alcs.data.TransactionData;
import com.alcs.services.GlobalConfigService;
import com.alcs.util.Constants;
import com.alcs.util.LoggerUtil;
import com.alcs.util.XMLPathUtil;

import reactor.bus.Event;
import reactor.bus.EventBus;

/**
 * This class file is used for notifying the requested events for execution. The
 * following methods are available: handleEvents(), executeEvents() and
 * getValidEvents().
 * 
 * @author ssared
 * 
 */
@Component
public class EventManager {

    private static final Logger LOG = LogManager.getLogger(EventManager.class);

    private Map<String, Map<String, String>> eventsConfigMap;

    @Autowired
    private GlobalConfigService globalConfigService;

    @Autowired
    private EventBus eventBus;

    /**
     * Init method to initialize event config map. This method reads the file
     * "alcs_controls.xml" and using XPATH creates the eventsConfigMap, a map of
     * event name and value as [map of pre-process, process and post-process
     * events]
     */
    @PostConstruct
    public void init() {
    	System.out.println("Hello");
        eventsConfigMap = new HashMap<>();
        NodeList nodeList = XMLPathUtil.executeXPathQuery(Constants.XML_FILE_ALCS_CONTROLS,
                Constants.XPATH_QUERY_EVENTS_CONFIG);

        if (nodeList != null && nodeList.getLength() > Constants.ZERO) {
            Map<String, String> eventsMap;

            for (int i = Constants.ZERO; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    String eventName = element.getAttribute(Constants.XML_ALCS_EVENT_NAME);
                    // remove any trailing space from event names
                    eventName = StringUtils.trimToEmpty(eventName);

                    // create map to store the pre-process, process and
                    // post-process events
                    eventsMap = new HashMap<>();

                    eventsMap.put(Constants.PRE_PROCESS_EVENTS,
                            XMLPathUtil.getXmlChildElementText(element, Constants.PRE_PROCESS_EVENTS));
                    eventsMap.put(Constants.PROCESS_EVENTS,
                            XMLPathUtil.getXmlChildElementText(element, Constants.PROCESS_EVENTS));
                    eventsMap.put(Constants.POST_PROCESS_EVENTS,
                            XMLPathUtil.getXmlChildElementText(element, Constants.POST_PROCESS_EVENTS));
                    eventsMap.put(Constants.INCLUDE_RESPONSE_EVENTS,
                            XMLPathUtil.getXmlChildElementText(element, Constants.INCLUDE_RESPONSE_EVENTS));
                    // save the eventsMap into eventsConfigMap
                    eventsConfigMap.put(eventName, eventsMap);
                }
            }
        }
    }

    /**
     * This method is used to execute an events in the following order:
     * PreProcessEvents, ProcessEvents, PostProcessEvents.
     * 
     * @param transactionData
     *            the wrapper object of {@link RequestData} and
     *            {@link ResponseData}
     */
    public void handleEvents(TransactionData transactionData) {
        RequestData requestData = transactionData.getRequestData();
        // Execute PreProcess events.
        executeEvents(requestData.getEvents(Constants.PRE_PROCESS_EVENTS), transactionData);
        // Execute Process events.
        executeEvents(requestData.getEvents(Constants.PROCESS_EVENTS), transactionData);
        // Execute PostProcess events.
        executeEvents(requestData.getEvents(Constants.POST_PROCESS_EVENTS), transactionData);
    }

    /**
     * This method is used to notify given events for execution. Each event is
     * handled by its registered handler.
     * 
     * @param events
     *            the events given for execution.
     * @param transactionData
     *            the wrapper object of {@link RequestData} and
     *            {@link ResponseData}
     */
    public void executeEvents(Set<String> events, TransactionData transactionData) {
        Set<String> validEvents;

        if (null == events) {
            validEvents = null;
        } else {
            validEvents = new HashSet<>(events);
        }

        try {
            validEvents = validateEvents(validEvents);
            // If events not empty and events dispatcher is alive.
            if (null != validEvents && !validEvents.isEmpty() && eventBus.getDispatcher().alive()) {

                final CountDownLatch latch = new CountDownLatch(validEvents.size());
                transactionData.setLatch(latch);
                String execEventTimeout = globalConfigService.getPropertyValueAsString(Constants.GLOBAL_CONFIG,
                        Constants.PROP_EXEC_EVENTS_TIMEOUT);
                // Iterate the events
                for (String event : validEvents) {
                    eventBus.notify(event, Event.wrap(transactionData));
                    String eventTimeout = globalConfigService.getPropertyValueAsString(Constants.EVENT_CONFIG,
                            event + Constants.DOT + Constants.PROP_EXEC_EVENTS_TIMEOUT);
                    execEventTimeout = updateEventTimeout(execEventTimeout, eventTimeout);
                }

                // Block until all submitted events have completed or till
                // timeout.

                latch.await(Long.valueOf(execEventTimeout), TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            LoggerUtil.error(LOG, "Exception in executeEvents() method", e);
            Thread.currentThread().interrupt();
        }

    }

    /**
     * This method will update the global events timeout value with event
     * specific timeout value if it is less.
     * 
     * @param execEventTimeout
     *            the global event timeout
     * @param eventTimeout
     *            the event specific timeout
     * @return updated timeout value
     */
    private String updateEventTimeout(String execEventTimeout, String eventTimeout) {
        String timeout = execEventTimeout;
        if (StringUtils.isNotBlank(eventTimeout) && Long.valueOf(timeout) < Long.valueOf(eventTimeout)) {
            timeout = eventTimeout;
        }
        return timeout;
    }

    /**
     * This method will lookup for the given events in registry, If not found,
     * event will be removed from the list.
     * 
     * @param events
     *            the given events.
     * @return validEvents the valid events.
     */
    private Set<String> validateEvents(Set<String> events) {
        Set<String> validEvents;

        if (null == events) {
            validEvents = null;
        } else {
            validEvents = new HashSet<>(events);
        }

        if (null != events && !events.isEmpty()) {
            Iterator<String> iterator = validEvents.iterator();

            while (iterator.hasNext()) {
                if (eventBus.getConsumerRegistry().select(iterator.next()).isEmpty()) {
                    iterator.remove();
                }
            }
        }

        return validEvents;
    }

    /**
     * returns the Map of all events name and values as another map of
     * processEvent type and event name as configured in the alcs_controls.xml
     * 
     * @return the map
     */
    public Map<String, Map<String, String>> getEventsConfigMap() {
        return eventsConfigMap;
    }

}
