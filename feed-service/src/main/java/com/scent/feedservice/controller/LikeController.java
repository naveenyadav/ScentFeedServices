package com.scent.feedservice.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

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
@RequestMapping("/like")

public class LikeController extends BaseController{

    @RequestMapping(value = "/upVote", method = GET, produces = APPLICATION_JSON_VALUE)
    public void upVotes(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }

    @RequestMapping(value = "/downVote", method = GET, produces = APPLICATION_JSON_VALUE)
    public void downVotes(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }
}
