package com.csws.mymaps.model.flows;

import com.csws.mymaps.model.locations.MarkerConfig;
import com.csws.mymaps.model.locations.PolygonConfig;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

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
