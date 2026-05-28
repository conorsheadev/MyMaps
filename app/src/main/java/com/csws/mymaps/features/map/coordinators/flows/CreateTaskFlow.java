package com.csws.mymaps.features.map.coordinators.flows;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.locations.MarkerConfig;
import com.csws.mymaps.domain.locations.PolygonConfig;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.core.flow.ActionFlow;
import com.csws.mymaps.features.map.controllers.ui.bottom_sheets.PlannedTaskConfigFragment;
import com.csws.mymaps.features.map.controllers.ui.bottom_sheets.TaskConfigFragment;
import com.csws.mymaps.features.map.controllers.ui.top_sheets.PlaceSearchFragment;
import com.csws.mymaps.features.map.coordinators.FlowContext;
import com.csws.mymaps.features.map.viewmodels.CreateTaskViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class CreateTaskFlow implements ActionFlow, PlaceSearchFragment.PlaceSelectionListener {

    private final CreateTaskViewModel viewModel;
    private final FlowContext flowContext;

    public CreateTaskFlow(CreateTaskViewModel viewModel, FlowContext flowContext){
        this.viewModel = viewModel;
        this.flowContext = flowContext;
    }

    @Override
    public void start() {
        viewModel.reset();

        flowContext.fabController.setMenu(R.menu.fab_select_location_menu);

        flowContext.mapActions.setMapGesturesEnabled(true);
    }

    @Override
    public void onAction(int actionId) {

        if (actionId == R.id.fab_search_place) {
            PlaceSearchFragment fragment = new PlaceSearchFragment();
            fragment.setListener(this);
            flowContext.topSheetController.show(fragment);
        }

        if (actionId == R.id.fab_confirm_task) {

        }
    }

    @Override
    public void onLocationSelected(LocationItem location) {
        // Existing marker selected
        onLocationChosen(location);
    }

    @Override
    public void onRecenterClicked() {

    }

    @Override
    public void onMapClicked(LatLng latLng) {
        // Drop pin case
        LocationItem tempLocation = new LocationItem(
                UUID.randomUUID().toString(),
                "Custom Location",
                "Task Location",
                latLng.latitude,
                latLng.longitude,
                new PolygonConfig(0f, null),
                new MarkerConfig(0f, "default")
        );

        flowContext.mapActions.renderTempLocation(latLng);
        onLocationChosen(tempLocation);
    }

    @Override
    public void onPlaceSelected(String name, double lat, double lng) {
        LocationItem location = new LocationItem(
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
        //TODO: Implement location select functionality
    }
    // --- Internal Functionality ---
    private void onLocationChosen(LocationItem location) {
        viewModel.setLocation(location);
        flowContext.mapActions.focusLocation(location);
        viewModel.setStage(CreateTaskViewModel.Stage.CONFIGURE_TASK);
        openTaskConfig();
    }

    // --- Task Config ---
    private void openTaskConfig() {
        String locationId = viewModel.getCurrentLocation().id;
        TaskConfigFragment fragment = TaskConfigFragment.newInstance(locationId);

        fragment.setListener(task -> {
            viewModel.setDraftTask(task);
            viewModel.setStage(CreateTaskViewModel.Stage.CONFIGURE_PLAN);
            openPlannedTaskConfig();
        });

        flowContext.bottomSheetController.show(fragment);
    }

    // --- PlannedTask Config ---
    private void openPlannedTaskConfig() {

        TaskItem task = viewModel.getDraftTask().getValue();

        PlannedTaskConfigFragment fragment = PlannedTaskConfigFragment.newInstance(task.id);

        fragment.setListener(plannedTask -> {
            viewModel.setDraftPlan(plannedTask);
            completeFlow();
        });

        flowContext.bottomSheetController.show(fragment);
    }

    private void completeFlow() {

        TaskItem task = viewModel.getCurrentTask();

        PlannedTask plannedTask = viewModel.getCurrentPlan();

        if (task == null || plannedTask == null) {
            return;
        }

        flowContext.sessionActions.createNewTask(task);
        flowContext.sessionActions.createNewPlannedTask(plannedTask);
        flowContext.bottomSheetController.hide();
        flowContext.flowNavigator.cancelCurrentFlow();
    }

    @Override
    public void onCancel() {
        flowContext.mapActions.clearTemp();
        flowContext.bottomSheetController.hide();
        viewModel.reset();
    }
}