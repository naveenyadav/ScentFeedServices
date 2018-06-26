package com.scent.feedservice.steps.profile;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.profile.Account;
import com.scent.feedservice.repositories.AccountRepository;
import com.scent.feedservice.steps.IStep;
import org.json.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.scent.feedservice.Util.Constants.*;
import static com.scent.feedservice.Util.Constants.MOBILE_NUIMBER;

@Component
public class GetAccountStep implements IStep<JSONObject> {
    private AccountRepository accountRepository;
    public GetAccountStep(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    public Mono<JSONObject>  executeStep(EventData eventData) {
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());
        String signUpType = paramMap.get(LOGIN_TYPE);
        Mono<String> accountCount;
        String email = paramMap.get(EMAIL_ID);
        accountCount = accountRepository.getAccountByEmailExists(email).single();
        if(signUpType.equals(LOGIN_BY_EMAIL)){


            //accountCount = accountRepository.countAccountByEmail(email).single();
        }else if(signUpType.equals(LOGIN_BY_MOBILE)){
            String mobileNumber = paramMap.get(MOBILE_NUIMBER);
            //accountCount = accountRepository.countAccountByMobileNumber(mobileNumber);
        }
        accountCount = accountCount.doOnSuccess(e -> doOnSuccess(e));
        accountCount = accountCount.doOnError(e -> doOnError(e));
        return accountCount.flatMap( count ->{
                    JSONObject jsonObject = new JSONObject();
//                    if(count)
//                        jsonObject.put(ERROR_STATUS_KEY, FAILED);
//                    else {
//                        jsonObject.put(STATUS, SUCCESS);
//                    }
                    return Mono.just(jsonObject);
        });

    }

    public void doOnSuccess(String  count){
        System.out.println(count);
    }
    public void doOnError(Throwable error){
        System.out.println(error);
    }
}
