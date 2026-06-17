package com.csws.mymaps.core.contracts.map;

import com.csws.mymaps.core.models.locations.LocationItem;
import com.csws.mymaps.core.models.navigation.NavigationRoute;
import com.csws.mymaps.coordinators.map.controllers.map.MapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Abstraction layer used by workflows and managers to interact
 * with the map without directly depending on MapFragment.
 */
public interface MapController {
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
    void showNavigation(NavigationRoute route);
    //Queries
    LatLng getUserLocation();
}
