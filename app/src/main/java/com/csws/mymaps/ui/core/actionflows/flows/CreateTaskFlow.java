package com.csws.mymaps.ui.core.actionflows.flows;

import com.csws.mymaps.R;
import com.csws.mymaps.model.locations.LocationItem;
import com.csws.mymaps.model.locations.MarkerConfig;
import com.csws.mymaps.model.locations.PolygonConfig;
import com.csws.mymaps.model.tasks.TaskItem;
import com.csws.mymaps.ui.core.actionflows.ActionFlow;
import com.csws.mymaps.ui.core.actionflows.interfaces.ActivityActions;
import com.csws.mymaps.ui.core.actionflows.interfaces.MapActions;
import com.csws.mymaps.ui.mapviewer.deprecated.bottomsheets.TaskConfigFragment;
import com.csws.mymaps.ui.mapviewer.fragments.placesearch.PlaceSearchFragment;
import com.csws.mymaps.viewmodel.flows.CreateTaskViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class CreateTaskFlow implements ActionFlow, PlaceSearchFragment.PlaceSelectionListener {

    private final CreateTaskViewModel viewModel;
    private final ActivityActions actions;
    private final MapActions mapActions;

    public CreateTaskFlow(CreateTaskViewModel viewModel, ActivityActions actions, MapActions mapActions) {
        this.viewModel = viewModel;
        this.actions = actions;
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
            createTask();
        }
    }

    @Override
    public void onLocationSelected(LocationItem location) {
        // Existing marker selected
        onLocationChosen(location);
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
    public void onSearchCancelled() {}
    // --- Internal Functionality ---
    private void onLocationChosen(LocationItem location) {
        viewModel.setLocation(location);

        mapActions.focusLocation(location);

        viewModel.setStage(CreateTaskViewModel.Stage.CONFIGURE_TASK);

        openTaskConfig();
    }

    private void openTaskConfig() {
        TaskConfigFragment fragment = TaskConfigFragment.newInstance(viewModel.getLocation().getValue().id);

        fragment.setListener(task -> {
            actions.createNewTask(task);
            actions.hideBottomSheet();
            actions.cancelCurrentFlow();
        });

        actions.showBottomSheet(fragment);
    }

    private void createTask() {
        LocationItem location = viewModel.getCurrentLocation();
        if (location == null) return;

        TaskItem task = new TaskItem(
                UUID.randomUUID().toString(),
                viewModel.getTaskName(),
                viewModel.getTaskDescription(),
                location.id,
                TaskItem.TaskType.SCHEDULED
        );

        actions.createNewTask(task);

        actions.hideBottomSheet();
        actions.cancelCurrentFlow();
    }

    @Override
    public void onCancel() {
        mapActions.clearTemp();
        actions.hideBottomSheet();
        viewModel.reset();
    }
}