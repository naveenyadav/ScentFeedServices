package com.scent.feedservices.repositories;

import com.scent.feedservices.Util.DateUtil;
import com.scent.feedservices.data.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

import static com.scent.feedservices.Util.Constants.*;

@Component
public class PostRepositoryImpl extends AbstractRepository{
    @Autowired
    private PostRepository _postRepository;


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


    public ResponseData createNewPost(RequestData requestData){
        Map<String, String> paramMap =  requestData.getDataMap();
        //POST expiry day
        int postExpiryDayCount = getConfigServiceImpl().getPropertyValueAsInteger(GLOBAL_CONFIG, PROP_POST_EXPIRY_DAY);
        String expiryDate = DateUtil.addAdvanceDaysToGivenDate(paramMap.get(DATE), postExpiryDayCount, POST_TIME_PATTERN, TIMEZONE_UTC);
        String createdDate = DateUtil.formatDate(paramMap.get(DATE), POST_TIME_PATTERN, TIMEZONE_UTC);
        Post post = new Post();
        post.setUserId(paramMap.get(USER_ID));
        post.setContent(paramMap.get(CONTENT));
        post.setCreatedDate(createdDate);
        post.setTimeZone(paramMap.get(TIMEZONE));
        post.setExpiryDate(expiryDate);
        Location location = getUserLocation(paramMap);
        post.setLocation(location);
        _postRepository.save(post);
        return null;
    }
    private Location getUserLocation(Map<String, String> paramMap){
        Location location = new Location();
        location.setType(POINT);
        location.setLatitude(Long.parseLong(paramMap.get(LATITUDE)));
        location.setLongitude(Long.parseLong(paramMap.get(LONGITUDE)));
        location.setName(paramMap.get(LOCATION_NAME));
        return location;
    }


}
