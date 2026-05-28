package com.csws.mymaps.features.map.coordinators;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.csws.mymaps.core.flow.Workflow;
import com.csws.mymaps.core.flow.interfaces.WorkflowNavigator;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.features.map.controllers.BottomSheetController;
import com.csws.mymaps.features.map.controllers.MapFabController;
import com.csws.mymaps.features.map.controllers.MapToolbarController;
import com.csws.mymaps.features.map.controllers.map.MapFragment;
import com.csws.mymaps.features.map.workflow.workflows.CreateLocationWorkflow;
import com.csws.mymaps.features.map.workflow.workflows.CreateTaskWorkflow;
import com.csws.mymaps.features.map.workflow.DefaultWorkflow;
import com.csws.mymaps.features.map.viewmodels.CreateLocationViewModel;
import com.csws.mymaps.features.map.viewmodels.CreateTaskViewModel;
import com.csws.mymaps.features.map.viewmodels.DefaultFlowViewModel;
import com.google.android.gms.maps.model.LatLng;

public class WorkflowController implements  MapToolbarController.Listener, MapFabController.FabActionListener, MapFragment.MapCallbacks, BottomSheetController.Listener, WorkflowNavigator {

    private final AppCompatActivity activity;
    private final FlowContext context;

    private Workflow activeWorkflow;

    public WorkflowController(AppCompatActivity activity, FlowContext context) {
        this.activity = activity;
        context.workflowNavigator = this;
        this.context = context;

        context.toolbarController.setListener(this);
        context.mapActions.setListener(this);
        context.fabController.setListener(this);
        context.bottomSheetController.setListener(this);
    }
    public void bindCallbacks(MapToolbarController toolbarController, MapFragment mapFragment, MapFabController fabController, BottomSheetController bottomSheetController) {

    }

    public Workflow getActiveWorkflow() { return activeWorkflow; }
    public void startWorkflow(Workflow flow) {
        if (activeWorkflow != null) activeWorkflow.stop();
        activeWorkflow = flow;
        if (activeWorkflow != null) activeWorkflow.start();
    }


    // --- FAB EVENTS ---
    @Override public void onFabAction(int actionId) {

        if (activeWorkflow != null) {
            activeWorkflow.onAction(actionId);
        }
    }
    // --- MAP EVENTS ---
    @Override public void onMapClicked(LatLng latLng) {

        if (activeWorkflow != null) {
            activeWorkflow.onMapClicked(latLng);
        }
    }
    @Override public void onLocationSelected(LocationItem location) {

        if (activeWorkflow != null) {
            activeWorkflow.onLocationSelected(location);
        }
    }
    @Override public void onRecenterClicked() {

        if (activeWorkflow != null) {
            activeWorkflow.onRecenterClicked();
        }
    }
    // --- SHEET EVENTS ---
    @Override public void onSheetShown() {
        context.mapActions.setMapClicksEnabled(false);
    }
    @Override public void onSheetHidden() {
        context.mapActions.setMapClicksEnabled(true);
    }
    // --- TOOLBAR EVENTS ---
    @Override public void onBackPressed() {
        activity.finish();
    }

    // --- FLOW NAVIGATION ---
    @Override
    public void startDefaultFlow() {

        DefaultFlowViewModel vm = new ViewModelProvider(activity).get(DefaultFlowViewModel.class);
        startWorkflow(new DefaultWorkflow(vm, context));
    }
    @Override
    public void startCreateLocationFlow() {

        CreateLocationViewModel vm = new ViewModelProvider(activity).get(CreateLocationViewModel.class);
        startWorkflow(new CreateLocationWorkflow(vm, context));
    }
    @Override
    public void startCreateTaskFlow() {

        CreateTaskViewModel vm = new ViewModelProvider(activity).get(CreateTaskViewModel.class);
        startWorkflow(new CreateTaskWorkflow(vm, context));
    }
    @Override
    public void startCreateTaskFromLocationFlow(LocationItem location) {

        startCreateLocationFlow();
        activeWorkflow.onLocationSelected(location);
    }
    @Override
    public void cancelCurrentFlow() {

        if (activeWorkflow != null) {
            activeWorkflow.stop();
            activeWorkflow = null;
        }

        startDefaultFlow();
    }

}
