package com.csws.mymaps.ui.map;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface MapActions {
    void renderTempPolygon(List<LatLng> points);
    void renderTempLocation(LatLng latLng);
    void clearTemp();
}
