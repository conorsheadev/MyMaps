package com.csws.mymaps.model.flows;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.List;

public class CreateLocationState {
    public String name;
    public LatLng latLng;
    public Place place;
    public List<LatLng> polygonPoints = new ArrayList<>();
}
