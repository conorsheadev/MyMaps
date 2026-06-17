package com.csws.mymaps.coordinators.workflows.workflows;

import com.csws.mymaps.core.models.locations.LocationItem;
import com.google.android.gms.maps.model.LatLng;

/**
 * Represents a user workflow that can temporarily take control
 * of application input and guide the user through a series of steps.
 * TODO: Move onMapClicked() onLocationSelected() and onRecenterClicked() to a new optional interface e.g. MapWorkflow
 */
public interface Workflow {
    void start();
    void stop();
    void onAction(int action);
    void onMapClicked(LatLng latLng);
    void onLocationSelected(LocationItem location);
    void onRecenterClicked();
    boolean blocksUserInput();
}
