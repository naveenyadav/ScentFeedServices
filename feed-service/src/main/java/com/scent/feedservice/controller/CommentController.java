package com.scent.feedservice.controller;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.steps.UpVoteAction;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.scent.feedservice.Util.Constants.*;

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

    private UpVoteAction upVoteAction;
    public CommentController(UpVoteAction upVoteAction){
        this.upVoteAction = upVoteAction;
    }
    /**
     * This GET controller method is used to handle following.
     * Purpose:
     *
     * @param queryParams map of all request parameter name and its value
     * @return
     */
    @RequestMapping(value = "/getComments", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getComments(@RequestParam Map<String, String> queryParams) {
        EventData eventData = new EventData();

        RequestData requestData = getRequestData(queryParams);

        eventData.setRequestData(requestData);
        upVoteAction.perFormAction(eventData);

        return "Success";

    }

    protected RequestData getRequestData(Map<String, String> queryParams) {
        RequestData requestData = new RequestData();
        requestData.setDataMap(queryParams);
        return requestData;
    }
}
