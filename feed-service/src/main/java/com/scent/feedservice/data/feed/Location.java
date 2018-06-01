package com.scent.feedservice.data.feed;

import com.scent.feedservice.data.feed.Coordinate;
import org.json.JSONObject;

import static com.scent.feedservice.Util.Constants.*;


public class Location {
    private String type;
    private Long[] coordinates;
    private String name;
    public Location(){

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
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
