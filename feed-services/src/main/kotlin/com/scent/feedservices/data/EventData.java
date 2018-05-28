package com.scent.feedservices.data;


import java.util.concurrent.CountDownLatch;

public class EventData {
    private CountDownLatch latch;
    private String id;
    private RequestData requestData;
    private ResponseData responseData;

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RequestData getRequestData() {
        return requestData;
    }

    public void setRequestData(RequestData requestData) {
        this.requestData = requestData;
    }

    public ResponseData getResponseData() {
        return responseData;
    }

    public void setResponseData(ResponseData responseData) {
        this.responseData = responseData;
    }

    @Override
    public String toString() {
        return "EventData[" +
                "latch=" + latch +
                ", id='" + id + '\'' +
                ", requestData=" + requestData +
                ", responseData=" + responseData +
                ']';
    }
}
