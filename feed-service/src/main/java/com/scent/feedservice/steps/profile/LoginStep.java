package com.scent.feedservice.steps.profile;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.profile.Account;
import com.scent.feedservice.repositories.AccountRepository;

import com.scent.feedservice.steps.IAction;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.scent.feedservice.Util.Constants.*;

@Component
public class LoginStep  implements IAction {

    private AccountRepository accountRepository;
    public LoginStep(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }
    public void perFormAction(EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());

        String password = paramMap.get(PASSWORD);
        String loginType = paramMap.get(SIGNUP_TYPE);
        if(loginType.equals(LOGIN_BY_EMAIL)){
            String emailId = paramMap.get(EMAIL_ID);
            Mono<Account> accountMono = accountRepository.getAccountByEmailAndPassword(emailId, password);
        }else if(loginType.equals(LOGIN_BY_MOBILE)){
            String mobileNumber = paramMap.get(MOBILE_NUIMBER);
            Mono<Account> accountMono = accountRepository.getAccountByEmailAndPassword(mobileNumber, password);
        }

    }
}
