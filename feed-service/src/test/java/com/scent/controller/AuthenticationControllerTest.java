package com.scent.controller;

import com.scent.feedservice.FeedServiceApplication;
import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.ResponseData;
import com.scent.feedservice.handler.CreateAccountHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.test.StepVerifier;

import java.util.HashMap;
import java.util.Map;

import static com.scent.feedservice.Util.Constants.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FeedServiceApplication.class)
public class AuthenticationControllerTest {
    @Autowired
    CreateAccountHandler createAccountHandler;
    @Test
    public void signup(){
        EventData eventData = new EventData();
        RequestData requestData = new RequestData();
        ResponseData responseData = new ResponseData();

        eventData.setResponseData(responseData);
        Map<String, String> queryParams = new HashMap<>();
        queryParams.put(LOGIN_TYPE, "LoginByEmail");
        queryParams.put(EMAIL_ID, "09bit097@gmail.com");
        queryParams.put(FIRST_NAME, "Naveen");
        queryParams.put(LAST_NAME, "YADAV");
        queryParams.put(PASSWORD, "12345");
        queryParams.put(GENDER, "male");
        requestData.setDataMap(queryParams);
        eventData.setRequestData(requestData);
        StepVerifier.
                create(createAccountHandler.getresult(eventData)).verifyComplete();

    }
}
