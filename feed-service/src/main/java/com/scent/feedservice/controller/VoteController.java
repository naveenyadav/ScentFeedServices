package com.scent.feedservice.controller;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.steps.likes.UpVoteStep;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static com.scent.feedservice.Util.Constants.DOWN_VOTE;
import static com.scent.feedservice.Util.Constants.UP_VOTE;
import static com.scent.feedservice.Util.Constants.VOTE;

/**
 * This controller class file is used to handle following:
 * Purpose:
 * Methods:
 *
 * @author nyadav
 */
@RestController
@RequestMapping("/like")
public class VoteController extends BaseController {

    private UpVoteStep upVoteAction;
    public VoteController(UpVoteStep upVoteAction){
        this.upVoteAction = upVoteAction;
    }
    /**
     * This GET controller method is used to handle following.
     * Purpose:
     *Required : USER_ID,
     * @param queryParams map of all request parameter name and its value
     * @return
     */
    @RequestMapping(value = "/upVote", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String upVote(@RequestParam Map<String, String> queryParams) {
        queryParams.put(VOTE, UP_VOTE);
        EventData eventData = gerEventData(queryParams);
        upVoteAction.perFormAction(eventData);
        return "Success";

    }

    /**
     * This GET controller method is used to handle following.
     * Purpose:
     *
     * @param queryParams map of all request parameter name and its value
     * @return
     */
    @RequestMapping(value = "/downVote", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String downVote(@RequestParam Map<String, String> queryParams) {
        queryParams.put(VOTE, DOWN_VOTE);
        EventData eventData = gerEventData(queryParams);
        return "Success";

    }

}
