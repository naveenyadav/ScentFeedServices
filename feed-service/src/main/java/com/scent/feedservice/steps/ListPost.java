package com.scent.feedservice.steps;

import com.scent.feedservice.Util.CustomCriteria;
import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.feed.Post;
import org.springframework.data.geo.Circle;
import org.springframework.data.mongodb.core.ReactiveMongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.Map;

import static com.scent.feedservice.Util.Constants.*;
@Component
public class ListPost implements IAction {
    private final ReactiveMongoOperations mongoOperations;

    public ListPost(ReactiveMongoOperations mongoOperations){
        this.mongoOperations = mongoOperations;
    }
    public void perFormAction(EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());

        double latitude = Double.parseDouble(paramMap.get(LATITUDE));
        Assert.notNull(latitude, "Latitude must not be null!");
        double longitude = Double.parseDouble(paramMap.get(LONGITUDE));
        Assert.notNull(longitude, "Longitude must not be null!");
        double distance = Double.parseDouble(paramMap.get(RADIUS));
        Assert.notNull(distance, "Distance must not be null!");

        String lim = paramMap.get(LIMIT);
        Assert.notNull(lim, "Limit must not be null!");
        int limit  = Integer.parseInt(lim);

        Circle circle = new Circle(longitude, latitude, distance);

        Query q = new Query(new Criteria("location").withinSphere(circle));
        System.out.println(q.toString());

         mongoOperations.find(q, Post.class, "location").subscribe(System.out::println);


    }
}
