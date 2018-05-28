package com.scent.feedservices.controller;

import com.scent.feedservices.data.RequestData;
import com.scent.feedservices.data.ResponseData;
import org.reactivestreams.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    @Autowired
    protected HttpServletRequest httpServletRequest;

    private final String USER_IP = "userIP";
    public RequestData getRequestData(HttpServletRequest request, Map<String, String> queryParams){
        RequestData requestData = new RequestData();
        requestData.setDataMap(queryParams);

        Map<String, String> cookiesMap = new HashMap<>();
        if (request.getCookies() != null) {
            Arrays.stream(request.getCookies())
                    .forEach(c -> cookiesMap.put(c.getName(), c.getValue()));

            setParamFromCookie(requestData, cookiesMap);
        }
        requestData.setParam(USER_IP, request.getRemoteAddr());
        return requestData;

    }

    public ResponseData getReponseData(Map<String, String> queryParams){
        RequestData requestData = this.getRequestData(httpServletRequest, queryParams);


    }

    public static String getServerDomain(HttpServletRequest request) {
        String serverDomain = "";

        if (request != null && StringUtils.hasText(request.getRequestURL())) {
           // serverDomain = StringUtils.substringBefore(request.getRequestURL().toString(), request.getContextPath());
        }

        return serverDomain;
    }

    private void setParamFromCookie(RequestData requestData, Map<String, String> loginCookies) {
        loginCookies.entrySet().stream()
                .filter(c -> requestData.containsKey(c.getKey()))
                .forEach(c -> requestData.setParam(c.getKey(), c.getValue()));
    }
}
