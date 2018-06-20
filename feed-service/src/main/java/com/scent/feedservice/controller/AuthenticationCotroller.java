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
@RequestMapping("/auth")
public class AuthenticationCotroller extends BaseController {

    @RequestMapping(value = "/login", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void authLogin(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }


    @RequestMapping(value = "/logout", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void authLogout(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void authSignUp(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }

    @RequestMapping(value = "/forgotPassword", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void forgotPassword(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }
}
