package com.scent.feedservice.controller;

import com.scent.feedservice.data.Employee;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.repositories.CommentRepository;
import com.scent.feedservice.repositories.PostRepositoryImpl;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.UUID;

import static com.scent.feedservice.Util.Constants.*;

/**
 * This controller class file is used to handle following:
 * Purpose:
 * Methods:
 *
 * @author nyadav
 */
@RestController
@RequestMapping("/comment")
public class CommentController {
    private PostRepositoryImpl postRepositoryImpl;
    public CommentController(PostRepositoryImpl postRepositoryImpl){
        this.postRepositoryImpl = postRepositoryImpl;
    }
    /**
     * This GET controller method is used to handle following.
     * Purpose:
     *
     * @param queryParams map of all request parameter name and its value
     * @return
     */
    @RequestMapping(value = "/getComments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getComments(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
        RequestData requestData = new RequestData();
        requestData.setParam(CONTENT, "content");
        requestData.setParam(DATE, "2018-05-30T19:35:22.346Z");
        requestData.setParam(TIMEZONE, "IST");
        requestData.setParam(LATITUDE, "1");
        requestData.setParam(LONGITUDE, "2");
        requestData.setParam(LOCATION_NAME, "Taj Mahal");
        requestData.setParam(POST_TYPE, "IMAGE");
        requestData.setParam(USER_ID, "1");
        postRepositoryImpl.createNewPost(requestData);

        return "Success";

    }
}
