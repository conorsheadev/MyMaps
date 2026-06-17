package com.csws.mymaps.coordinators.workflows;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.csws.mymaps.core.models.locations.LocationItem;
import com.csws.mymaps.coordinators.map.MapViewContext;
import com.csws.mymaps.coordinators.workflows.workflows.Workflow;
import com.csws.mymaps.coordinators.workflows.workflows.create_collection.CreateCollectionViewModel;
import com.csws.mymaps.coordinators.workflows.workflows.create_plan.CreatePlannedTaskViewModel;
import com.csws.mymaps.coordinators.workflows.workflows.create_location.CreateLocationWorkflow;
import com.csws.mymaps.coordinators.workflows.workflows.create_plan.CreatePlannedTaskWorkflow;
import com.csws.mymaps.coordinators.workflows.workflows.create_collection.CreateTaskCollectionWorkflow;
import com.csws.mymaps.coordinators.workflows.workflows.create_task.CreateTaskWorkflow;
import com.csws.mymaps.coordinators.workflows.workflows.create_location.CreateLocationViewModel;
import com.csws.mymaps.coordinators.workflows.workflows.create_task.CreateTaskViewModel;

/**
 * Central coordinator for workflow execution within the map screen.
 *
 * Responsibilities:
 *
 * 1. Maintains the currently active workflow.
 * 2. Routes user input to either the active workflow or the default
 *    map browsing mode.
 * 3. Controls workflow lifecycle (start / stop / finish).
 *
 * TODO: Move workflow construction into a dedicated Factory class e.g. WorkflowFactory
 */
public class WorkflowManager implements com.csws.mymaps.core.contracts.WorkflowManager {

    private final AppCompatActivity activity;
    private final MapViewContext context;
    private Workflow activeWorkflow;

    public WorkflowManager(AppCompatActivity activity, MapViewContext context) {
        this.activity = activity;
        context.workflowManager = this;
        this.context = context;
    }



    // --- FLOW NAVIGATION ---
    @Override
    public void startWorkflow(Workflow flow) {
        if (activeWorkflow != null) activeWorkflow.stop();
        activeWorkflow = flow;
        if (activeWorkflow != null) activeWorkflow.start();
    }
    @Override
    public boolean hasActiveWorkflow() {
        return activeWorkflow != null;
    }
    @Override
    public Workflow getActiveWorkflow() { return activeWorkflow; }
    @Override
    public void startCreateLocationFlow() {

        CreateLocationViewModel vm = new ViewModelProvider(activity).get(CreateLocationViewModel.class);
        startWorkflow(new CreateLocationWorkflow(vm, context));
    }
    @Override
    public void startCreatePlannedTaskFlow() {

        CreatePlannedTaskViewModel vm = new ViewModelProvider(activity).get(CreatePlannedTaskViewModel.class);
        startWorkflow(new CreatePlannedTaskWorkflow(vm, context));
    }
    @Override
    public void startCreateTaskFlow() {

        CreateTaskViewModel vm = new ViewModelProvider(activity).get(CreateTaskViewModel.class);
        startWorkflow(new CreateTaskWorkflow(vm, context));
    }
    @Override
    public void startCreateCollectionFlow() {

        CreateCollectionViewModel vm = new ViewModelProvider(activity).get(CreateCollectionViewModel.class);
        startWorkflow(new CreateTaskCollectionWorkflow(vm, context));
    }
    @Override
    public void startCreateTaskFromLocationFlow(LocationItem location) {

        startCreateLocationFlow();
        activeWorkflow.onLocationSelected(location);
    }
    @Override
    public void finishWorkflow() {

        if (activeWorkflow != null) {
            activeWorkflow.stop();
            activeWorkflow = null;
        }
    }

}
