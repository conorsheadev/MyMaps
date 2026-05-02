package com.csws.mymaps.domain.flows;

import com.csws.mymaps.domain.locations.MarkerConfig;
import com.csws.mymaps.domain.locations.PolygonConfig;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class CreateLocationState {
    public String name;
    public String type;
    public LatLng latLng;
    public List<LatLng> polygonPoints = new ArrayList<>();

    public MarkerConfig markerConfig;
    public PolygonConfig polygonConfig;
}
