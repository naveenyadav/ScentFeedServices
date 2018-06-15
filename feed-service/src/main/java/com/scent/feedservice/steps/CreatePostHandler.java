package com.scent.feedservice.steps;


import com.scent.feedservice.Util.ConfigServiceImpl;
import com.scent.feedservice.Util.DateUtil;
import com.scent.feedservice.data.*;
import com.scent.feedservice.data.feed.Location;
import com.scent.feedservice.data.feed.Post;
import com.scent.feedservice.data.feed.PrivacyType;
import com.scent.feedservice.repositories.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.util.Logger;
import reactor.util.Loggers;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.scent.feedservice.Util.Constants.*;


@Component
public class CreatePostHandler implements IAction {
    private Logger LOG = Loggers.getLogger(CreatePostHandler.class);

    @Autowired
    protected ConfigServiceImpl configServiceImpl;
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
//        addToRequiredFieldList(requiredFields);
    }

    public void perFormAction(EventData eventData){
        final RequestData requestData = eventData.getRequestData();
        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());
        Post post = getPostbody(paramMap);
        postRepository.save(post).subscribe();
    }
    private Post getPostbody(Map<String, String> paramMap){
        //POST expiry day count
        int postExpiryDayCount = configServiceImpl.getPropertyValueAsInteger(GLOBAL_CONFIG, PROP_POST_EXPIRY_DAY);

        Date expiryDate = DateUtil.addAdvanceDaysToGivenDate(paramMap.get(DATE), postExpiryDayCount, POST_TIME_PATTERN, TIMEZONE_UTC);

        Date createdDate = DateUtil.getFormatDate(paramMap.get(DATE), POST_TIME_PATTERN, POST_TIME_PATTERN);

        Post post = new Post();
        post.setPostId(UUID.randomUUID().toString());
        post.setPrivacy(PrivacyType.PUBLIC);
        post.setUserId(paramMap.get(USER_ID));
        post.setContent(paramMap.get(CONTENT));
        post.setCreatedDate(createdDate);
        post.setExpiryDate(expiryDate);
        post.setLocationName(paramMap.get(LOCATION_NAME));
        Double loc[] =  new Double[]{ Double.parseDouble(paramMap.get(LONGITUDE)), Double.parseDouble(paramMap.get(LATITUDE))};
        post.setLocation(loc);
        return post;
    }

    private void createHashtag(String content){
        Pattern MY_PATTERN = Pattern.compile("#[A-Za-z0-9]+");
        Matcher mat = MY_PATTERN.matcher(content);
        List<String> strs=new ArrayList<String>();
        while (mat.find()) {
            strs.add(mat.group(1));
        }
    }

//    private Location getUserLocation(Map<String, String> paramMap){
////        Location location = new Location();
////        location.setType(POINT);
////        location.setLatitude(Double.parseDouble(paramMap.get(LATITUDE)));
////        location.setLongitude(Double.parseDouble(paramMap.get(LONGITUDE)));
////        location.setName(paramMap.get(LOCATION_NAME));
//        return location;
//    }

//    public void upVote(EventData eventData){
//        final RequestData requestData = eventData.getRequestData();
//        Map<String, String> paramMap =  getRequestParamsCopy(requestData.getDataMap());
//        // Get Post from id
//        Mono<Post> postMono = postRepository.getPostByPostId(paramMap.get(POST_ID));
//        postMono.flatMap(post -> {
//            //Rule 1 - is not owner of the post
//            boolean result = !post.getUserId().equals(paramMap.get(USER_ID));
//
////            if(post.addToUpVote(paramMap.get(USER_ID))){
////                String expiryDate = post.getExpiryDate();
////                int upVoteHour = getValueFromGlobalAsInteger(upVoteIncrement(post));
////                String date = DateUtil.updateHourToExpiryDate(expiryDate, upVoteHour, POST_TIME_PATTERN, TIMEZONE_UTC);
////                post.setExpiryDate(date);
////            }
//            return Mono.just(post);
//        }).flatMap(postRepository::save).subscribe(System.out::println);
//
//    }
//
//    private String upVoteIncrement(Post post){
////        if(post.getUpVote().size() >= 2000)
////            return PROP_POST_HOUR_UP_VOTE_2000;
////        else if(post.getUpVote().size() >= 1000 && post.getUpVote().size() < 1000){
////            return PROP_POST_HOUR_UP_VOTE_1000;
////        }
//        return PROP_POST_HOUR_UP_VOTE;
//    }

//    private boolean timeDifference(Map<String, String> paramMap, Post post){
//        //String currentDate = DateUtil.convertDateToTimeZone(paramMap.get(DATE), POST_TIME_PATTERN, paramMap.get(TIMEZONE), POST_TIME_PATTERN, TIMEZONE_UTC);
//        long currentTimeInMillis = DateUtil.getTimeInMillis(paramMap.get(DATE), POST_TIME_PATTERN, TIMEZONE_UTC);
//        long expiryTimeInMillis = DateUtil.getTimeInMillis(post.getExpiryDate(), POST_TIME_PATTERN, TIMEZONE_UTC);
//        if(expiryTimeInMillis < currentTimeInMillis){
//            return false;
//        }
//        return true;
//    }









}
