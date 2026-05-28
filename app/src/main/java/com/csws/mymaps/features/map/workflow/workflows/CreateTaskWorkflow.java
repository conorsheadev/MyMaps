package com.csws.mymaps.features.map.workflow.workflows;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.locations.MarkerConfig;
import com.csws.mymaps.domain.locations.PolygonConfig;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.features.map.controllers.ui.bottom_sheets.PlannedTaskConfigFragment;
import com.csws.mymaps.features.map.controllers.ui.bottom_sheets.TaskConfigFragment;
import com.csws.mymaps.features.map.controllers.ui.top_sheets.PlaceSearchFragment;
import com.csws.mymaps.features.map.coordinators.FlowContext;
import com.csws.mymaps.features.map.viewmodels.CreateTaskViewModel;
import com.csws.mymaps.features.map.workflow.BaseWorkflow;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class CreateTaskWorkflow extends BaseWorkflow implements PlaceSearchFragment.PlaceSelectionListener {

    private final CreateTaskViewModel viewModel;

    public CreateTaskWorkflow(CreateTaskViewModel viewModel, FlowContext context) {
        super(context);
        this.viewModel = viewModel;
    }

    @Override
    public void start() {

        viewModel.reset();

        context.fabController.setMenu(R.menu.fab_select_location_menu);

        context.mapActions.setMapGesturesEnabled(true);
    }

    @Override
    public void stop() {

        resetUI();

        viewModel.reset();
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
    public void onLocationSelected(LocationItem location) {
        onLocationChosen(location);
    }

    @Override
    public void onMapClicked(LatLng latLng) {

        LocationItem tempLocation = new LocationItem(
                        UUID.randomUUID().toString(),
                        "Custom Location",
                        "Task Location",
                        latLng.latitude,
                        latLng.longitude,
                        new PolygonConfig(0f, null),
                        new MarkerConfig(0f, "default")
                );

        context.mapActions.renderTempLocation(latLng);

        onLocationChosen(tempLocation);
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
    public void onSearchCancelled() {

    }

    private void onLocationChosen(LocationItem location) {

        viewModel.setLocation(location);

        context.mapActions.focusLocation(location);

        viewModel.setStage(CreateTaskViewModel.Stage.CONFIGURE_TASK);

        openTaskConfig();
    }

    private void openTaskConfig() {

        String locationId = viewModel.getCurrentLocation().id;

        TaskConfigFragment fragment = TaskConfigFragment.newInstance(locationId);

        fragment.setListener(task -> {

            viewModel.setDraftTask(task);

            viewModel.setStage(CreateTaskViewModel.Stage.CONFIGURE_PLAN);

            openPlannedTaskConfig();
        });

        context.bottomSheetController.show(fragment);
    }

    private void openPlannedTaskConfig() {

        TaskItem task = viewModel.getDraftTask().getValue();

        PlannedTaskConfigFragment fragment = PlannedTaskConfigFragment.newInstance(task.id);

        fragment.setListener(plannedTask -> {

            viewModel.setDraftPlan(plannedTask);

            completeFlow();
        });

        context.bottomSheetController.show(fragment);
    }

    private void completeFlow() {

        TaskItem task = viewModel.getCurrentTask();

        PlannedTask plannedTask = viewModel.getCurrentPlan();

        if (task == null || plannedTask == null) {
            return;
        }

        context.sessionActions.createNewTask(task);

        context.sessionActions.createNewPlannedTask(plannedTask);

        context.workflowNavigator.cancelCurrentFlow();
    }
}