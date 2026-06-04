package com.csws.mymaps.features.map.workflow.workflows;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.locations.MarkerConfig;
import com.csws.mymaps.domain.locations.PolygonConfig;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.features.map.coordinators.MapViewContext;
import com.csws.mymaps.features.map.interaction.ui.bottom_sheets.PlannedTaskConfigFragment;
import com.csws.mymaps.features.map.interaction.ui.bottom_sheets.pickers.LocationPickerFragment;
import com.csws.mymaps.features.map.interaction.ui.bottom_sheets.pickers.TaskPickerFragment;
import com.csws.mymaps.features.map.interaction.ui.top_sheets.PlaceSearchFragment;
import com.csws.mymaps.features.map.viewmodels.CreatePlannedTaskViewModel;
import com.csws.mymaps.features.map.workflow.BaseWorkflow;
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

        context.fabController.setMenu(R.menu.fab_select_location_menu);

        context.mapActions.setMapGesturesEnabled(true);

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

        context.bottomSheetController.show(configFragment);
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

        context.bottomSheetController.show(fragment);
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

        context.bottomSheetController.show(fragment);
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

            context.topSheetController.show(fragment);
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

        context.mapActions.renderTempLocation(latLng);

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

        context.mapActions.focusLocation(location);

        openPlanConfig();
    }

    private void completeFlow() {

        PlannedTask plan = viewModel.getCurrentPlan();

        if (plan == null) {
            return;
        }

        context.sessionActions.createNewPlannedTask(plan);

        context.workflowNavigator.finishWorkflow();
    }

}
