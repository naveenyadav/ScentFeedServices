package com.scent.feedservice.controller;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.ResponseData;
import com.scent.feedservice.steps.profile.GetAccountStep;
import com.scent.feedservice.steps.profile.SignUp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

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
    @Autowired
    private GetAccountStep getAccountStep;
    @RequestMapping(value = "/login", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void authLogin(@RequestBody Map<String, String> queryParams) {
        queryParams.put("", "");
    }


    @RequestMapping(value = "/logout", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public void authLogout(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }

    @RequestMapping(value = "/signUp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ServerResponse> authSignUp(@RequestBody Map<String, String> queryParams) {
        EventData eventData = gerEventData(queryParams);
        return getAccountStep.executeStep(eventData);


    }

    @RequestMapping(value = "/forgotPassword", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public void forgotPassword(@RequestBody Map<String, String> queryParams) {
        queryParams.put("", "");
    }
}
