package com.csws.mymaps.ui.core.actionflows;

import com.csws.mymaps.model.locations.LocationItem;
import com.google.android.gms.maps.model.LatLng;

public interface ActionFlow {
    void start();
    void onAction(int action);
    void onMapClicked(LatLng latLng);
    void onLocationSelected(LocationItem location);
    void onCancel();
}
