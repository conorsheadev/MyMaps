package com.csws.mymaps.coordinators.workflows.workflows;

import com.csws.mymaps.core.models.locations.LocationItem;
import com.csws.mymaps.coordinators.map.MapViewContext;
import com.google.android.gms.maps.model.LatLng;

public abstract class BaseWorkflow implements Workflow {

    protected final MapViewContext context;

    public BaseWorkflow(MapViewContext context) {
        this.context = context;
    }

    @Override
    public void start() {}

    @Override
    public void stop() {}

    @Override
    public void onAction(int actionId) {}

    @Override
    public void onMapClicked(LatLng latLng) {}

    @Override
    public void onLocationSelected(LocationItem location) {}

    @Override
    public void onRecenterClicked() {}

    @Override
    public boolean blocksUserInput() {
        return false;
    }

    protected void resetUI() {

        context.uiCoordinator.hideBottomSheet();
        context.uiCoordinator.hideTopSheet();
        context.uiCoordinator.clearTempMapObjects();
    }

    protected void resetToDefaultFab() {

        context.uiCoordinator.showDefaultFabMenu();
    }
}
