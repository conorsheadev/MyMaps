package com.csws.mymaps.features.map.coordinators.flows;

import com.csws.mymaps.R;
import com.csws.mymaps.core.flow.ActionFlow;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.features.map.coordinators.FlowContext;
import com.google.android.gms.maps.model.LatLng;

public abstract class BaseFlow implements ActionFlow {

    protected final FlowContext context;

    public BaseFlow(FlowContext context) {
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
        context.bottomSheetController.hide();
        context.topSheetController.hide();
        context.mapActions.clearTemp();
    }

    protected void resetToDefaultFab() {
        context.fabController.setMenu(R.menu.fab_defaultactions_menu);
    }
}
