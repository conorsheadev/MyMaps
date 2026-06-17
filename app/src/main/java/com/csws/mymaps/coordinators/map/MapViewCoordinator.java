package com.csws.mymaps.coordinators.map;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.csws.mymaps.R;
import com.csws.mymaps.core.contracts.map.MapController;
import com.csws.mymaps.core.contracts.ui_coordinator.BrowsingActions;
import com.csws.mymaps.core.contracts.services.RouteService;
import com.csws.mymaps.core.contracts.ui_coordinator.UiCoordinator;
import com.csws.mymaps.core.models.locations.LocationItem;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.core.models.prompts.PlannerPromptResult;
import com.csws.mymaps.core.models.navigation.NavigationRoute;
import com.csws.mymaps.core.models.navigation.NavigationSession;
import com.csws.mymaps.core.models.tasks.TaskItem;
import com.csws.mymaps.core.models.sessions.DailySession;
import com.csws.mymaps.coordinators.map.controllers.BottomSheetController;
import com.csws.mymaps.coordinators.map.controllers.MapFabController;
import com.csws.mymaps.coordinators.map.controllers.MapToolbarController;
import com.csws.mymaps.coordinators.map.controllers.TopSheetController;
import com.csws.mymaps.coordinators.map.fragments.bottom_sheets.DayPlanFragment;
import com.csws.mymaps.coordinators.map.fragments.bottom_sheets.LocationPlanFragment;
import com.csws.mymaps.coordinators.map.controllers.map.MapFragment;
import com.csws.mymaps.core.ui.prompts.PlannerPromptFragment;
import com.csws.mymaps.core.models.prompts.PlannerPrompt;
import com.csws.mymaps.core.ui.prompts.PromptFragmentFactory;
import com.csws.mymaps.coordinators.map.models.DefaultBrowsingViewModel;
import com.csws.mymaps.coordinators.map.behaviour.MapBrowsingMode;
import com.csws.mymaps.coordinators.workflows.workflows.Workflow;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
/**
 * Coordinates planner prompt presentation on the map screen.
 *
 * Responsibilities:
 *
 * - Maintains a queue of planner prompts.
 * - Displays prompts when the UI is available.
 * - Collects prompt results and forwards them to the scheduler.
 * - Displays navigation previews associated with prompts.
 *
 * TODO: Extract prompt queue management to new class e.g. PromptCoordinator
 */
public class MapViewCoordinator  implements MapToolbarController.Listener,MapFabController.FabActionListener,MapFragment.MapCallbacks, TopSheetController.Listener, BottomSheetController.Listener, UiCoordinator, BrowsingActions {
    //Debugging
    private static final String TAG = "MapViewCoordinator";

    private final AppCompatActivity activity;
    private final MapViewContext mapViewContext;

    private final MapToolbarController toolbarController;
    private final TopSheetController topSheetController;
    private final MapController mapController;
    private final MapFabController fabController;
    private final BottomSheetController bottomSheetController;

    private final MapBrowsingMode browsingMode;
    private final List<PlannerPrompt> prompts = new ArrayList<>();
    private PlannerPrompt currentPrompt;

    public MapViewCoordinator(
            AppCompatActivity activity,
            MapViewContext mapViewContext,

            MapToolbarController toolbarController,
            TopSheetController topSheetController,
            MapController mapController,
            MapFabController fabController,
            BottomSheetController bottomSheetController
    ) {
        this.activity = activity;
        this.mapViewContext = mapViewContext;

        this.toolbarController = toolbarController;
        this.toolbarController.setListener(this);
        this.topSheetController = topSheetController;
        this.topSheetController.setListener(this);
        this.mapController = mapController;
        this.mapController.setListener(this);
        this.fabController = fabController;
        this.fabController.setListener(this);
        this.bottomSheetController = bottomSheetController;
        this.bottomSheetController.setListener(this);
        DefaultBrowsingViewModel vm = new ViewModelProvider(activity).get(DefaultBrowsingViewModel.class);

        browsingMode = new MapBrowsingMode(vm, this, this);
    }



    // --- Observers ---
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private Runnable pendingRefresh;

    public void observe(LifecycleOwner owner) {

        Log.d(TAG, "observe START");

        mapViewContext.sessionManager.getLocationsLiveData().observe(owner, locations -> {

                    Log.d(TAG, "Locations observer fired size=" + (locations != null ? locations.size() : -1));
                    if (mapController instanceof MapFragment) {
                        ((MapFragment) mapController).displayLocations(locations);
                    }
                });

        Observer<Object> refreshTasks = ignored -> {

            if (pendingRefresh != null) {
                mainHandler.removeCallbacks(pendingRefresh);
            }

            pendingRefresh = this::refreshTaskMarkers;
            mainHandler.postDelayed(pendingRefresh, 300);
        };

        mapViewContext.sessionManager.getTasksLiveData().observe(owner, refreshTasks);
        mapViewContext.sessionManager.getPlansLiveData().observe(owner, refreshTasks);

        Log.d(TAG, "observe END");
    }
    private void refreshTaskMarkers() {

        List<TaskItem> tasks = mapViewContext.sessionManager.getAllTasks();
        List<PlannedTask> plans = mapViewContext.sessionManager.getAllPlans();

        if (mapController instanceof MapFragment) {
            ((MapFragment) mapController).setTasks(tasks, plans);
        }
    }
    // --- Start ---
    public void start() {

        Log.d(TAG, "start()");
    }


    //#################
    //### UI Events ###
    //#################

    // --- TOOLBAR ---
    @Override public void onBackPressed() {
        Workflow workflow = mapViewContext.workflowManager.getActiveWorkflow();
        if (workflow != null) {
            Log.d("MapViewCoordinator", "ActiveWorkflow calling onBack");
            //TODO: route input to active workflow
        }
        else{activity.finish();}
    }
    // --- FAB ---
    @Override
    public void onFabAction(int actionId) {

        Workflow workflow = mapViewContext.workflowManager.getActiveWorkflow();
        if (workflow != null) {workflow.onAction(actionId);}
        else {browsingMode.onFabAction(actionId);}
    }
    // --- MAP ---
    @Override
    public void onMapClicked(LatLng latLng) {

        Workflow workflow = mapViewContext.workflowManager.getActiveWorkflow();
        if (workflow != null) {workflow.onMapClicked(latLng);}
        else {browsingMode.onMapClicked(latLng);}
    }
    @Override
    public void onLocationSelected(LocationItem location) {

        Workflow workflow = mapViewContext.workflowManager.getActiveWorkflow();
        if (workflow != null) {workflow.onLocationSelected(location);}
        else {browsingMode.onLocationSelected(location);}
    }
    @Override
    public void onRecenterClicked() {

        Workflow workflow = mapViewContext.workflowManager.getActiveWorkflow();
        if (workflow != null) {workflow.onRecenterClicked();}
        else {browsingMode.onRecenterClicked();}
    }
    // --- SHEETS ---
    private boolean topSheetVisible;
    private boolean bottomSheetVisible;
    private boolean promptVisible;
    @Override public void onSheetShown() {
        bottomSheetVisible = true;
    }
    @Override public void onSheetHidden() {
        bottomSheetVisible = false;
    }


    //##############################
    //### UI Coordinator Actions ###
    //##############################

    // --- Toolbar ---
    @Override
    public void showToolbarCountdown(Long millis) {
        toolbarController.showCountdown(millis);
    }
    @Override
    public void hideToolbarCountdown() {
        toolbarController.hideCountdown();
    }
    // --- FAB ---
    @Override
    public void showDefaultFabMenu() {
        fabController.setMenu(R.menu.fab_defaultactions_menu);
    }
    @Override
    public void showLocationFabMenu() {
        fabController.setMenu(R.menu.fab_locationactions_menu);
    }
    @Override
    public void showFabMenu(int menuRes) {
        fabController.setMenu(menuRes);
    }

    // --- Bottom Sheet ---
    @Override
    public void showBottomSheet(Fragment fragment) {
        bottomSheetController.show(fragment);
    }
    @Override
    public void hideBottomSheet() {
        bottomSheetController.hide();
    }
    @Override
    public void showLocationDetails(LocationItem location) {

        List<PlannedTask> plans = mapViewContext.sessionManager.getPlansForLocation(location.id);

        List<TaskItem> tasks = mapViewContext.sessionManager.getTasksForLocation(location.id);

        bottomSheetController.show(LocationPlanFragment.newInstance(location, tasks, plans));
    }

    // --- Top Sheet ---
    @Override
    public void showTopSheet(Fragment fragment) {
        topSheetController.show(fragment);
    }
    @Override
    public void hideTopSheet() {
        topSheetController.hide();
    }

    // Map
    @Override
    public void previewLocation(LocationItem location) {
        mapController.previewLocation(location);
    }
    @Override
    public void focusLocation(LocationItem location) {
        mapController.focusLocation(location);
    }
    @Override
    public void moveToUserLocation() {
        mapController.moveToUserLocation();
    }
    @Override
    public boolean isCenteredOnUser() {
        return mapController.isCenteredOnUser();
    }
    @Override
    public void renderTempLocation(LatLng latLng) {
        mapController.renderTempLocation(latLng);
    }
    @Override
    public void renderTempPolygon(List<LatLng> points) {
        mapController.renderTempPolygon(points);
    }
    @Override
    public void clearTempMapObjects() {
        mapController.clearTemp();
    }
    @Override
    public void setMapGesturesEnabled(boolean enabled) {
        mapController.setMapGesturesEnabled(enabled);
    }

    // --- Helpers ---
    @Override
    public void showDayPlan() {

        DailySession session = mapViewContext.sessionManager.getCurrentSession();
        List<TaskItem> tasks = mapViewContext.sessionManager.getAllTasks();
        List<PlannedTask> plans = mapViewContext.sessionManager.getAllPlans();
        bottomSheetController.show(DayPlanFragment.newInstance(session, tasks, plans));
    }



    // --- WorkflowActions ---
    @Override
    public void startCreateLocation() {
        mapViewContext.workflowManager.startCreateLocationFlow();
    }

    @Override
    public void startCreateTask() {
        mapViewContext.workflowManager.startCreateTaskFlow();
    }

    @Override
    public void startCreatePlan() {
        mapViewContext.workflowManager.startCreatePlannedTaskFlow();
    }

    @Override
    public void startCreateCollection() {
        mapViewContext.workflowManager.startCreateCollectionFlow();
    }

    @Override
    public void startCreateTaskFromLocation(LocationItem location) {
        mapViewContext.workflowManager.startCreateTaskFromLocationFlow(location);
    }


    //TODO: MOVE TO SERVICES

    // --- Navigation ---
    @Override
    public void setNavigationSession(NavigationSession session) {

        if (!(mapController instanceof MapFragment)) {
            return;
        }

        //TODO: Remove
    }
    // --- PromptHandler ---
    @Override
    public void setPlannerPrompts(List<PlannerPrompt> prompts) {

        Log.d("MapViewCoordinator", "setPlannerPrompts() - Recieved " + prompts.size() + " prompts" );

        this.prompts.clear();

        if (prompts != null) {

            this.prompts.addAll(prompts);
        }

        tryDisplayNextPrompt();
    }

    private void tryDisplayNextPrompt() {

        if (mapViewContext.workflowManager.hasActiveWorkflow()) {
            return;
        }

        if (prompts.isEmpty()) {
            return;
        }

        PlannerPrompt prompt = prompts.get(0);
        currentPrompt = prompt;
        displayPrompt(prompt);
        prompts.remove(0);
    }
    @Override
    public boolean canDisplayPlannerPrompts() {
        //TODO: Implement logic to determine if prompts can be displayed
        return true;
    }

    private void displayPrompt(PlannerPrompt prompt) {

        if (prompt.type ==
                PlannerPrompt.Type.START_NAVIGATION) {

            String destinationId =
                    prompt.data.get("destinationId");

            LocationItem destination = mapViewContext.sessionManager.getLocationById(destinationId);

            if (destination != null) {

                LatLng userLatLng = mapController.getUserLocation();
                // request route preview
                mapViewContext.routeService.calculateRoute(
                        userLatLng,
                        new LatLng(destination.lat, destination.lng),
                        new RouteService.RouteCallback() {
                            @Override
                            public void onRouteReady(NavigationRoute route) {

                                mainHandler.post(() -> {

                                    mapController.showNavigation(route);

                                });
                            }

                            @Override
                            public void onRouteError(Exception exception) {
                                Log.e(
                                        "RouteService",
                                        "Route calculation failed",
                                        exception
                                );
                            }


                        }
                );
            }
        }

        promptVisible = true;

        PlannerPromptFragment fragment = PromptFragmentFactory.create(prompt);

        fragment.setListener(
                new PlannerPromptFragment.Listener() {

                    @Override
                    public void onPromptResult(PlannerPromptResult result) {

                        mapViewContext.promptResultListener.submitPromptResult(result);

                        dismissPrompt();
                    }
                }
        );

        topSheetController.show(fragment);
    }

    private void dismissPrompt() {

        promptVisible = false;

        currentPrompt = null;

        topSheetController.hide();

        tryDisplayNextPrompt();
    }



}