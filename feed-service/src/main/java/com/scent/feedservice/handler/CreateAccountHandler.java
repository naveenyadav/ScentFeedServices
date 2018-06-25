package com.scent.feedservice.handler;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.steps.profile.GetAccountStep;
import com.scent.feedservice.steps.profile.CreateAccountStep;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static com.scent.feedservice.Util.Constants.*;
@Component
public class CreateAccountHandler {
    @Autowired
    GetAccountStep getAccountStep;
    @Autowired
    CreateAccountStep signUp;
    public Mono<JSONObject> getresult(EventData eventData){
        return getAccountStep.executeStep(eventData).flatMap(count -> {
            if(count > 0 ){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(STATUS, FAILED);
                jsonObject.put(STAUS_CODE, ACOUNT_ALREADY_EXISTS_CODE);
                return Mono.just(jsonObject);
            }else{
                return  signUp.executeStep(eventData);
            }
        });
    }
}
