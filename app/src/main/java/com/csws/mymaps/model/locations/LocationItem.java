package com.csws.mymaps.model.locations;

public class LocationItem {
    public String id;
    public String name;
    public double lat;
    public double lng;

    public PolygonConfig polygonConfig;
    public MarkerConfig markerConfig;


    public LocationItem(String id, String name, double lat, double lng, PolygonConfig polygonConfig, MarkerConfig markerConfig) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lng = lng;
        this.polygonConfig = polygonConfig;
        this.markerConfig = markerConfig;
    }

}