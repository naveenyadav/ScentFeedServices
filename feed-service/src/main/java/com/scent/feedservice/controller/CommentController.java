package com.scent.feedservice.controller;

import com.scent.feedservice.data.EventData;
import com.scent.feedservice.data.RequestData;
import com.scent.feedservice.repositories.CreatePostHandler;
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

    private CreatePostHandler postRepositoryImpl;
    public CommentController(CreatePostHandler postRepositoryImpl){
        this.postRepositoryImpl = postRepositoryImpl;
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
        queryParams.put("", "");
        queryParams.put(CONTENT, "content");
        queryParams.put(DATE, "2018-05-30T23:35:22.346Z");
        queryParams.put(TIMEZONE, "IST");
        queryParams.put(LOCATION_NAME, "Taj Mahal");
        queryParams.put(POST_TYPE, "IMAGE");
        queryParams.put(USER_ID, "1");
        EventData eventData = new EventData();

        RequestData requestData = getRequestData(queryParams);

        eventData.setRequestData(requestData);
        postRepositoryImpl.handleEvent(eventData);

        return "Success";

    }

    protected RequestData getRequestData(Map<String, String> queryParams) {
        RequestData requestData = new RequestData();
        requestData.setDataMap(queryParams);
        return requestData;
    }
}
