package com.csws.mymaps.features.map.flows;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.core.flow.ActionFlow;
import com.csws.mymaps.core.flow.interfaces.ActivityActions;
import com.csws.mymaps.core.flow.interfaces.MapActions;
import com.csws.mymaps.features.map.viewmodels.DefaultFlowViewModel;
import com.google.android.gms.maps.model.LatLng;

public class DefaultFlow implements ActionFlow {
    private final DefaultFlowViewModel viewModel;

    private final ActivityActions actions;
    private final MapActions mapActions;

    public DefaultFlow(DefaultFlowViewModel viewModel, ActivityActions actions, MapActions mapActions) {
        this.viewModel = viewModel;
        this.actions = actions;
        this.mapActions = mapActions;
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
            actions.startCreateTaskFlow();
        }
    }

    @Override
    public void onLocationSelected(LocationItem location) {
        //TODO: ReImplement DisplayLocationDetails
        mapActions.focusLocation(location);
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
