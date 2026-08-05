package com.csws.mymaps.coordinators.workflows.create_location;

import com.csws.mymaps.R;

import com.csws.mymaps.core.models.locations.LocationItem;
import com.csws.mymaps.core.models.locations.MarkerConfig;
import com.csws.mymaps.core.models.locations.PolygonConfig;
import com.csws.mymaps.core.ui.forms.LocationConfigFragment;
import com.csws.mymaps.core.ui.pickers.PlaceSearchFragment;
import com.csws.mymaps.coordinators.CoordinatorContext;
import com.csws.mymaps.coordinators.workflows.BaseWorkflow;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.UUID;

public class CreateLocationWorkflow extends BaseWorkflow implements LocationConfigFragment.Listener, PlaceSearchFragment.PlaceSelectionListener {

    private final CreateLocationViewModel viewModel;

    public CreateLocationWorkflow(CreateLocationViewModel viewModel, CoordinatorContext context) {
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

        context.uiCoordinator.showTopSheet(fragment);
    }

    @Override
    public void stop() {

        resetUI();

        context.uiCoordinator.setMapGesturesEnabled(true);
    }

    @Override
    public void onMapClicked(LatLng latLng) {

        LocationItem draft = viewModel.getCurrentLocation();

        draft.polygonConfig.points.add(latLng);

        viewModel.setDraftLocation(draft);

        context.uiCoordinator.renderTempPolygon(draft.polygonConfig.points);
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

        context.uiCoordinator.renderTempLocation(new LatLng(lat, lng));
        context.uiCoordinator.showFabMenu(R.menu.fab_polyeditactions_menu);
    }

    @Override
    public void onSearchCancelled() {

        context.workflowManager.finishWorkflow();
    }

    private void onConfirmPolygon() {

        context.uiCoordinator.setMapGesturesEnabled(false);

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


        context.uiCoordinator.showBottomSheet(fragment);
    }

    private void onUndoPolygon() {

        LocationItem draft = viewModel.getCurrentLocation();

        if (!draft.polygonConfig.points.isEmpty()) {

            draft.polygonConfig.points.remove(draft.polygonConfig.points.size() - 1);

            viewModel.setDraftLocation(draft);

            context.uiCoordinator.renderTempPolygon(draft.polygonConfig.points);
        }
    }

    private void onCancelPolygon() {

        context.workflowManager.finishWorkflow();
    }

    @Override
    public void onLocationConfirmed(LocationItem locationItem) {

        context.entityCreationService.createLocation(locationItem);

        context.workflowManager.finishWorkflow();
    }
}