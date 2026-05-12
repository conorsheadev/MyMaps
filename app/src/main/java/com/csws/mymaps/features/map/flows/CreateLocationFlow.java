package com.csws.mymaps.features.map.flows;

import android.util.Log;

import com.csws.mymaps.R;
import com.csws.mymaps.core.flow.interfaces.FlowActions;
import com.csws.mymaps.core.flow.interfaces.SessionActions;
import com.csws.mymaps.domain.flows.CreateLocationState;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.locations.MarkerConfig;
import com.csws.mymaps.domain.locations.PolygonConfig;
import com.csws.mymaps.core.flow.ActionFlow;
import com.csws.mymaps.features.map.controllers.ui.bottom_sheets.LocationConfigFragment;
import com.csws.mymaps.core.flow.interfaces.MapActions;
import com.csws.mymaps.core.flow.interfaces.ActivityActions;
import com.csws.mymaps.features.map.controllers.ui.placesearch.PlaceSearchFragment;
import com.csws.mymaps.features.map.viewmodels.CreateLocationViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class CreateLocationFlow implements ActionFlow, PlaceSearchFragment.PlaceSelectionListener {

    private final CreateLocationViewModel viewModel;

    private final ActivityActions activityActions;
    private final SessionActions sessionActions;
    private final FlowActions flowActions;
    private final MapActions mapActions;

    public CreateLocationFlow(CreateLocationViewModel viewModel, ActivityActions activityActions, SessionActions sessionActions, FlowActions flowActions, MapActions mapActions) {
        this.viewModel = viewModel;
        this.activityActions = activityActions;
        this.sessionActions = sessionActions;
        this.flowActions = flowActions;
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

    @Override
    public void onRecenterClicked() {

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
        flowActions.cancelCurrentFlow();
    }
    // --- Polygon Actions ---
    public void onConfirmPolygon() {

        mapActions.setMapGesturesEnabled(false);

        CreateLocationState state = viewModel.getCurrent();

        LocationConfigFragment fragment = LocationConfigFragment.newInstance(state.name, state.type, state.markerConfig, state.polygonConfig);

        fragment.setListener(this::onConfirmLocation);

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
        flowActions.cancelCurrentFlow();
    }

    // --- Bottom Sheet Fragment ---
    public void onConfirmLocation(String name, String type, MarkerConfig markerConfig, PolygonConfig polygonConfig) {
        CreateLocationState state = viewModel.getCurrent();

        polygonConfig.points = state.polygonPoints;

        LocationItem item = new LocationItem(
                UUID.randomUUID().toString(),
                name,
                type,
                state.latLng.latitude,
                state.latLng.longitude,
                polygonConfig,
                markerConfig
        );

        sessionActions.createNewLocation(item);
        mapActions.setMapGesturesEnabled(true);
        activityActions.hideBottomSheet();
        flowActions.cancelCurrentFlow();
    }
}
