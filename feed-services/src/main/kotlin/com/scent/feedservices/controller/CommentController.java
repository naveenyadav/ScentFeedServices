package com.scent.feedservices.controller;

import com.scent.feedservices.EventHandler;
import com.scent.feedservices.data.EventData;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Map;

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
    /**
     * This GET controller method is used to handle following.
     * Purpose:
     *
     * @param queryParams map of all request parameter name and its value
     * @return
     */
    @RequestMapping(value = "/getComments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void getComments(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");

    }

    public static void main(String[] args) {
        EventHandler<Integer> ss = new EventHandler<Integer>();
        Flux<Integer> ints = Flux.range(1, 4);
        ints.subscribe(ss);
    }


    /**
     * This POST controller method is used to handle following.
     * Purpose:
     *
     * @param queryParams map of all request parameter name and its value
     * @return
     */
    @RequestMapping(value = "/postComment", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void postComment(@RequestBody Map<String, String> queryParams) {
        queryParams.put("", "");
    }
}
