package com.scent.feedservice.data;

import org.json.JSONObject;

import static com.scent.feedservice.Util.Constants.*;


public class Location {
    private String type;
    private Long[] coordinates;
    private String name;
    public Location(){
        coordinates = new Long[2];
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Long[] coordinates) {
        setLongitude(coordinates[0]);
        setLatitude(coordinates[1]);
    }

    public void setLongitude(Long longitude) {
        this.coordinates[0] = longitude;
    }
    public void setLatitude(Long latitude) {
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
        jsonObject.put(LONGITUDE, coordinates[0]);
        jsonObject.put(LATITUDE, coordinates[1]);
        jsonObject.put(LOCATION_NAME, name);
        return jsonObject;
    }
}
