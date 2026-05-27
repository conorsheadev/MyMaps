package com.csws.mymaps.features.map.coordinators.flows;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.core.flow.ActionFlow;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.session.DailySession;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.features.map.controllers.ui.bottom_sheets.DayPlanFragment;
import com.csws.mymaps.features.map.controllers.ui.bottom_sheets.LocationDetailFragment;
import com.csws.mymaps.features.map.coordinators.FlowContext;
import com.csws.mymaps.features.map.viewmodels.DefaultFlowViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class DefaultFlow implements ActionFlow {
    private final DefaultFlowViewModel viewModel;
    private final FlowContext flowContext;

    public DefaultFlow(DefaultFlowViewModel viewModel, FlowContext flowContext) {
        this.viewModel = viewModel;
        this.flowContext = flowContext;
    }

    @Override
    public void start(){
        flowContext.fabController.setMenu(R.menu.fab_defaultactions_menu);
    }

    @Override
    public void onAction(int actionId) {
        if (actionId == R.id.fab_add_location) {
            flowContext.flowNavigator.startCreateLocationFlow();
        }

        if (actionId == R.id.fab_add_task) {
            flowContext.flowNavigator.startCreateTaskFlow();
        }

        if (actionId ==R.id.fab_add_task_to_location){
            flowContext.flowNavigator.startCreateTaskFromLocationFlow(viewModel.getCurrentLocation());
        }
    }

    @Override
    public void onLocationSelected(LocationItem location) {
        //TODO: ReImplement DisplayLocationDetails
        if(viewModel.getCurrentLocation() != null && location.id.equals(viewModel.getCurrentLocation().id)){
            flowContext.mapActions.focusLocation(location);

            List<TaskItem> tasks = flowContext.taskViewModel.getTasksForLocation(location.id);
            List<PlannedTask> plannedTasks = flowContext.plannedTaskViewModel.getPlansForTasks(tasks);
            flowContext.bottomSheetController.show(LocationDetailFragment.newInstance(location, tasks,plannedTasks));
            return;
        }

        flowContext.mapActions.previewLocation(location);
        viewModel.setSelectedLocation(location);
        flowContext.fabController.setMenu(R.menu.fab_locationactions_menu);
    }

    @Override
    public void onRecenterClicked() {
        if (flowContext.mapActions.isCenteredOnUser()) {

            DailySession session = flowContext.sessionViewModel.getCurrentSession().getValue();
            List<TaskItem> tasks = flowContext.taskViewModel.getTasks().getValue();
            List<PlannedTask> plannedTasks = flowContext.plannedTaskViewModel.getPlannedTasks().getValue();

            if (session == null) {session = new DailySession();}
            if (tasks == null) {tasks = new ArrayList<>();}
            if (plannedTasks == null) {plannedTasks = new ArrayList<>();}


            DayPlanFragment fragment = DayPlanFragment.newInstance(session, tasks, plannedTasks);

            flowContext.bottomSheetController.show(fragment);

        } else {

            flowContext.mapActions.moveToUserLocation();
        }
    }

    @Override
    public void onMapClicked(LatLng latLng) {
        viewModel.clearSelection();
        flowContext.bottomSheetController.hide();
        flowContext.fabController.setMenu(R.menu.fab_defaultactions_menu);
    }

    @Override
    public void onCancel() {
        //TODO: ReImplement Cancel
    }
}
