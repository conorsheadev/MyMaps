package com.csws.mymaps.coordinators.map.behaviour;

import com.csws.mymaps.R;
import com.csws.mymaps.coordinators.map.models.DefaultBrowsingViewModel;
import com.csws.mymaps.core.contracts.ui_coordinator.BrowsingActions;
import com.csws.mymaps.core.contracts.ui_coordinator.UiCoordinator;
import com.csws.mymaps.core.models.locations.LocationItem;
import com.csws.mymaps.coordinators.map.controllers.MapFabController;
import com.csws.mymaps.coordinators.map.controllers.map.MapFragment;
import com.google.android.gms.maps.model.LatLng;

public class MapBrowsingMode implements MapFabController.FabActionListener, MapFragment.MapCallbacks {



    private final DefaultBrowsingViewModel viewModel;
    private final UiCoordinator ui;
    private final BrowsingActions actions;

    public MapBrowsingMode(DefaultBrowsingViewModel viewModel, UiCoordinator ui, BrowsingActions actions) {

        this.viewModel = viewModel;
        this.actions = actions;
        this.ui = ui;

        ui.showDefaultFabMenu();
    }

    // --- FAB EVENTS ---
    @Override
    public void onFabAction(int actionId) {

        if (actionId == R.id.fab_add_location) {
            actions.startCreateLocation();
        }

        else if (actionId == R.id.fab_add_plan){

            actions.startCreatePlan();
        }

        else if (actionId == R.id.fab_add_task) {

            actions.startCreateTask();
        }

        else if (actionId == R.id.fab_add_collection) {

            actions.startCreateCollection();
        }

        else if (actionId == R.id.fab_add_task_to_location) {

            actions.startCreateTaskFromLocation(viewModel.getCurrentLocation());
        }
    }
    // --- MAP EVENTS ---
    @Override
    public void onMapClicked(LatLng latLng) {

        viewModel.clearSelection();

        ui.hideBottomSheet();

        ui.showDefaultFabMenu();
    }
    @Override
    public void onLocationSelected(LocationItem location) {

        /*LEGACY

        if (viewModel.getCurrentLocation() != null && location.id.equals(viewModel.getCurrentLocation().id)) {


            ui.focusLocation(location);

            List<PlannedTask> plannedTasks = context.plannedTaskViewModel.getPlansForLocation(location.id);
            Set<String> taskIds = new HashSet<>();

            for (PlannedTask plan : plannedTasks) {

                if (plan.taskId != null) {
                    taskIds.add(plan.taskId);
                }
            }
            List<TaskItem> tasks = context.taskViewModel.getTasksByIds(taskIds);

            context.bottomSheetController.show(
                    LocationPlanFragment.newInstance(
                            location,
                            tasks,
                            plannedTasks
                    )
            );

            return;
        }
        */

        ui.previewLocation(location);

        viewModel.setSelectedLocation(location);

        ui.showLocationFabMenu();
    }
    @Override public void onRecenterClicked() {
        /* LEGACY

        if (context.mapController.isCenteredOnUser()) {

            DailySession session = context.sessionViewModel.getCurrentSession().getValue();

            List<TaskItem> tasks = context.taskViewModel.getTasks().getValue();

            List<PlannedTask> plannedTasks = context.plannedTaskViewModel.getPlannedTasks().getValue();

            if (session == null) {
                session = new DailySession();
            }

            if (tasks == null) {
                tasks = new ArrayList<>();
            }

            if (plannedTasks == null) {
                plannedTasks = new ArrayList<>();
            }

            DayPlanFragment fragment = DayPlanFragment.newInstance(session, tasks, plannedTasks);

            context.bottomSheetController.show(fragment);

        } else {

            context.mapController.moveToUserLocation();
        }*/

        if(ui.isCenteredOnUser()){
            ui.showDayPlan();
        }
        else
        {
            ui.moveToUserLocation();
        }
    }
}
