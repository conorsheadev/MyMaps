package com.csws.mymaps.ui.core.actions.flows;

import android.util.Log;

import com.csws.mymaps.R;
import com.csws.mymaps.model.flows.CreateLocationState;
import com.csws.mymaps.model.locations.LocationItem;
import com.csws.mymaps.model.locations.MarkerConfig;
import com.csws.mymaps.model.locations.PolygonConfig;
import com.csws.mymaps.ui.core.actions.ActionFlow;
import com.csws.mymaps.ui.map.LocationConfigFragment;
import com.csws.mymaps.ui.map.MapActions;
import com.csws.mymaps.ui.map.MapFabController;
import com.csws.mymaps.ui.map.MapFragment;
import com.csws.mymaps.ui.map.ActivityActions;
import com.csws.mymaps.ui.places.PlaceSearchFragment;
import com.csws.mymaps.viewmodel.flows.CreateLocationViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class CreateLocationFlow implements ActionFlow, PlaceSearchFragment.PlaceSelectionListener {

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
        activityActions.openPlaceSearch(this);
    }

    @Override
    public void onCancel(){

    }

    @Override
    public void onAction(int action) {
        //Polygon Actions
        if (action == R.id.fab_confirm_polygon) {
            onConfirmPolygon();
        }

        else if (action == R.id.fab_undo_polygon) {
            onUndoPolygon();
        }

        else if (action == R.id.fab_cancel_polygon) {
            onCancelPolygon();
        }
    }
    // --- PlaceSearchCallbacks ---
    @Override
    public void onPlaceSelected(String name, double lat, double lng) {
        // Save to state
        CreateLocationState state = viewModel.getCurrent();
        state.name = name;
        state.latLng = new LatLng(lat, lng);
        viewModel.update(state);

        // Display TempMarker and set callbacks
        mapActions.renderTempLocation(state.latLng);

        // Set Fab Menu
        activityActions.setFabMenu(R.menu.fab_polyeditactions_menu);
        Log.d("CreateLocationFlow", "onPlaceSelected: " + name + " (" + lat + ", " + lng + ")");
    }
    @Override
    public void onSearchCancelled() {
        //TODO: Instead of cancelling flow when search is cancelled provide users alternative ways before cancelling
        activityActions.cancelCurrentFlow();
    }
    // --- Polygon Actions ---
    public void onConfirmPolygon() {

        mapActions.setMapGesturesEnabled(false);

        CreateLocationState state = viewModel.getCurrent();

        LocationConfigFragment fragment = LocationConfigFragment.newInstance(state.name, state.type, state.markerConfig, state.polygonConfig);

        fragment.setListener((name,type,markerConfig,polygonConfig) -> {

            LocationItem item = new LocationItem(
                    UUID.randomUUID().toString(),
                    name,
                    type,
                    state.latLng.latitude,
                    state.latLng.longitude,
                    polygonConfig,
                    markerConfig
            );

            activityActions.createNewLocation(item);
            mapActions.setMapGesturesEnabled(true);
            activityActions.hideBottomSheet();
            activityActions.cancelCurrentFlow();
        });

        activityActions.showBottomSheet(fragment);
    }

    public void onUndoPolygon() {
        CreateLocationState currentState = viewModel.getCurrent();
        if (!currentState.polygonPoints.isEmpty()) {
            currentState.polygonPoints.remove(currentState.polygonPoints.size() - 1);
            mapActions.renderTempPolygon(currentState.polygonPoints);
        }
    }

    public void onCancelPolygon() {
        mapActions.clearTemp();
        activityActions.cancelCurrentFlow();
    }
}
