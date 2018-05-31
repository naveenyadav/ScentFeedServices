package com.scent.feedservice.data;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public class ResponseData {

    @JsonProperty("totalResponseTime")
    private long totalTime;

    @JsonProperty("services")
    private Map<String, Object> dataMap;

    public ResponseData() {
        dataMap = new HashMap<String, Object>();
    }

    public long getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    @Override
    public String toString() {
        return "ResponseData{" +
                "totalTime=" + totalTime +
                ", dataMap=" + dataMap +
                '}';
    }
}
