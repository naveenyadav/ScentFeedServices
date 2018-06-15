package com.scent.feedservice.controller;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.feed.Post;
import com.scent.feedservice.steps.CreatePostHandler;
import com.scent.feedservice.steps.ListPost;
import com.scent.feedservice.steps.UpVoteAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

import static com.scent.feedservice.Util.Constants.*;
import static com.scent.feedservice.Util.Constants.POST_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

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
    @RequestMapping(value = "/createPost", method = GET, produces = APPLICATION_JSON_VALUE)
    public String getMono(@RequestParam Map<String, String> queryParams) {
        queryParams.put(CONTENT, "content");
        queryParams.put(TIMEZONE, "IST");
        queryParams.put(LOCATION_NAME, "Taj Mahal");

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
    @RequestMapping(value = "/getPosts", method = GET, produces = APPLICATION_JSON_VALUE)
    public Flux<Post> getFlux(@RequestParam Map<String, String> queryParams) {
        EventData eventData = new EventData();
        RequestData requestData = getRequestData(queryParams);

        eventData.setRequestData(requestData);
        return likePost.getPosts(eventData);

    }

    @RequestMapping(value = "/changePrivacy", method = GET, produces = APPLICATION_JSON_VALUE)
    public void getEvents(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }

    @RequestMapping(value = "/deletePost", method = GET, produces = APPLICATION_JSON_VALUE)
    public void deletePost(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }

    @RequestMapping(value = "/sortByDsitance", method = GET, produces = APPLICATION_JSON_VALUE)
    public void sortByDistance(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }

    @RequestMapping(value = "/sortByVotes", method = GET, produces = APPLICATION_JSON_VALUE)
    public void sortByVotes(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }

    @RequestMapping(value = "/sortByTime", method = GET, produces = APPLICATION_JSON_VALUE)
    public void sortByTime(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }

}
