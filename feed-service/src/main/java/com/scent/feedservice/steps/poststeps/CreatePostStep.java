package com.scent.feedservice.steps.poststeps;


import com.scent.feedservice.Util.ConfigServiceImpl;
import com.scent.feedservice.Util.DateUtil;
import com.scent.feedservice.data.*;
import com.scent.feedservice.data.feed.*;
import com.scent.feedservice.repositories.PostRepository;
import com.scent.feedservice.steps.IAction;
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
public class CreatePostStep implements IAction {
    private Logger LOG = Loggers.getLogger(CreatePostStep.class);
    private final String DATE_YYYY_FORMAT = "YYYY";
    private final String DATE_MM_FORMAT = "MM";

    @Autowired
    protected ConfigServiceImpl configServiceImpl;
    private PostRepository postRepository;
    public CreatePostStep(PostRepository postRepository){
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

        String date = paramMap.get(USER_DATE);

        Date createdDate = DateUtil.getFormatDate(date, POST_TIME_PATTERN, POST_TIME_PATTERN);

        Date expiryDate = DateUtil.addAdvanceDaysToGivenDate(date, postExpiryDayCount, POST_TIME_PATTERN, TIMEZONE_UTC);

        Post post = new Post();
        //Set postId
        post.setPostId(UUID.randomUUID().toString());
        //Set privacy
        post.setPrivacy(PrivacyType.valueOf(paramMap.get(PRIVACY_TYPE)));
        //Set userId
        post.setUserId(paramMap.get(USER_ID));
        //Set content
        post.setContent(paramMap.get(CONTENT));
        //Set creation date
        post.setCreatedDate(createdDate);
        //Set Expiry Date
        post.setExpiryDate(expiryDate);
        //Set Location Name
        post.setLocationName(paramMap.get(LOCATION_NAME));
        //Set Longitude and latitude
        Double loc[] =  new Double[]{Double.parseDouble(paramMap.get(LONGITUDE)), Double.parseDouble(paramMap.get(LATITUDE))};
        post.setLocation(loc);
        //Set flag to Delete
        post.setFlagToDelete(StatusType.POSTED);
        //Set post type
        post.setPostType(PostType.valueOf(paramMap.get(POST_TYPE)));
        //Set hide location
        post.setLocationHidden(Boolean.valueOf(paramMap.get(LOCATION_HIDDEN)));
        //Set image URL
        post.setImageUrl(getImageURL(date));

        //post.setMediaFormat(paramMap.get());
        return post;
    }

    private String getImageURL(String createdDate){
        return FORWARD_SLASH.concat(DateUtil.formatDate(createdDate, POST_TIME_PATTERN, DATE_YYYY_FORMAT))
        .concat(FORWARD_SLASH).concat(DateUtil.formatDate(createdDate, POST_TIME_PATTERN, DATE_MM_FORMAT))
        .concat(FORWARD_SLASH);
    }

    private void createHashtag(String content){
        Pattern MY_PATTERN = Pattern.compile("#[A-Za-z0-9]+");
        Matcher mat = MY_PATTERN.matcher(content);
        List<String> strs=new ArrayList<String>();
        while (mat.find()) {
            strs.add(mat.group(1));
        }
    }
}
