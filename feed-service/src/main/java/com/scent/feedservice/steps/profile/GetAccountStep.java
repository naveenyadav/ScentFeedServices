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
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class GetAccountStep implements IStep<Long> {
    private AccountRepository accountRepository;
    public GetAccountStep(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    public Mono<Long>  executeStep(EventData eventData) {
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());
        String signUpType = paramMap.get(LOGIN_TYPE);
        Mono<Long> accountCount = Mono.empty();
        if(signUpType.equals(LOGIN_BY_EMAIL)){
            String email = paramMap.get(EMAIL_ID);
            accountCount = accountRepository.countAccountByEmail(email);
        }else if(signUpType.equals(LOGIN_BY_MOBILE)){
            String mobileNumber = paramMap.get(MOBILE_NUIMBER);
            accountCount = accountRepository.countAccountByMobileNumber(mobileNumber);
        }
        accountCount.flatMap( count ->{
                    JSONObject jsonObject = new JSONObject();
                    if(count > 0)
                        jsonObject.put(STATUS, SUCCESS);
                    else {
                        jsonObject.put(STATUS, FAILED);
                    }
                    return Mono.just(jsonObject);
        });
        accountCount.doOnSuccess(e -> doOnSuccess(e));
        accountCount.doOnError(e -> doOnError(e));
        return accountCount;
    }

    public void doOnSuccess(Long count){
        System.out.println(count);
    }
    public void doOnError(Throwable error){
        System.out.println(error);
    }
}
