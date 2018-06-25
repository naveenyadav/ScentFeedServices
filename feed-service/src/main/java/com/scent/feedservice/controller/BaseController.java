package com.scent.feedservice.controller;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.ResponseData;

import java.util.Map;


public abstract class BaseController {

    public BaseController(){

    }

    protected RequestData getRequestData(Map<String, String> queryParams) {
        RequestData requestData = new RequestData();
        requestData.setDataMap(queryParams);
        return requestData;
    }

    protected ResponseData getResponseData(){
        ResponseData responseData = new ResponseData();
        return responseData;
    }

    protected EventData gerEventData(Map<String, String> queryParams){
        EventData eventData = new EventData();
        RequestData requestData = getRequestData(queryParams);
        eventData.setRequestData(requestData);
        eventData.setResponseData(getResponseData());
        return eventData;

    }



}
