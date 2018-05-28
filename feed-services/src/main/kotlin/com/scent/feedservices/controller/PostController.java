package com.scent.feedservices.controller;


import com.scent.feedservices.Util.Constants;

import org.springframework.web.bind.annotation.*;


import java.util.Map;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * This controller class file is used to handle following:
 * Purpose:
 * Methods:
 *
 * @author nyadav
 */
@RestController
@RequestMapping("/posts")
public class PostController {



    /**
     * This GET controller method is used to handle following.
     * Purpose:
     *
     * @param queryParams map of all request parameter name and its value
     * @return
     */
    @GetMapping(value = "/getPost/{ID}",  produces = APPLICATION_JSON_VALUE)
    public void getPosts(@RequestParam Map<String, String> queryParams) {


    }



    /**
     * This POST controller method is used to handle following.
     * Purpose:
     *
     * @param queryParams map of all request parameter name and its value
     * @return
     */
    @RequestMapping(value = "/createPost", method = POST, produces = APPLICATION_JSON_VALUE)
    public void createPost(@RequestBody Map<String, String> queryParams) {
        queryParams.put("", "");
    }


}
