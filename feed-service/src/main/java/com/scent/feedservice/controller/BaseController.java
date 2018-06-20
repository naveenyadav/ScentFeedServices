package com.scent.feedservice.controller;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;

import java.util.Map;


public abstract class BaseController {

    public BaseController(){

    }

    protected RequestData getRequestData(Map<String, String> queryParams) {
        RequestData requestData = new RequestData();
        requestData.setDataMap(queryParams);
        return requestData;
    }

    protected EventData gerEventData(Map<String, String> queryParams){
        EventData eventData = new EventData();
        RequestData requestData = getRequestData(queryParams);
        eventData.setRequestData(requestData);
        return eventData;

    }



}
