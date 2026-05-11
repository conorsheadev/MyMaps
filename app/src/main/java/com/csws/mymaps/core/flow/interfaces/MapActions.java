package com.csws.mymaps.core.flow.interfaces;

import com.csws.mymaps.domain.locations.LocationItem;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface MapActions {
    void setMapClicksEnabled(boolean enabled);
    void previewLocation(LocationItem location);
    void focusLocation(LocationItem location);
    void setMapGesturesEnabled(boolean enabled);
    void renderTempPolygon(List<LatLng> points);
    void renderTempLocation(LatLng latLng);
    void clearTemp();
}
