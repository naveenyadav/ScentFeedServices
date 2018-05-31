package com.scent.feedservices;

import com.scent.feedservices.Util.ConfigServiceImpl;

import com.scent.feedservices.data.Post;
import com.scent.feedservices.data.RequestData;
import com.scent.feedservices.repositories.PostRepositoryImpl;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static com.scent.feedservices.Util.Constants.*;
import static com.scent.feedservices.Util.Constants.POST_TYPE;
import static com.scent.feedservices.Util.Constants.USER_ID;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigServiceImplTests {
    @Autowired
    PostRepositoryImpl postRepository;

    @Autowired
    private ConfigServiceImpl configService;

//    @Test
//    public void testGetPropertyValueAsString(){
//        int dayCount = configService.getPropertyValueAsInteger(GLOBAL_CONFIG, "post.expiry.dayCount");
//        Assert.assertEquals(1, dayCount);
//    }

    @Test
    public void createPost(){
        RequestData requestData = new RequestData();
        requestData.setParam(CONTENT, "content");
        requestData.setParam(DATE, "2018-05-30T19:35:22.346Z");
        requestData.setParam(TIMEZONE, "IST");
        requestData.setParam(LATITUDE, "1");
        requestData.setParam(LONGITUDE, "2");
        requestData.setParam(LOCATION_NAME, "Taj Mahal");
        requestData.setParam(POST_TYPE, "IMAGE");
        requestData.setParam(USER_ID, "1");
        postRepository.createNewPost(requestData);
    }
}
