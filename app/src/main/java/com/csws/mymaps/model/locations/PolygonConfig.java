package com.csws.mymaps.model.locations;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public class PolygonConfig {
    public float colorHue;
    public List<LatLng> points;

    public PolygonConfig(float colorHue, List<LatLng> points){
        this.colorHue = colorHue;
        this.points = points;
    }
}
