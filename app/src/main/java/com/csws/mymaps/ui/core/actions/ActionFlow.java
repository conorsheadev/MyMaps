package com.csws.mymaps.ui.core.actions;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

public interface ActionFlow {
    void start();
    void onPlaceSelected(String name, double lat, double lng);
    void onAction(int action);
    void onCancel();
}
