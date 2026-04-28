package com.csws.mymaps.ui.map;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface MapActions {
    void setCallbacksListener(MapFragment.MapCallbacks listener);
    void setMapGesturesEnabled(boolean enabled);
    void renderTempPolygon(List<LatLng> points);
    void renderTempLocation(LatLng latLng);
    void clearTemp();
}
