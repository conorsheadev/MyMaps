package com.csws.mymaps.features.map.workflow.workflows;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.flows.CreateLocationState;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.locations.MarkerConfig;
import com.csws.mymaps.domain.locations.PolygonConfig;
import com.csws.mymaps.features.map.controllers.ui.bottom_sheets.LocationConfigFragment;
import com.csws.mymaps.features.map.controllers.ui.top_sheets.PlaceSearchFragment;
import com.csws.mymaps.features.map.coordinators.FlowContext;
import com.csws.mymaps.features.map.viewmodels.CreateLocationViewModel;
import com.csws.mymaps.features.map.workflow.BaseWorkflow;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class CreateLocationWorkflow extends BaseWorkflow implements PlaceSearchFragment.PlaceSelectionListener {

    private final CreateLocationViewModel viewModel;

    public CreateLocationWorkflow(CreateLocationViewModel viewModel, FlowContext context) {
        super(context);
        this.viewModel = viewModel;
    }

    @Override
    public void start() {

        PlaceSearchFragment fragment = new PlaceSearchFragment();
        fragment.setListener(this);

        context.topSheetController.show(fragment);
    }

    @Override
    public void stop() {

        resetUI();

        context.mapActions.setMapGesturesEnabled(true);
    }

    @Override
    public void onMapClicked(LatLng latLng) {

        CreateLocationState state = viewModel.getCurrent();

        state.polygonPoints.add(latLng);

        context.mapActions.renderTempPolygon(state.polygonPoints);
    }

    @Override
    public void onAction(int action) {

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

    @Override
    public void onPlaceSelected(
            String name,
            double lat,
            double lng
    ) {

        CreateLocationState state = viewModel.getCurrent();

        state.name = name;
        state.latLng = new LatLng(lat, lng);

        viewModel.update(state);

        context.mapActions.renderTempLocation(state.latLng);
        context.fabController.setMenu(R.menu.fab_polyeditactions_menu);
    }

    @Override
    public void onSearchCancelled() {

        context.workflowNavigator.cancelCurrentFlow();
    }

    private void onConfirmPolygon() {

        context.mapActions.setMapGesturesEnabled(false);

        CreateLocationState state = viewModel.getCurrent();

        LocationConfigFragment fragment = LocationConfigFragment.newInstance(
                        state.name,
                        state.type,
                        state.markerConfig,
                        state.polygonConfig
                );

        fragment.setListener(this::onConfirmLocation);

        context.bottomSheetController.show(fragment);
    }

    private void onUndoPolygon() {

        CreateLocationState currentState = viewModel.getCurrent();

        if (!currentState.polygonPoints.isEmpty()) {

            currentState.polygonPoints.remove(currentState.polygonPoints.size() - 1);
            context.mapActions.renderTempPolygon(currentState.polygonPoints);
        }
    }

    private void onCancelPolygon() {

        context.workflowNavigator.cancelCurrentFlow();
    }

    private void onConfirmLocation(String name, String type, MarkerConfig markerConfig, PolygonConfig polygonConfig) {

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

        context.sessionActions.createNewLocation(item);

        context.workflowNavigator.cancelCurrentFlow();
    }
}