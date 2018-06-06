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

    @JsonProperty("responses")
    private Map<String, Object> dataMap;
    /**
     * Response data constructor
     */
    public ResponseData() {
        dataMap = new HashMap<String, Object>();
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





}
