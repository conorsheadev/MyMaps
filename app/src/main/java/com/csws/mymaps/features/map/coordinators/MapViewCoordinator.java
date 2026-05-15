package com.csws.mymaps.features.map.coordinators;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.csws.mymaps.core.flow.ActionFlowFactory;
import com.csws.mymaps.core.flow.interfaces.ActivityActions;
import com.csws.mymaps.core.flow.interfaces.FlowActions;
import com.csws.mymaps.core.flow.interfaces.MapActions;
import com.csws.mymaps.core.flow.interfaces.SessionActions;
import com.csws.mymaps.core.viewmodel.LocationViewModel;
import com.csws.mymaps.core.viewmodel.PlannedTaskViewModel;
import com.csws.mymaps.core.viewmodel.TaskViewModel;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.session.SessionStartType;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.features.map.controllers.BottomSheetController;
import com.csws.mymaps.features.map.controllers.MapFabController;
import com.csws.mymaps.features.map.controllers.MapToolbarController;
import com.csws.mymaps.features.map.controllers.map.MapFragment;
import com.csws.mymaps.features.map.controllers.ui.dialogs.SessionStartDialogFragment;
import com.csws.mymaps.features.map.coordinators.flows.CreateTaskFlow;
import com.csws.mymaps.features.map.viewmodels.CreateLocationViewModel;
import com.csws.mymaps.features.map.viewmodels.CreateTaskViewModel;
import com.csws.mymaps.features.map.viewmodels.DefaultFlowViewModel;
import com.csws.mymaps.features.map.viewmodels.SessionViewModel;
import com.google.android.gms.maps.model.LatLng;

public class MapViewCoordinator implements SessionActions, FlowActions, MapToolbarController.Listener, MapFabController.FabActionListener, MapFragment.MapCallbacks, BottomSheetController.Listener, SessionStartDialogFragment.Listener {

    private final AppCompatActivity activity;

    private final ActivityActions actions;
    private final MapActions mapActions;

    private final LocationViewModel locationViewModel;
    private final TaskViewModel taskViewModel;
    private final PlannedTaskViewModel plannedTaskViewModel;
    private final SessionViewModel sessionViewModel;

    private final ActionFlowController flowController;
    private final ActionFlowFactory flowFactory;

    public MapViewCoordinator(AppCompatActivity activity, ActivityActions actions, MapActions mapActions, LocationViewModel locationViewModel, TaskViewModel taskViewModel, PlannedTaskViewModel plannedTaskViewModel, SessionViewModel sessionViewModel) {

        this.activity = activity;

        this.actions = actions;
        this.mapActions = mapActions;

        this.locationViewModel = locationViewModel;
        this.taskViewModel = taskViewModel;
        this.plannedTaskViewModel = plannedTaskViewModel;
        this.sessionViewModel = sessionViewModel;

        flowController = new ActionFlowController();

        flowFactory = new ActionFlowFactory(
                taskViewModel,
                plannedTaskViewModel,
                locationViewModel,
                sessionViewModel,
                actions,
                this,
                this,
                mapActions
        );
    }

    // --- Setup ---
    public void observe(LifecycleOwner owner) {
        //TODO: Clean Up
        locationViewModel.getLocations().observe(owner, locations -> {

            if (mapActions instanceof MapFragment) {

                ((MapFragment) mapActions).displayLocations(locations);
            }
        });

        Observer<Object> refreshTasks = o -> {

            if (mapActions instanceof MapFragment) {

                ((MapFragment) mapActions).setTasks(
                        taskViewModel.getTasks().getValue(),
                        plannedTaskViewModel.getPlannedTasks().getValue()
                );
            }
        };

        taskViewModel.getTasks().observe(owner, refreshTasks);
        plannedTaskViewModel.getPlannedTasks().observe(owner, refreshTasks);
    }

    // --- Start ---
    public void start() {
        initializeSession();

        cancelCurrentFlow();
    }

    public void initializeSession() {

        if (sessionViewModel.hasSessionToday()) {

            sessionViewModel.loadTodaySession();

        } else {
            showSessionStartDialog();
        }
    }
    private void showSessionStartDialog() {
        SessionStartDialogFragment dialog = new SessionStartDialogFragment();
        dialog.setListener(this);
        dialog.show(activity.getSupportFragmentManager(), "session_start");
    }
    @Override
    public void onSessionStartSelected(SessionStartType startType) {
        if(startType != SessionStartType.CONTINUED)
        {
            sessionViewModel.createSession(startType);
        }
        else{
            sessionViewModel.loadLatestSession();
        }
    }

    // --- Activity REAL Actions ---
    @Override
    public void createNewLocation(LocationItem locationItem) {
        locationViewModel.addLocation(locationItem);
    }
    @Override
    public void createNewTask(TaskItem taskItem) {
        taskViewModel.addTask(taskItem);
    }
    @Override
    public void createNewPlannedTask(PlannedTask plannedTask) {
        plannedTaskViewModel.addPlannedTask(plannedTask);
    }

    // --- FLOW Actions ---
    @Override
    public void startCreateLocationFlow() {
        CreateLocationViewModel vm = new ViewModelProvider(activity).get(CreateLocationViewModel.class);
        flowController.startFlow(flowFactory.createLocationFlow(vm));
    }
    @Override
    public void startCreateTaskFlow(){
        CreateTaskViewModel vm = new ViewModelProvider(activity).get(CreateTaskViewModel.class);
        flowController.startFlow(flowFactory.createTaskFlow(vm));
    }
    @Override
    public void startCreateTaskFromLocationFlow(LocationItem location){
        CreateTaskViewModel vm = new ViewModelProvider(activity).get(CreateTaskViewModel.class);
        CreateTaskFlow flow = flowFactory.createTaskFlow(vm);
        flowController.startFlow(flow);
        flow.onLocationSelected(location);
    }
    @Override
    public void cancelCurrentFlow() {
        DefaultFlowViewModel vm = new ViewModelProvider(activity).get(DefaultFlowViewModel.class);
        flowController.startFlow(flowFactory.createDefaultFlow(vm));
    }

    // --- MAP/FAB/BOTTOM_SHEET Controller Callbacks ---
    @Override //FAB
    public void onFabAction(int actionId) {
        if (flowController.getCurrentFlow() != null) {
            flowController.getCurrentFlow().onAction(actionId);
        }
    }

    @Override //MAP
    public void onMapClicked(LatLng latLng) {
        flowController.getCurrentFlow().onMapClicked(latLng);
    }
    @Override //MAP
    public void onLocationSelected(LocationItem location) {
        flowController.getCurrentFlow().onLocationSelected(location);
    }
    @Override
    public void onRecenterClicked() {
        if (flowController.getCurrentFlow() != null) {
            flowController.getCurrentFlow().onRecenterClicked();
        }
    }

    @Override //BOTTOM_SHEET
    public void onSheetShown() {
        mapActions.setMapClicksEnabled(false);
    }
    @Override //BOTTOM_SHEET
    public void onSheetHidden() {
        mapActions.setMapClicksEnabled(true);
    }

    @Override //TOOLBAR
    public void onBackPressed() {
        activity.finish();
    }
}
