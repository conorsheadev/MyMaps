package com.csws.mymaps.features.map.workflow.workflows;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.locations.MarkerConfig;
import com.csws.mymaps.domain.locations.PolygonConfig;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.features.map.coordinators.MapViewContext;
import com.csws.mymaps.features.map.interaction.ui.bottom_sheets.PlannedTaskConfigFragment;
import com.csws.mymaps.features.map.interaction.ui.bottom_sheets.pickers.TaskPickerFragment;
import com.csws.mymaps.features.map.interaction.ui.top_sheets.PlaceSearchFragment;
import com.csws.mymaps.features.map.viewmodels.CreatePlannedTaskViewModel;
import com.csws.mymaps.features.map.workflow.BaseWorkflow;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class CreatePlannedTaskWorkflow extends BaseWorkflow implements PlaceSearchFragment.PlaceSelectionListener {
    private final CreatePlannedTaskViewModel viewModel;

    public CreatePlannedTaskWorkflow(CreatePlannedTaskViewModel viewModel, MapViewContext context) {
        super(context);

        this.viewModel = viewModel;
    }

    @Override
    public void start() {

        viewModel.reset();

        context.fabController.setMenu(
                R.menu.fab_select_location_menu
        );

        context.mapActions.setMapGesturesEnabled(true);

        openTaskSelection();


    }

    private void openTaskSelection() {

        TaskPickerFragment fragment =
                new TaskPickerFragment();

        fragment.setListener(task -> {

            viewModel.setTask(task);

            viewModel.setStage(
                    CreatePlannedTaskViewModel.Stage.SELECT_LOCATION
            );
        });

        context.bottomSheetController.show(fragment);
    }

    private void onTaskSelected(TaskItem task) {

        viewModel.setTask(task);
        viewModel.setStage(CreatePlannedTaskViewModel.Stage.SELECT_LOCATION);
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

        LocationItem location =
                new LocationItem(
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
    public void onPlaceSelected(
            String name,
            double lat,
            double lng
    ) {

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

        viewModel.setStage(
                CreatePlannedTaskViewModel.Stage.CONFIGURE_PLAN
        );

        openPlanConfig();
    }

    private void openPlanConfig() {

        TaskItem task = viewModel.getCurrentTask();

        LocationItem location = viewModel.getCurrentLocation();

        PlannedTaskConfigFragment fragment = PlannedTaskConfigFragment.newInstance(task.id, location.id);

        fragment.setListener(plannedTask -> {

            plannedTask.locationId = location.id;

            viewModel.setDraftPlan(plannedTask);

            completeFlow();
        });

        context.bottomSheetController.show(fragment);
    }

    private void completeFlow() {

        PlannedTask plan =
                viewModel.getCurrentPlan();

        if (plan == null) {
            return;
        }

        context.sessionActions.createNewPlannedTask(plan);

        context.workflowNavigator.finishWorkflow();
    }

}
