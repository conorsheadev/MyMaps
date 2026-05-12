package com.csws.mymaps.features.map.flows;

import com.csws.mymaps.R;
import com.csws.mymaps.core.flow.interfaces.FlowActions;
import com.csws.mymaps.core.flow.interfaces.SessionActions;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.locations.MarkerConfig;
import com.csws.mymaps.domain.locations.PolygonConfig;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.core.flow.ActionFlow;
import com.csws.mymaps.core.flow.interfaces.ActivityActions;
import com.csws.mymaps.core.flow.interfaces.MapActions;
import com.csws.mymaps.features.map.controllers.ui.bottom_sheets.PlannedTaskConfigFragment;
import com.csws.mymaps.features.map.controllers.ui.bottom_sheets.TaskConfigFragment;
import com.csws.mymaps.features.map.controllers.ui.placesearch.PlaceSearchFragment;
import com.csws.mymaps.features.map.viewmodels.CreateTaskViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class CreateTaskFlow implements ActionFlow, PlaceSearchFragment.PlaceSelectionListener {

    private final CreateTaskViewModel viewModel;
    private final ActivityActions actions;
    private final SessionActions sessionActions;
    private final FlowActions flowActions;
    private final MapActions mapActions;

    public CreateTaskFlow(CreateTaskViewModel viewModel, ActivityActions actions, SessionActions sessionActions, FlowActions flowActions, MapActions mapActions) {
        this.viewModel = viewModel;
        this.actions = actions;
        this.sessionActions = sessionActions;
        this.flowActions = flowActions;
        this.mapActions = mapActions;
    }

    @Override
    public void start() {
        viewModel.reset();

        actions.setFabMenu(R.menu.fab_select_location_menu);

        mapActions.setMapGesturesEnabled(true);
    }

    @Override
    public void onAction(int actionId) {

        if (actionId == R.id.fab_search_place) {
            actions.openPlaceSearch(this);
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

        mapActions.renderTempLocation(latLng);
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
        mapActions.focusLocation(location);
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

        actions.showBottomSheet(fragment);
    }

    // --- PlannedTask Config ---
    private void openPlannedTaskConfig() {

        TaskItem task = viewModel.getDraftTask().getValue();

        PlannedTaskConfigFragment fragment = PlannedTaskConfigFragment.newInstance(task.id);

        fragment.setListener(plannedTask -> {
            viewModel.setDraftPlan(plannedTask);
            completeFlow();
        });

        actions.showBottomSheet(fragment);
    }

    private void completeFlow() {

        TaskItem task = viewModel.getCurrentTask();

        PlannedTask plannedTask = viewModel.getCurrentPlan();

        if (task == null || plannedTask == null) {
            return;
        }

        sessionActions.createNewTask(task);
        sessionActions.createNewPlannedTask(plannedTask);
        actions.hideBottomSheet();
        flowActions.cancelCurrentFlow();
    }

    @Override
    public void onCancel() {
        mapActions.clearTemp();
        actions.hideBottomSheet();
        viewModel.reset();
    }
}