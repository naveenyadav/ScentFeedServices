package com.scent.feedservice.repositories;


import com.scent.feedservice.Util.Constants;
import com.scent.feedservice.Util.DateUtil;
import com.scent.feedservice.data.*;
import com.scent.feedservice.data.feed.Coordinate;
import com.scent.feedservice.data.feed.Location;
import com.scent.feedservice.data.feed.Post;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;


import javax.annotation.PostConstruct;
import java.util.Date;
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
        //postRepository.getPostByPostId(paramMap.get(POST_ID)).subscribe(System.out::println);
        upVote(eventData);
        return null;
    }

    private Post updatePost(Map<String, String> paramMap){
        //postRepository.getPostByPostId(paramMap.get(POST_ID)).subscribe(System.out::println);
        return null;
    }

    public void performAction(EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());
        // Get Post from id
        Mono<Post> postMono = postRepository.getPostByPostId(paramMap.get(POST_ID));
        postMono.flatMap(post -> {
            String expiryDate = post.getExpiryDate();
            int downVoteHour = getValueFromGlobalAsInteger(PROP_POST_HOUR_DOWN_VOTE);
            String date = DateUtil.updateHourToExpiryDate(expiryDate, downVoteHour, POST_TIME_PATTERN, TIMEZONE_UTC);
            post.setExpiryDate(date);
            return Mono.just(post);
        }).flatMap(post -> {
            if (timeDifference(paramMap, post)) {
                return postRepository.save(post);
            } else{
                return Mono.empty();
            }
        }).subscribe(System.out::println);

    }

    public void upVote(EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());
        // Get Post from id
        Mono<Post> postMono = postRepository.getPostByPostId(paramMap.get(POST_ID));
        postMono.flatMap(post -> {
            //Rule 1 - is not owner of the post
            boolean result = !post.getUserId().equals(paramMap.get(USER_ID));

//            if(post.addToUpVote(paramMap.get(USER_ID))){
//                String expiryDate = post.getExpiryDate();
//                int upVoteHour = getValueFromGlobalAsInteger(upVoteIncrement(post));
//                String date = DateUtil.updateHourToExpiryDate(expiryDate, upVoteHour, POST_TIME_PATTERN, TIMEZONE_UTC);
//                post.setExpiryDate(date);
//            }
            return Mono.just(post);
        }).flatMap(postRepository::save).subscribe(System.out::println);

    }

    private String upVoteIncrement(Post post){
//        if(post.getUpVote().size() >= 2000)
//            return PROP_POST_HOUR_UP_VOTE_2000;
//        else if(post.getUpVote().size() >= 1000 && post.getUpVote().size() < 1000){
//            return PROP_POST_HOUR_UP_VOTE_1000;
//        }
        return PROP_POST_HOUR_UP_VOTE;
    }

    private boolean timeDifference(Map<String, String> paramMap, Post post){
        //String currentDate = DateUtil.convertDateToTimeZone(paramMap.get(DATE), POST_TIME_PATTERN, paramMap.get(TIMEZONE), POST_TIME_PATTERN, TIMEZONE_UTC);
        long currentTimeInMillis = DateUtil.getTimeInMillis(paramMap.get(DATE), POST_TIME_PATTERN, TIMEZONE_UTC);
        long expiryTimeInMillis = DateUtil.getTimeInMillis(post.getExpiryDate(), POST_TIME_PATTERN, TIMEZONE_UTC);
        if(expiryTimeInMillis < currentTimeInMillis){
            return false;
        }
        return true;
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
        //post.setTimeZone(paramMap.get(TIMEZONE));
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
