package com.scent.feedservice.data.feed;


import org.json.JSONObject;

import static com.scent.feedservice.Util.Constants.*;


public class Location {
    private String type;
    private Double[] coordinates;
    private String name;
    public Location(){
        coordinates = new Double[2];
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Double[] coordinates) {
        setLongitude(coordinates[0]);
        setLatitude(coordinates[1]);
    }

    public void setLongitude(Double longitude) {
        this.coordinates[0] = longitude;
    }

    public void setLatitude(Double latitude) {
        this.coordinates[1] = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONObject getLocationJson(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(TYPE, type);
        jsonObject.put(LOCATION_NAME, name);
        return jsonObject;
    }


}
