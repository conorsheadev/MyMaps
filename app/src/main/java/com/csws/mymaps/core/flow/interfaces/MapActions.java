package com.csws.mymaps.core.flow.interfaces;

import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.features.map.controllers.map.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface MapActions {
    //UI Controls
    void setListener(MapFragment.MapCallbacks listener);
    void setMapClicksEnabled(boolean enabled);
    void setMapGesturesEnabled(boolean enabled);
    boolean isCenteredOnUser();
    //UI Actions
    void previewLocation(LocationItem location);
    void focusLocation(LocationItem location);
    void moveToUserLocation();
    //Drawing
    void renderTempPolygon(List<LatLng> points);
    void renderTempLocation(LatLng latLng);
    void clearTemp();
}
