package com.csws.mymaps.model.locations;

public class LocationItem {
    public String id;
    public String name;
    public String type;
    public double lat;
    public double lng;

    public PolygonConfig polygonConfig;
    public MarkerConfig markerConfig;


    public LocationItem(String id, String name, String type, double lat, double lng, PolygonConfig polygonConfig, MarkerConfig markerConfig) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.lat = lat;
        this.lng = lng;
        this.polygonConfig = polygonConfig;
        this.markerConfig = markerConfig;
    }

}