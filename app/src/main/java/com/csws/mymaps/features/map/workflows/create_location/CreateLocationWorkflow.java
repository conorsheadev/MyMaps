package com.csws.mymaps.features.map.workflows.create_location;

import com.csws.mymaps.R;

import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.locations.MarkerConfig;
import com.csws.mymaps.domain.locations.PolygonConfig;
import com.csws.mymaps.core.ui.forms.LocationConfigFragment;
import com.csws.mymaps.core.ui.pickers.PlaceSearchFragment;
import com.csws.mymaps.features.map.coordinators.MapViewContext;
import com.csws.mymaps.features.map.workflows.BaseWorkflow;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.UUID;

public class CreateLocationWorkflow extends BaseWorkflow implements LocationConfigFragment.Listener, PlaceSearchFragment.PlaceSelectionListener {

    private final CreateLocationViewModel viewModel;

    public CreateLocationWorkflow(CreateLocationViewModel viewModel, MapViewContext context) {
        super(context);
        this.viewModel = viewModel;
    }

    @Override
    public void start() {

        viewModel.reset();

        LocationItem draft = new LocationItem(
                UUID.randomUUID().toString(),
                "",
                "",
                0,
                0,
                new PolygonConfig(0f, new ArrayList<>()),
                new MarkerConfig(0f, "default")
        );

        viewModel.setDraftLocation(draft);

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

        LocationItem draft = viewModel.getCurrentLocation();

        draft.polygonConfig.points.add(latLng);

        viewModel.setDraftLocation(draft);

        context.mapActions.renderTempPolygon(draft.polygonConfig.points);
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

        LocationItem draft =
                viewModel.getCurrentLocation();

        draft.name = name;
        draft.lat = lat;
        draft.lng = lng;

        viewModel.setDraftLocation(draft);

        context.mapActions.renderTempLocation(new LatLng(lat, lng));
        context.fabController.setMenu(R.menu.fab_polyeditactions_menu);
    }

    @Override
    public void onSearchCancelled() {

        context.workflowNavigator.finishWorkflow();
    }

    private void onConfirmPolygon() {

        context.mapActions.setMapGesturesEnabled(false);

        LocationItem draft =
                viewModel.getCurrentLocation();

        LocationConfigFragment fragment =
                LocationConfigFragment.newInstance(
                        draft.name,
                        draft.type,
                        draft.lat,
                        draft.lng,
                        draft.markerConfig,
                        draft.polygonConfig
                );

        fragment.setListener(this);


        context.bottomSheetController.show(fragment);
    }

    private void onUndoPolygon() {

        LocationItem draft = viewModel.getCurrentLocation();

        if (!draft.polygonConfig.points.isEmpty()) {

            draft.polygonConfig.points.remove(draft.polygonConfig.points.size() - 1);

            viewModel.setDraftLocation(draft);

            context.mapActions.renderTempPolygon(draft.polygonConfig.points);
        }
    }

    private void onCancelPolygon() {

        context.workflowNavigator.finishWorkflow();
    }

    @Override
    public void onLocationConfirmed(LocationItem locationItem) {

        LocationItem draft = viewModel.getCurrentLocation();

        viewModel.setDraftLocation(draft);

        context.sessionActions.createNewLocation(draft);

        context.workflowNavigator.finishWorkflow();
    }
}