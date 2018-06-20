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
import static com.scent.feedservice.Util.Constants.LOGIN_BY_MOBILE;
import static com.scent.feedservice.Util.Constants.MOBILE_NUIMBER;

@Component
public class SignUp  implements IAction {

    private AccountRepository accountRepository;
    public SignUp(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }
    public void perFormAction(EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());




    }

    public Account getAccount( Map<String, String> paramMap){
        Account account = new Account();
        account.setFirstName(paramMap.get(FIRST_NAME));
        account.setLastName(paramMap.get(LAST_NAME));
        account.setPassword(paramMap.get(PASSWORD));
        String signupType = paramMap.get(LOGIN_TYPE);

        if(signupType.equals(LOGIN_BY_EMAIL)){
            String mobileNumber = paramMap.get(EMAIL_ID);

        }else if(signupType.equals(LOGIN_BY_MOBILE)){
            String mobileNumber = paramMap.get(MOBILE_NUIMBER);

        }
    }
}