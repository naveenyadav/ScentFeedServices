package com.scent.feedservice.repositories;


import com.scent.feedservice.Util.ConfigServiceImpl;
import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.ResponseData;
import com.scent.feedservice.data.feed.IData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.UriTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.scent.feedservice.Util.Constants.GLOBAL_CONFIG;

public class AbstractRepository {

    private List<String> requiredFieldList;
    @Autowired
    protected ConfigServiceImpl configServiceImpl;
    /**
     * getter method for globalConfigService
     *
     * @return GlobalConfigService instance
     */
    public ConfigServiceImpl getConfigServiceImpl() {
        return configServiceImpl;
    }
    public List<String> getRequiredFieldList() {
        return requiredFieldList;
    }

    public void addToRequiredFieldList(String requiredFieldName) {
        if (null == requiredFieldList) {
            this.requiredFieldList = new ArrayList<>();
        }
        this.requiredFieldList.add(requiredFieldName);
    }

    public void addToRequiredFieldList(String[] requiredFieldName) {
        if (null == requiredFieldList) {
            this.requiredFieldList = new ArrayList<>();
        }
        for(String field: requiredFieldName) {
            this.requiredFieldList.add(field);
        }
    }
    protected Map<String, String> getRequestParamsCopy(Map<String, String> dataMap) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.putAll(dataMap);
        return paramsMap;
    }

    /**
     * This method will return the expanded url after replacing place holders
     * with values from the given {@link RequestData} object.
     *
     * @param paramsMap
     *            Map of all request parameter name and its value
     * @param urlPropertyName
     *            key-name of property
     * @param brandName
     *            the brand name
     * @return String URL the url from properties file against the given
     *         urlPropertyName
     */

    protected String getApiUrl(Map<String, String> paramsMap, String keyName, String fileName) {
        String url = configServiceImpl.getPropertyValueAsString(fileName, keyName);
        final UriTemplate uriTemplate = new UriTemplate(url);
        url = uriTemplate.expand(paramsMap).toString();
        return url;
    }

    protected String getValueFromGlobalAsString(String key){
        return configServiceImpl.getPropertyValueAsString(GLOBAL_CONFIG, key);
    }
    protected int getValueFromGlobalAsInteger(String key){
        return configServiceImpl.getPropertyValueAsInteger(GLOBAL_CONFIG, key);
    }



}
