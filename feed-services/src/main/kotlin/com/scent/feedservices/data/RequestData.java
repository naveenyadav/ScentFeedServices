package com.scent.feedservices.data;

import java.util.HashMap;
import java.util.Map;

public class RequestData {

    private String can;
    private Map<String, String> dataMap;

    public RequestData(){
        dataMap = new HashMap<>();
    }

    public Map<String, String> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, String> dataMap) {
        this.dataMap = dataMap;
    }
    public void setParam(String name, String value) {
        dataMap.put(name, value);
    }

    public String getParam(String name) {
        return dataMap.get(name);
    }

    public boolean containsKey(String key){
        return dataMap.containsKey(key);
    }

    public String getCan() {
        return can;
    }

    public void setCan(String can) {
        this.can = can;
    }

    @Override
    public String toString() {
        return "RequestData{" +
                "can='" + can + '\'' +
                ", dataMap=" + dataMap +
                '}';
    }
}
