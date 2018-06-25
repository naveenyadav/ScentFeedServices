package com.scent.controller;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.ResponseData;
import org.junit.Test;

public class AuthenticationControllerTest {

    @Test
    public void signup(){
        EventData eventData = new EventData();
        RequestData requestData = new RequestData();
        ResponseData responseData = new ResponseData();
        eventData.setRequestData(requestData);
        eventData.setResponseData(responseData);

    }
}
