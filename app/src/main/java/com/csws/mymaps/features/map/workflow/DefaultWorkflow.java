package com.csws.mymaps.features.map.workflow;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.session.DailySession;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.features.map.ui.bottom_sheets.DayPlanFragment;
import com.csws.mymaps.features.map.ui.bottom_sheets.LocationDetailFragment;
import com.csws.mymaps.features.map.coordinators.FlowContext;
import com.csws.mymaps.features.map.viewmodels.DefaultFlowViewModel;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class DefaultWorkflow extends BaseWorkflow {

    private final DefaultFlowViewModel viewModel;

    public DefaultWorkflow(DefaultFlowViewModel viewModel, FlowContext context) {
        super(context);
        this.viewModel = viewModel;
    }

    @Override
    public void start() {
        context.fabController.setMenu(R.menu.fab_defaultactions_menu);
    }

    @Override
    public void onAction(int actionId) {

        if (actionId == R.id.fab_add_location) {
            context.workflowNavigator.startCreateLocationFlow();
        }

        else if (actionId == R.id.fab_add_task) {
            context.workflowNavigator.startCreateTaskFlow();
        }

        else if (actionId == R.id.fab_add_task_to_location) {

            context.workflowNavigator.startCreateTaskFromLocationFlow(
                    viewModel.getCurrentLocation()
            );
        }
    }

    @Override
    public void onLocationSelected(LocationItem location) {

        if (viewModel.getCurrentLocation() != null && location.id.equals(viewModel.getCurrentLocation().id)) {

            context.mapActions.focusLocation(location);

            List<TaskItem> tasks = context.taskViewModel.getTasksForLocation(location.id);
            List<PlannedTask> plannedTasks = context.plannedTaskViewModel.getPlansForTasks(tasks);

            context.bottomSheetController.show(
                    LocationDetailFragment.newInstance(
                            location,
                            tasks,
                            plannedTasks
                    )
            );

            return;
        }

        context.mapActions.previewLocation(location);

        viewModel.setSelectedLocation(location);

        context.fabController.setMenu(R.menu.fab_locationactions_menu);
    }

    @Override
    public void onRecenterClicked() {

        if (context.mapActions.isCenteredOnUser()) {

            DailySession session = context.sessionViewModel.getCurrentSession().getValue();
            List<TaskItem> tasks = context.taskViewModel.getTasks().getValue();
            List<PlannedTask> plannedTasks = context.plannedTaskViewModel.getPlannedTasks().getValue();

            if (session == null) session = new DailySession();
            if (tasks == null) tasks = new ArrayList<>();
            if (plannedTasks == null) plannedTasks = new ArrayList<>();

            DayPlanFragment fragment = DayPlanFragment.newInstance(session, tasks, plannedTasks);

            context.bottomSheetController.show(fragment);

        } else {

            context.mapActions.moveToUserLocation();
        }
    }

    @Override
    public void onMapClicked(LatLng latLng) {

        viewModel.clearSelection();

        context.bottomSheetController.hide();

        resetToDefaultFab();
    }
}