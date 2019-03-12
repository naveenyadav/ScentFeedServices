package com.scent.feedservice.steps;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

import com.alcs.annotation.EventMetaData;
import com.alcs.event.handler.AbstractEventHandler;
import com.alcs.util.Constants;
import com.alcs.util.LoggerUtil;

import reactor.bus.EventBus;
import reactor.bus.selector.Selectors;

/**
 * This class file register the event and event hanlder in Reactor bus. It
 * processes all the event handler packages automatically and register events to
 * the eventbus based on metatdata annotaion. The following methods are
 * available: register(), registerEvents().
 * 
 * @author nyad11
 * 
 */

@Component
public class EventRegistry {
    private static final Logger LOG = LogManager.getLogger(EventRegistry.class);
    private Set<String> hash; 
    @Autowired
    private EventBus eventBus;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * This is init method for Event manager
     * 
     */
    @PostConstruct
    public void register() {
    	hash = new HashSet<String>();
        LoggerUtil.error(LOG, "Registering events in reactor...");
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(new AssignableTypeFilter(Object.class));
        Set<BeanDefinition> components = provider.findCandidateComponents(Constants.EVENT_HANDLER_PACKAGE_NAME);
        for (BeanDefinition component : components) {
            try {
            	
            	hash.add(component.getBeanClassName() );
                Class<?> cls = Class.forName(component.getBeanClassName());
                if (cls.isAnnotationPresent(EventMetaData.class)) {
                    EventMetaData annotation = cls.getAnnotation(EventMetaData.class);
                    registerEvents(annotation, cls);
                }
            } catch (ClassNotFoundException e) {
                LoggerUtil.error(LOG, "Error in loading eventBus...", e);

            }
        }
        LoggerUtil.info(LOG, "Registered events successful.");
    }

    /**
     * This method is register based on the event metadata annotation
     * 
     * @param annotation
     *            eventMetaData annotation
     * @param cls
     *            instance of the class
     */
    private void registerEvents(EventMetaData annotation, Class<?> cls) {
        if (annotation.isEnabled()) {
            if (annotation.isConcatType()) {
                eventBus.on(Selectors.R(annotation.eventName().concat(Constants.REGEX)),
                        (AbstractEventHandler) applicationContext.getBean(cls));
            } else {
                eventBus.on(Selectors.$(annotation.eventName()),
                        (AbstractEventHandler) applicationContext.getBean(cls));
            }
        }

    }
    public Set<String> getHash() {
    	return hash;
    }

}
