package com.scent.feedservices;

import com.scent.feedservices.Util.ConfigServiceImpl;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import static com.scent.feedservices.Util.Constants.GLOBAL_CONFIG;
@RunWith(SpringRunner.class)
@SpringBootTest
public class ConfigServiceImplTests {

    @Autowired
    private ConfigServiceImpl configService;

    @Test
    public void testGetPropertyValueAsString(){
        int dayCount = configService.getPropertyValueAsInteger(GLOBAL_CONFIG, "post.expiry.dayCount");
        Assert.assertEquals(1, dayCount);
    }
}
