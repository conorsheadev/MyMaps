package com.csws.mymaps.features.map.flows;

import com.csws.mymaps.R;
import com.csws.mymaps.core.flow.interfaces.FlowActions;
import com.csws.mymaps.core.flow.interfaces.SessionActions;
import com.csws.mymaps.core.viewmodel.LocationViewModel;
import com.csws.mymaps.core.viewmodel.TaskViewModel;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.core.flow.ActionFlow;
import com.csws.mymaps.core.flow.interfaces.ActivityActions;
import com.csws.mymaps.core.flow.interfaces.MapActions;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.session.DailySession;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.features.map.controllers.ui.bottom_sheets.DayPlanFragment;
import com.csws.mymaps.features.map.controllers.ui.bottom_sheets.LocationDetailFragment;
import com.csws.mymaps.features.map.viewmodels.DefaultFlowViewModel;
import com.csws.mymaps.core.viewmodel.PlannedTaskViewModel;
import com.csws.mymaps.features.map.viewmodels.SessionViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class DefaultFlow implements ActionFlow {
    private final DefaultFlowViewModel viewModel;
    private final TaskViewModel taskViewModel;
    private final PlannedTaskViewModel plannedTaskViewModel;
    private final LocationViewModel locationViewModel;
    private final SessionViewModel sessionViewModel;

    private final ActivityActions actions;
    private final SessionActions sessionActions;
    private final FlowActions flowActions;
    private final MapActions mapActions;

    public DefaultFlow(DefaultFlowViewModel viewModel, TaskViewModel taskViewModel, PlannedTaskViewModel plannedTaskViewModel, LocationViewModel locationViewModel, SessionViewModel sessionViewModel, ActivityActions actions, SessionActions sessionActions, FlowActions flowActions, MapActions mapActions) {
        this.viewModel = viewModel;
        this.taskViewModel = taskViewModel;
        this.plannedTaskViewModel = plannedTaskViewModel;
        this.locationViewModel = locationViewModel;
        this.sessionViewModel = sessionViewModel;
        this.actions = actions;
        this.sessionActions = sessionActions;
        this.flowActions = flowActions;
        this.mapActions = mapActions;
    }

    @Override
    public void start(){
        actions.setFabMenu(R.menu.fab_defaultactions_menu);
    }

    @Override
    public void onAction(int actionId) {
        if (actionId == R.id.fab_add_location) {
            flowActions.startCreateLocationFlow();
        }

        if (actionId == R.id.fab_add_task) {
            flowActions.startCreateTaskFlow();
        }

        if (actionId ==R.id.fab_add_task_to_location){
            flowActions.startCreateTaskFromLocationFlow(viewModel.getCurrentLocation());
        }
    }

    @Override
    public void onLocationSelected(LocationItem location) {
        //TODO: ReImplement DisplayLocationDetails
        if(viewModel.getCurrentLocation() != null && location.id.equals(viewModel.getCurrentLocation().id)){
            mapActions.focusLocation(location);

            List<TaskItem> tasks = taskViewModel.getTasksForLocation(location.id);
            List<PlannedTask> plannedTasks = plannedTaskViewModel.getPlansForTasks(tasks);
            actions.showBottomSheet(LocationDetailFragment.newInstance(location, tasks,plannedTasks));
            return;
        }

        mapActions.previewLocation(location);
        viewModel.setSelectedLocation(location);
        actions.setFabMenu(R.menu.fab_locationactions_menu);
    }

    @Override
    public void onRecenterClicked() {
        if (mapActions.isCenteredOnUser()) {

            DailySession session = sessionViewModel.getCurrentSession().getValue();
            List<TaskItem> tasks = taskViewModel.getTasks().getValue();
            List<PlannedTask> plannedTasks = plannedTaskViewModel.getPlannedTasks().getValue();

            if (session == null) {session = new DailySession();}
            if (tasks == null) {tasks = new ArrayList<>();}
            if (plannedTasks == null) {plannedTasks = new ArrayList<>();}


            DayPlanFragment fragment = DayPlanFragment.newInstance(session, tasks, plannedTasks);

            actions.showBottomSheet(fragment);

        } else {

            mapActions.moveToUserLocation();
        }
    }

    @Override
    public void onMapClicked(LatLng latLng) {
        viewModel.clearSelection();
        actions.hideBottomSheet();
        actions.setFabMenu(R.menu.fab_defaultactions_menu);
    }

    @Override
    public void onCancel() {
        //TODO: ReImplement Cancel
    }
}
