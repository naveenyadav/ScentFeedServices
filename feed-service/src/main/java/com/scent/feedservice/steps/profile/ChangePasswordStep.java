package com.scent.feedservice.steps.profile;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.ResponseData;
import com.scent.feedservice.data.profile.Account;
import com.scent.feedservice.repositories.AccountRepository;
import com.scent.feedservice.steps.IAction;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.scent.feedservice.Util.Constants.*;
import static com.scent.feedservice.Util.Constants.LOGIN_BY_MOBILE;
import static com.scent.feedservice.Util.Constants.MOBILE_NUIMBER;

@Component
public class ChangePasswordStep implements IAction {
    private AccountRepository accountRepository;
    public ChangePasswordStep(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }
    public ResponseData perFormAction(EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());

        final String oldPassword = paramMap.get(OLD_PASSWORD);
        final String newPassword = paramMap.get(NEW_PASSWORD);
        if(oldPassword.equals(newPassword)){

        }
        String loginType = paramMap.get(SIGNUP_TYPE);
        if(loginType.equals(LOGIN_BY_EMAIL)){
            String emailId = paramMap.get(EMAIL_ID);
            Mono<Account> accountMono = accountRepository.getAccountByEmailAndPassword(emailId, oldPassword).flatMap(account -> {
                if(account.getPassword().equals(oldPassword)){
                    account.setPassword(newPassword);
                    return Mono.just(account);
                }
                return Mono.empty();
            });
        }else if(loginType.equals(LOGIN_BY_MOBILE)){
            String mobileNumber = paramMap.get(MOBILE_NUIMBER);
            Mono<Account> accountMono = accountRepository.getAccountByEmailAndPassword(mobileNumber, oldPassword).flatMap(account -> {
                if(account.getPassword().equals(oldPassword)){
                    account.setPassword(newPassword);
                }
                return Mono.just(account);
            });
        }
        return eventData.getResponseData();

    }
}
