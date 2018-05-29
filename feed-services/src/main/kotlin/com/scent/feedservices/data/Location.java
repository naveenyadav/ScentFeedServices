package com.scent.feedservices.data;

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

    private void setLongitude(Long longitude) {
        this.coordinates[0] = longitude;
    }
    private void setLatitude(Long latitude) {
        this.coordinates[1] = latitude;
    }
}
