package com.scent.feedservice.controller;

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



}
