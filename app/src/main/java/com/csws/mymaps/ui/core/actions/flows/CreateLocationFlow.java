package com.csws.mymaps.ui.core.actions.flows;

import com.csws.mymaps.R;
import com.csws.mymaps.model.flows.CreateLocationState;
import com.csws.mymaps.model.locations.LocationItem;
import com.csws.mymaps.model.locations.MarkerConfig;
import com.csws.mymaps.model.locations.PolygonConfig;
import com.csws.mymaps.ui.core.actions.ActionFlow;
import com.csws.mymaps.ui.map.MapActions;
import com.csws.mymaps.ui.map.MapController;
import com.csws.mymaps.ui.map.ActivityActions;
import com.csws.mymaps.viewmodel.flows.CreateLocationViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

import java.util.UUID;

public class CreateLocationFlow implements ActionFlow, MapController.MapCallbacks {

    private final CreateLocationViewModel viewModel;

    private final ActivityActions activityActions;
    private final MapActions mapActions;

    public CreateLocationFlow(CreateLocationViewModel viewModel, ActivityActions activityActions, MapActions mapActions) {
        this.viewModel = viewModel;
        this.activityActions = activityActions;
        this.mapActions = mapActions;
    }

    // --- MapCallbacks ---
    @Override
    public void onMapClicked(LatLng latLng) {
        CreateLocationState state = viewModel.getCurrent();
        state.polygonPoints.add(latLng);

        mapActions.renderTempPolygon(state.polygonPoints);
    }
    @Override
    public void onLocationSelected(LocationItem location) {

    }

    // --- Action Flow ---
    @Override
    public void start() {
        activityActions.openPlaceSearch();
    }
    @Override
    public void onPlaceSelected(String name, double lat, double lng) {
        // Save to state
        CreateLocationState state = viewModel.getCurrent();
        state.name = name;
        state.latLng = new LatLng(lat, lng);
        viewModel.update(state);

        // Tell map to show temp marker
        mapActions.renderTempLocation(state.latLng);

        // Enable drawing mode
        mapActions.setCallbacksListener(this);
    }
    @Override
    public void onCancel(){

    }
    // --- UI Actions ---
    @Override
    public void onAction(int action) {
        //Polygon Actions
        if (action == R.id.fab_confirm_polygon) {
            CreateLocationState currentState = viewModel.getCurrent();
            if (currentState.polygonPoints.size() < 3) return;

            PolygonConfig polygon = new PolygonConfig(210f, currentState.polygonPoints);

            LocationItem item = new LocationItem(
                    UUID.randomUUID().toString(),
                    "New Location",
                    currentState.polygonPoints.get(0).latitude,
                    currentState.polygonPoints.get(0).longitude,
                    polygon,
                    new MarkerConfig(0f,"default")
            );

            activityActions.createNewLocation(item);
        }

        else if (action == R.id.fab_undo_polygon) {
            CreateLocationState currentState = viewModel.getCurrent();
            if (!currentState.polygonPoints.isEmpty()) {
                currentState.polygonPoints.remove(currentState.polygonPoints.size() - 1);
                mapActions.renderTempPolygon(currentState.polygonPoints);
            }
        }

        else if (action == R.id.fab_cancel_polygon) {
            mapActions.clearTemp();
            activityActions.cancelCurrentFlow();
        }
    }
}
