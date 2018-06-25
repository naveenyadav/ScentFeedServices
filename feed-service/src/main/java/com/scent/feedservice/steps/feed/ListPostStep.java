package com.scent.feedservice.steps.feed;

import com.scent.feedservice.Util.DateUtil;
import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.ResponseData;
import com.scent.feedservice.data.feed.Post;
import com.scent.feedservice.data.feed.StatusType;
import com.scent.feedservice.steps.IAction;
import org.springframework.data.domain.Sort;
import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.*;

import static com.scent.feedservice.Util.Constants.*;
@Component
public class ListPostStep implements IAction {
    private final ReactiveMongoOperations mongoOperations;

    public ListPostStep(ReactiveMongoOperations mongoOperations){
        this.mongoOperations = mongoOperations;
    }
    public ResponseData perFormAction(EventData eventData){
        return eventData.getResponseData();


    }

    public Flux<Post> getPosts(EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());

        double latitude = Double.parseDouble(paramMap.get(LATITUDE));
        double longitude = Double.parseDouble(paramMap.get(LONGITUDE));
        double distance = Double.parseDouble(paramMap.get(RADIUS));

        Date currentDate = DateUtil.getFormatDate(paramMap.get(USER_DATE), POST_TIME_PATTERN, POST_TIME_PATTERN);

        String privacyTypes = paramMap.get(PRIVACY_TYPE);
        List<String> privacyArray = new ArrayList<>();

        if(privacyTypes.contains(COMMA)){
            privacyArray = Arrays.asList(privacyTypes.split(COMMA));
        }else{
            privacyArray.add(privacyTypes);
        }

        String lim = paramMap.get(LIMIT);
        int limit  = Integer.parseInt(lim);

        Circle circle = new Circle(longitude, latitude, distance);

        //Filter by location
        Query q = new Query(new Criteria(LOCATION).withinSphere(circle));

        //Filter by Expiry Date
        q.addCriteria(new Criteria(EXPIRY_DATE).gte(currentDate));
        q.addCriteria(new Criteria(PRIVACY).in(privacyArray));
        q.addCriteria(new Criteria(FLAG_TO_DELETE).in(StatusType.POSTED));
        q.with(new Sort(Sort.Direction.ASC, EXPIRY_DATE));
        q.with(new Sort(Sort.Direction.ASC, UP_VOTES));
        return mongoOperations.find(q, Post.class);
    }
}
