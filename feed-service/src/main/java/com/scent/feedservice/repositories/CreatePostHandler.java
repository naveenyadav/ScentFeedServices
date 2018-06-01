package com.scent.feedservice.repositories;


import com.scent.feedservice.Util.Constants;
import com.scent.feedservice.Util.DateUtil;
import com.scent.feedservice.data.*;
import com.scent.feedservice.data.feed.Coordinate;
import com.scent.feedservice.data.feed.Location;
import com.scent.feedservice.data.feed.Post;
import org.springframework.stereotype.Component;


import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.UUID;

import static com.scent.feedservice.Util.Constants.*;


@Component
public class CreatePostHandler extends AbstractRepository{

    private PostRepository postRepository;
    public CreatePostHandler(PostRepository postRepository){
        this.postRepository = postRepository;
    }

    @PostConstruct
    public void init() {
        String[] requiredFields = new String[]{CONTENT,
                DATE,
                TIMEZONE,
                LATITUDE,
                LONGITUDE,
                LOCATION_NAME,
                USER_ID,
                POST_TYPE};
        addToRequiredFieldList(requiredFields);
    }


    public ResponseData handleEvent(EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());
        //Post post = getPostbody(paramMap);
        //postRepository.save(post).subscribe(System.out::println);
        postRepository.getPostByPostId(paramMap.get(POST_ID)).subscribe(System.out::println);
        return null;
    }

    private Post updatePost(Map<String, String> paramMap){
        //postRepository.getPostByPostId(paramMap.get(POST_ID)).subscribe(System.out::println);
        return null;
    }

    private Post getPostbody(Map<String, String> paramMap){
        //POST expiry day count
        int postExpiryDayCount = getValueFromGlobalAsInteger(PROP_POST_EXPIRY_DAY);
        String expiryDate = DateUtil.addAdvanceDaysToGivenDate(paramMap.get(DATE), postExpiryDayCount, POST_TIME_PATTERN, TIMEZONE_UTC);
        String createdDate = DateUtil.formatDate(paramMap.get(DATE), POST_TIME_PATTERN, POST_TIME_PATTERN);
        Post post = new Post();
        post.setPostId(UUID.randomUUID().toString());
        post.setUserId(paramMap.get(USER_ID));
        post.setContent(paramMap.get(CONTENT));
        post.setCreatedDate(createdDate);
        post.setTimeZone(paramMap.get(TIMEZONE));
        post.setExpiryDate(expiryDate);
        Location location = getUserLocation(paramMap);
        post.setLocation(location);
        return post;
    }

    private Location getUserLocation(Map<String, String> paramMap){
        Location location = new Location();
        location.setType(POINT);
        location.setLatitude(Double.parseDouble(paramMap.get(LATITUDE)));
        location.setLongitude(Double.parseDouble(paramMap.get(LONGITUDE)));
        location.setName(paramMap.get(LOCATION_NAME));
        return location;
    }







}
