package com.scent.feedservices.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * This controller class file is used to handle following:
 * Purpose:
 * Methods:
 *
 * @author nyadav
 */
@RestController
@RequestMapping("/like")
public class LikeController {

    /**
     * This GET controller method is used to handle following.
     * Purpose:
     *
     * @param queryParams map of all request parameter name and its value
     * @return
     */
    @RequestMapping(value = "/getLikes", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void getLikes(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }

    /**
     * This POST controller method is used to handle following.
     * Purpose:
     *
     * @param queryParams map of all request parameter name and its value
     * @return
     */
    @RequestMapping(value = "/postLikes", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void postLikes(@RequestBody Map<String, String> queryParams) {
        queryParams.put("", "");
    }
}
