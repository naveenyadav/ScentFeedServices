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
@RequestMapping("/user")
public class UserController extends BaseController {

    @RequestMapping(value = "/closeAccount", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void closeAccount(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }


    @RequestMapping(value = "/getProfile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void getProfile(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }

    @RequestMapping(value = "/changePassword", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void changePassword(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }

    @RequestMapping(value = "/updateProfile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void updateProfile(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }

}
