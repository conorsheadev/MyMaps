package com.csws.mymaps.core.flow;

import com.csws.mymaps.domain.locations.LocationItem;
import com.google.android.gms.maps.model.LatLng;

public interface Workflow {
    void start();
    void stop();
    void onAction(int action);
    void onMapClicked(LatLng latLng);
    void onLocationSelected(LocationItem location);
    void onRecenterClicked();
    boolean blocksUserInput();
}
