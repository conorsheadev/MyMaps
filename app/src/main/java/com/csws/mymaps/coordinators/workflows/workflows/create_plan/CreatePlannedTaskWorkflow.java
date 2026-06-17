package com.csws.mymaps.coordinators.workflows.workflows.create_plan;

import com.csws.mymaps.R;
import com.csws.mymaps.core.models.locations.LocationItem;
import com.csws.mymaps.core.models.locations.MarkerConfig;
import com.csws.mymaps.core.models.locations.PolygonConfig;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.coordinators.map.MapViewContext;
import com.csws.mymaps.core.ui.forms.PlannedTaskConfigFragment;
import com.csws.mymaps.core.ui.pickers.LocationPickerFragment;
import com.csws.mymaps.core.ui.pickers.TaskPickerFragment;
import com.csws.mymaps.core.ui.pickers.PlaceSearchFragment;
import com.csws.mymaps.coordinators.workflows.workflows.BaseWorkflow;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class CreatePlannedTaskWorkflow extends BaseWorkflow implements PlannedTaskConfigFragment.Listener, PlaceSearchFragment.PlaceSelectionListener {
    private final CreatePlannedTaskViewModel viewModel;
    private PlannedTaskConfigFragment configFragment;

    public CreatePlannedTaskWorkflow(CreatePlannedTaskViewModel viewModel, MapViewContext context) {
        super(context);

        this.viewModel = viewModel;
    }

    @Override
    public void start() {

        viewModel.reset();

        context.uiCoordinator.showFabMenu(R.menu.fab_select_location_menu);

        context.uiCoordinator.setMapGesturesEnabled(true);

        openPlanConfig();
    }
    @Override
    public void stop() {

        resetUI();
        viewModel.reset();
    }

    private void openPlanConfig() {

        configFragment = PlannedTaskConfigFragment.newInstance();

        configFragment.setListener(this);
        configFragment.setSelectedTask(viewModel.getCurrentTask());
        configFragment.setSelectedLocation(viewModel.getCurrentLocation());

        context.uiCoordinator.showBottomSheet(configFragment);
    }

    @Override
    public void onSelectTaskRequested() {

        TaskPickerFragment fragment = new TaskPickerFragment();

        fragment.setListener(task -> {

            viewModel.setTask(task);

            if (configFragment != null) {
                configFragment.setSelectedTask(task);
            }

            openPlanConfig();
        });

        context.uiCoordinator.showBottomSheet(fragment);
    }

    @Override
    public void onSelectLocationRequested() {

        LocationPickerFragment fragment = new LocationPickerFragment();

        fragment.setListener(location -> {

            viewModel.setLocation(location);

            if (configFragment != null) {
                configFragment.setSelectedLocation(location);
            }

            openPlanConfig();
        });

        context.uiCoordinator.showBottomSheet(fragment);
    }

    @Override
    public void onPlannedTaskConfirmed(PlannedTask plannedTask) {
        viewModel.setDraftPlan(plannedTask);
        completeFlow();
    }

    @Override
    public void onAction(int actionId) {

        if (actionId == R.id.fab_search_place) {

            PlaceSearchFragment fragment = new PlaceSearchFragment();

            fragment.setListener(this);

            context.uiCoordinator.showBottomSheet(fragment);
        }
    }

    @Override
    public void onMapClicked(LatLng latLng) {

        LocationItem location = new LocationItem(
                        UUID.randomUUID().toString(),
                        "Custom Location",
                        "Task Location",
                        latLng.latitude,
                        latLng.longitude,
                        new PolygonConfig(0f, null),
                        new MarkerConfig(0f, "default")
                );

        context.uiCoordinator.renderTempLocation(latLng);

        onLocationChosen(location);
    }

    @Override
    public void onLocationSelected(LocationItem location) {

        onLocationChosen(location);
    }

    @Override
    public void onPlaceSelected(String name, double lat, double lng) {

        LocationItem location =
                new LocationItem(
                        UUID.randomUUID().toString(),
                        name,
                        "Task Location",
                        lat,
                        lng,
                        new PolygonConfig(0f, null),
                        new MarkerConfig(0f, "default")
                );

        onLocationChosen(location);
    }
    @Override
    public void onSearchCancelled(){}

    private void onLocationChosen(LocationItem location) {

        viewModel.setLocation(location);

        context.uiCoordinator.focusLocation(location);

        openPlanConfig();
    }

    private void completeFlow() {

        PlannedTask plan = viewModel.getCurrentPlan();

        if (plan == null) {
            return;
        }

        context.entityCreationService.createPlannedTask(plan);

        context.workflowManager.finishWorkflow();
    }

}
