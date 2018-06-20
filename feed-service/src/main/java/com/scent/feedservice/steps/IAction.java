package com.scent.feedservice.steps;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.ResponseData;

import java.util.HashMap;
import java.util.Map;

public interface IAction {

    void perFormAction(EventData eventData);

    default Map<String, String> getRequestParamsCopy(Map<String, String> dataMap) {
        Map<String, String> paramsMap = new HashMap<>();
        paramsMap.putAll(dataMap);
        return paramsMap;
    }

    default ResponseData updateResponse(String eventName, EventData eventData,
                                          Object data) {
        // Set response data.
        ResponseData responseData = eventData.getResponseData();
        responseData.setData(eventName, data);
        return responseData;
    }



}
