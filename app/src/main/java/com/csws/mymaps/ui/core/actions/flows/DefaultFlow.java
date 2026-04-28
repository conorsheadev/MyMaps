package com.csws.mymaps.ui.core.actions.flows;

import com.csws.mymaps.R;
import com.csws.mymaps.model.locations.LocationItem;
import com.csws.mymaps.ui.core.actions.ActionFlow;
import com.csws.mymaps.ui.map.ActivityActions;
import com.csws.mymaps.ui.map.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class DefaultFlow implements ActionFlow {
    private final ActivityActions actions;

    public DefaultFlow(ActivityActions actions) {
        this.actions = actions;
    }

    @Override
    public void start(){
        actions.setFabMenu(R.menu.fab_defaultactions_menu);
    }

    @Override
    public void onAction(int actionId) {
        if (actionId == R.id.fab_add_location) {
            actions.startCreateLocationFlow();
        }

        if (actionId == R.id.fab_add_task) {
            // handle
        }
    }

    @Override
    public void onLocationSelected(LocationItem location) {
        //TODO: ReImplement DisplayLocationDetails
        actions.setFabMenu(R.menu.fab_locationactions_menu);
    }

    @Override
    public void onMapClicked(LatLng latLng) {
        actions.setFabMenu(R.menu.fab_defaultactions_menu);
    }

    @Override
    public void onCancel() {
        //TODO: ReImplement Cancel
    }
}
