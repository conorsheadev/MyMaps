package com.csws.mymaps.data;

public class LocationItem {
    public String id;
    public String name;
    public double lat;
    public double lng;
    public String type; // "building", "poi"

    public LocationItem(String id, String name, double lat, double lng, String type) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.type = type;
    }
}
