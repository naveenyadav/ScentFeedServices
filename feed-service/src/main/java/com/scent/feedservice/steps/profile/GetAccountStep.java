package com.scent.feedservice.steps.profile;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.profile.Account;
import com.scent.feedservice.repositories.AccountRepository;
import com.scent.feedservice.steps.IStep;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.scent.feedservice.Util.Constants.*;
import static com.scent.feedservice.Util.Constants.MOBILE_NUIMBER;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;

@Component
public class GetAccountStep implements IStep {
    private AccountRepository accountRepository;
    public GetAccountStep(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    @Override
    public Mono<ServerResponse>  executeStep(EventData eventData) {
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());
        String signupType = paramMap.get(LOGIN_TYPE);
        Mono<Long> accountCount = Mono.empty();
        if(signupType.equals(LOGIN_BY_EMAIL)){
            String email = paramMap.get(EMAIL_ID);
            accountCount = accountRepository.countAccountByEmail(email);

        }else if(signupType.equals(LOGIN_BY_MOBILE)){
            String mobileNumber = paramMap.get(MOBILE_NUIMBER);
            accountCount = accountRepository.countAccountByMobileNumber(mobileNumber);
        }
        return accountCount
                .flatMap(account -> ServerResponse.ok().contentType(APPLICATION_JSON).body(fromObject(account)))
                .switchIfEmpty(notFound);
    }
}
