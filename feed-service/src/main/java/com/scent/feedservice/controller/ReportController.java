package com.scent.feedservice.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * This controller class file is used to handle following:
 * Purpose:
 * Methods:
 *
 * @author nyadav
 */
@RestController
@RequestMapping("/report")
public class ReportController extends BaseController{

    @RequestMapping(value = "/reportUser", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void reportUser(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }

    @RequestMapping(value = "/reportPost", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void reportPosts(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }
}
