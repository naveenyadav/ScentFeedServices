package com.scent.feedservice.repositories;


import com.scent.feedservice.data.Location;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.ResponseData;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;

import static com.scent.feedservice.Util.Constants.*;


@Component
public class PostRepositoryImpl extends AbstractRepository{


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
//        postRepository.deleteAll();
        Map<String, String> paramMap =  requestData.getDataMap();
//        //POST expiry day
//        int postExpiryDayCount = getConfigServiceImpl().getPropertyValueAsInteger(GLOBAL_CONFIG, PROP_POST_EXPIRY_DAY);
//        String expiryDate = DateUtil.addAdvanceDaysToGivenDate(paramMap.get(DATE), postExpiryDayCount, POST_TIME_PATTERN, TIMEZONE_UTC);
//        String createdDate = DateUtil.formatDate(paramMap.get(DATE), POST_TIME_PATTERN, POST_TIME_PATTERN);
//        Post post = new Post();
//        post.setPostId(UUID.randomUUID().toString());
//        post.setUserId(paramMap.get(USER_ID));
//        post.setContent(paramMap.get(CONTENT));
//        post.setCreatedDate(createdDate);
//        post.setTimeZone(paramMap.get(TIMEZONE));
//        post.setExpiryDate(expiryDate);
//        Location location = getUserLocation(paramMap);
//        post.setLocation(location);
//        Test test = new Test();
//        test.setTestID("1");
//        postRepository.save(test).log()
//                .map(Test::getTestID)
//                .subscribe(System.out::println, System.out::print);
//        employeeRepository.deleteAll()
//                .subscribe(null,null, ()-> {
//                    Stream.of(new Employee(UUID.randomUUID().toString(), "a", "1L"),
//                            new Employee(UUID.randomUUID().toString(), "b", "2L"),
//                            new Employee(UUID.randomUUID().toString(), "c", "3L"),
//                            new Employee(UUID.randomUUID().toString(), "d", "4L"),
//                            new Employee(UUID.randomUUID().toString(), "e", "5L"),
//                            new Employee(UUID.randomUUID().toString(), "f", "6L")
//                    ).forEach(employee -> {
//                        employeeRepository.save(employee).subscribe(System.out::println);
//                    });
//                });


        return null;
    }
    public  void createPost(){

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
