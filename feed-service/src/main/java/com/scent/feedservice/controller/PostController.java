package com.scent.feedservice.controller;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.data.feed.Post;
import com.scent.feedservice.steps.poststeps.CreatePostStep;
import com.scent.feedservice.steps.poststeps.ListPostStep;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

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
    private ListPostStep listPostStep;
    @Autowired
    private CreatePostStep createPostStep;

    /**
     * This GET controller method is used to handle following.
     * Purpose: This method creates posts.
     *
     * @param queryParams map of all request parameter name and its value
     * @return
     */
    @RequestMapping(value = "/createPost", method = POST, produces = APPLICATION_JSON_VALUE)
    public String createPost(@RequestBody Map<String, String> queryParams) {
        EventData eventData = gerEventData(queryParams);
        createPostStep.perFormAction(eventData);
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
    public Flux<Post> getPosts(@RequestParam Map<String, String> queryParams) {
        EventData eventData = gerEventData(queryParams);
        return listPostStep.getPosts(eventData);

    }

    @RequestMapping(value = "/changePrivacy", method = GET, produces = APPLICATION_JSON_VALUE)
    public void getEvents(@RequestParam Map<String, String> queryParams) {
        EventData eventData = gerEventData(queryParams);
    }

    @RequestMapping(value = "/deletePost", method = GET, produces = APPLICATION_JSON_VALUE)
    public void deletePost(@RequestParam Map<String, String> queryParams) {
        EventData eventData = gerEventData(queryParams);
    }

    @RequestMapping(value = "/hideLocation", method = GET, produces = APPLICATION_JSON_VALUE)
    public void hideLocation(@RequestParam Map<String, String> queryParams) {
        EventData eventData = gerEventData(queryParams);
    }



    @RequestMapping(value = "/sortByDsitance", method = GET, produces = APPLICATION_JSON_VALUE)
    public void sortByDistance(@RequestParam Map<String, String> queryParams) {
        EventData eventData = gerEventData(queryParams);
    }

    @RequestMapping(value = "/sortByVotes", method = GET, produces = APPLICATION_JSON_VALUE)
    public void sortByVotes(@RequestParam Map<String, String> queryParams) {
        EventData eventData = gerEventData(queryParams);
    }

    @RequestMapping(value = "/sortByTime", method = GET, produces = APPLICATION_JSON_VALUE)
    public void sortByTime(@RequestParam Map<String, String> queryParams) {
        EventData eventData = gerEventData(queryParams);
    }

}
