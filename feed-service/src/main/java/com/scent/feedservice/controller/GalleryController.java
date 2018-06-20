package com.scent.feedservice.controller;

import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/gallery")
public class GalleryController {

    @RequestMapping(value = "/uploadPhoto", method = GET, produces = APPLICATION_JSON_VALUE)
    public void uploadPhotos(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }

    @RequestMapping(value = "/uploadVideos", method = GET, produces = APPLICATION_JSON_VALUE)
    public void uploadVideos(@RequestParam Map<String, String> queryParams) {
        queryParams.put("", "");
    }
}
