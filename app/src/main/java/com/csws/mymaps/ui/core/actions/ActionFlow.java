package com.csws.mymaps.ui.core.actions;

import com.google.android.gms.maps.model.LatLng;

public interface ActionFlow {
    void start();
    void onAction(int action);

}
