package com.csws.mymaps.features.map.coordinators;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.csws.mymaps.core.flow.ActionFlow;
import com.csws.mymaps.core.flow.interfaces.FlowNavigator;
import com.csws.mymaps.core.flow.interfaces.MapActions;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.features.map.controllers.BottomSheetController;
import com.csws.mymaps.features.map.controllers.MapFabController;
import com.csws.mymaps.features.map.controllers.MapToolbarController;
import com.csws.mymaps.features.map.controllers.map.MapFragment;
import com.csws.mymaps.features.map.coordinators.flows.CreateLocationFlow;
import com.csws.mymaps.features.map.coordinators.flows.CreateTaskFlow;
import com.csws.mymaps.features.map.coordinators.flows.DefaultFlow;
import com.csws.mymaps.features.map.viewmodels.CreateLocationViewModel;
import com.csws.mymaps.features.map.viewmodels.CreateTaskViewModel;
import com.csws.mymaps.features.map.viewmodels.DefaultFlowViewModel;
import com.google.android.gms.maps.model.LatLng;

public class ActionFlowController implements  MapToolbarController.Listener, MapFabController.FabActionListener, MapFragment.MapCallbacks, BottomSheetController.Listener, FlowNavigator {

    private final AppCompatActivity activity;
    private final FlowContext context;

    private ActionFlow currentFlow;

    public ActionFlowController(AppCompatActivity activity, FlowContext context) {
        this.activity = activity;
        context.flowNavigator = this;
        this.context = context;
    }
    public void bindCallbacks(MapToolbarController toolbarController, MapFragment mapFragment, MapFabController fabController, BottomSheetController bottomSheetController) {
        toolbarController.setListener(this);
        mapFragment.setListener(this);
        fabController.setListener(this);
        bottomSheetController.setListener(this);
    }

    public ActionFlow getCurrentFlow() { return currentFlow; }
    public void startFlow(ActionFlow flow) {
        if (currentFlow != null) currentFlow.onCancel();
        currentFlow = flow;
        if (currentFlow != null) currentFlow.start();
    }


    // --- FAB EVENTS ---
    @Override public void onFabAction(int actionId) {

        if (currentFlow != null) {
            currentFlow.onAction(actionId);
        }
    }
    // --- MAP EVENTS ---
    @Override public void onMapClicked(LatLng latLng) {

        if (currentFlow != null) {
            currentFlow.onMapClicked(latLng);
        }
    }
    @Override public void onLocationSelected(LocationItem location) {

        if (currentFlow != null) {
            currentFlow.onLocationSelected(location);
        }
    }
    @Override public void onRecenterClicked() {

        if (currentFlow != null) {
            currentFlow.onRecenterClicked();
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
        startFlow(new DefaultFlow(vm, context));
    }
    @Override
    public void startCreateLocationFlow() {

        CreateLocationViewModel vm = new ViewModelProvider(activity).get(CreateLocationViewModel.class);
        startFlow(new CreateLocationFlow(vm, context));
    }
    @Override
    public void startCreateTaskFlow() {

        CreateTaskViewModel vm = new ViewModelProvider(activity).get(CreateTaskViewModel.class);
        startFlow(new CreateTaskFlow(vm, context));
    }
    @Override
    public void startCreateTaskFromLocationFlow(LocationItem location) {

        startCreateLocationFlow();
        currentFlow.onLocationSelected(location);
    }
    @Override
    public void cancelCurrentFlow() {

        if (currentFlow != null) {
            currentFlow.onCancel();
            currentFlow = null;
        }

        startDefaultFlow();
    }

}
