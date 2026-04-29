package com.csws.mymaps.ui.core.actionflows.interfaces;

import com.csws.mymaps.model.locations.LocationItem;
import com.csws.mymaps.ui.mapviewer.fragments.map.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface MapActions {
    void focusLocation(LocationItem location);
    void setMapGesturesEnabled(boolean enabled);
    void renderTempPolygon(List<LatLng> points);
    void renderTempLocation(LatLng latLng);
    void clearTemp();
}
