package com.scent.feedservice.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.steps.CreatePostHandler;
import com.scent.feedservice.steps.ListPost;
import com.scent.feedservice.steps.UpVoteAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.scent.feedservice.Util.Constants.*;
import static com.scent.feedservice.Util.Constants.POST_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * This controller class file is used to handle following:
 * Purpose:
 * Methods:
 *
 * @author nyadav
 */
@RestController
@RequestMapping("/post")
public class PostController extends BaseController {
    @Autowired
    private ListPost likePost;
    private CreatePostHandler createPostHandler;
    public PostController(CreatePostHandler createPostHandler){
        this.createPostHandler = createPostHandler;
    }


    /**
     * This GET controller method is used to handle following.
     * Purpose: This method creates posts.
     *
     * @param queryParams map of all request parameter name and its value
     * @return
     */
    @RequestMapping(value = "/createPost", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getMono(@RequestParam Map<String, String> queryParams) {
        queryParams.put(CONTENT, "content");
        queryParams.put(DATE, "2018-05-30T23:35:22.346Z");
        queryParams.put(TIMEZONE, "IST");
        queryParams.put(LOCATION_NAME, "Taj Mahal");
        queryParams.put(LATITUDE, "1");
        queryParams.put(LONGITUDE, "2");
        queryParams.put(POST_TYPE, "IMAGE");
        //queryParams.put(USER_ID, "6");
        EventData eventData = new EventData();

        RequestData requestData = getRequestData(queryParams);

        eventData.setRequestData(requestData);
        createPostHandler.perFormAction(eventData);

        return "Success";
    }

    /**
     * This GET controller method is used to handle following.
     * Purpose: This method creates posts.
     *
     * @param queryParams map of all request parameter name and its value
     * @return
     */
    @RequestMapping(value = "/getPosts", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public String getFlux(@RequestParam Map<String, String> queryParams) {
        EventData eventData = new EventData();
        RequestData requestData = getRequestData(queryParams);

        eventData.setRequestData(requestData);
        likePost.perFormAction(eventData);
        return "Success";
    }
}
