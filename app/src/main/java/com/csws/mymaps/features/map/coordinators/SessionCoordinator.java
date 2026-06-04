package com.csws.mymaps.features.map.coordinators;

import com.csws.mymaps.core.flow.interfaces.coordinator_interfaces.SessionActions;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskCollection;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.features.map.workflows.sessionflows.InitialiseSessionWorkflow;

public class SessionCoordinator implements SessionActions {

    private final MapViewContext context;

    public SessionCoordinator(MapViewContext context) {
        this.context = context;
    }

    public void start() {

        context.workflowNavigator.finishWorkflow();

        boolean hasSession = context.sessionViewModel.hasSessionToday();

        if (hasSession) {
            context.sessionViewModel.loadTodaySession();

        } else {
            context.workflowNavigator.startWorkflow(new InitialiseSessionWorkflow(context));
        }
    }

    // --- SessionActions ---
    @Override
    public void createNewLocation(LocationItem locationItem) {

        context.locationViewModel.addLocation(locationItem);
    }

    @Override
    public void createNewTask(TaskItem taskItem) {

        context.taskViewModel.addTask(taskItem);
    }

    @Override
    public void createNewPlannedTask(PlannedTask plannedTask) {

        context.plannedTaskViewModel.addPlannedTask(plannedTask);
    }

    @Override
    public void createTaskCollection(TaskCollection collection) {

        context.taskCollectionViewModel.addCollection(collection);
    }
}
