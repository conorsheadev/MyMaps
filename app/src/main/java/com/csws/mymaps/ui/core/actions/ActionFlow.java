package com.csws.mymaps.ui.core.actions;

import com.csws.mymaps.model.locations.LocationItem;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

public interface ActionFlow {
    void start();
    void onAction(int action);
    void onMapClicked(LatLng latLng);
    void onLocationSelected(LocationItem location);
    void onCancel();
}
