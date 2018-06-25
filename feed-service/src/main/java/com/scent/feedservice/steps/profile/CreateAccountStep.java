package com.scent.feedservice.steps.profile;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.ResponseData;
import com.scent.feedservice.data.feed.Like;
import com.scent.feedservice.data.profile.Account;
import com.scent.feedservice.data.profile.AccountStatus;
import com.scent.feedservice.data.profile.AccountType;
import com.scent.feedservice.data.profile.Report;
import com.scent.feedservice.repositories.AccountRepository;
import com.scent.feedservice.repositories.LikeRepository;
import com.scent.feedservice.repositories.ReportRepository;
import com.scent.feedservice.steps.IAction;
import com.scent.feedservice.steps.IStep;
import org.json.JSONObject;
import org.reactivestreams.Publisher;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple3;
import java.util.Map;
import java.util.UUID;

import static com.scent.feedservice.Util.Constants.*;
import static com.scent.feedservice.Util.Constants.LOGIN_BY_MOBILE;
import static com.scent.feedservice.Util.Constants.MOBILE_NUIMBER;

@Component
public class CreateAccountStep implements IStep<JSONObject> {

    private AccountRepository accountRepository;
    private LikeRepository likeRepository;
    private ReportRepository reportRepository;

    public CreateAccountStep(AccountRepository accountRepository, ReportRepository reportRepository, LikeRepository likeRepository){
        this.accountRepository = accountRepository;
        this.likeRepository = likeRepository;
        this.reportRepository = reportRepository;
    }
    public Mono<JSONObject> executeStep(EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());

        Account account = createAccount(paramMap);
        String userId = account.getUserId();
        Like like = createLike(userId);
        Report report = createReport(userId);
        Mono<Account> accountMono = accountRepository.save(account);
        Mono<Like> likeMono = likeRepository.save(like);
        Mono<Report> reportMono = reportRepository.save(report);
        Mono<JSONObject> jsonObjectMono =  Mono.zip(accountMono, likeMono, reportMono).flatMap( tuple -> handleAccount(tuple));
        return jsonObjectMono;
    }
    public Mono<JSONObject> handleAccount(Tuple3<Account, Like, Report> profile){
        Account account = profile.getT1();
        JSONObject jsonObject = new JSONObject();
        if(null != account){
            jsonObject.put(STATUS, SUCCESS);
        }else{
            jsonObject.put(STATUS, FAILED);
            jsonObject.put(STAUS_CODE, ACCOUNT_CREATION_FAILED_CODE);

        }
        return Mono.just(jsonObject);
    }

    public Account createAccount( Map<String, String> paramMap){
        Account account = new Account();
        account.setFirstName(paramMap.get(FIRST_NAME));
        account.setLastName(paramMap.get(LAST_NAME));
        account.setPassword(paramMap.get(PASSWORD));
        String signupType = paramMap.get(LOGIN_TYPE);
        if(signupType.equals(LOGIN_BY_EMAIL)){
            String email = paramMap.get(EMAIL_ID);
            account.setEmail(email);
        }else if(signupType.equals(LOGIN_BY_MOBILE)){
            String mobileNumber = paramMap.get(MOBILE_NUIMBER);
            account.setMobileNumber(mobileNumber);
        }
        account.setAccountType(AccountType.PUBLIC);
        account.setGender(paramMap.get(GENDER));
        account.setStatus(AccountStatus.CREATED);
        account.setUserId(UUID.randomUUID().toString());
        return account;
    }

    public Like createLike(String userId){
        Like like = new Like();
        like.setUserId(userId);
        return like;
    }

    public Report createReport(String userId){
        Report report = new Report();
        report.setUserId(userId);
        return report;
    }
}