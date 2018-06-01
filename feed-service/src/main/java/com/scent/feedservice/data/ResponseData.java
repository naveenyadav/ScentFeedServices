package com.scent.feedservice.data;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.reactivestreams.Publisher;

import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import static com.scent.feedservice.Util.Constants.JSON_RESPONSE_FILTER;

@JsonFilter(JSON_RESPONSE_FILTER)
public class ResponseData {

    /** The total time. */
    @JsonProperty("totalResponseTime")
    private long totalTime;

    /** The data map. */
    @JsonProperty("services")
    private Map<String, Publisher<T>> dataMap;

    /**
     * Response data constructor.
     */
    public ResponseData() {
        dataMap = new HashMap<>();
    }

    public void setDataMap(Map<String, Publisher<T>> dataMap) {
        this.dataMap = dataMap;
    }

    /**
     * This method is used to get response data.
     *
     * @param name
     *            String parameter to get data
     * @return Object Returns data object
     */
    public Object getData(String name) {
        return dataMap.get(name);
    }

    /**
     * This method is used to set response data.
     *
     * @param name
     *            Key to be set in data map.
     * @param value
     *            Value to be set in data map.
     */
    public void setData(String name, Object value) {
        dataMap.put(name, value);
    }

    /**
     * Gets the data map.
     *
     * @return the data map
     */
    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    /**
     * Sets the data map.
     *
     * @param dataMap
     *            the data map
     */
    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }

    /**
     * Gets the total time.
     *
     * @return the total time
     */
    public long getTotalTime() {
        return totalTime;
    }

    /**
     * Sets the total time.
     *
     * @param totalTime
     *            the new total time
     */
    public void setTotalTime(long totalTime) {
        this.totalTime = totalTime;
    }

    /**
     * returns String representation of ResponseData.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "ResponseData [totalTime=" + totalTime + ", dataMap=" + dataMap + "]";
    }





}
