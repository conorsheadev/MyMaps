package com.csws.mymaps.features.map.coordinators;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.csws.mymaps.core.flow.interfaces.coordinator_interfaces.PromptHandler;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.features.map.controllers.map.MapFragment;
import com.csws.mymaps.features.map.ui.top_sheets.PlannerPromptFragment;
import com.csws.mymaps.domain.planner.engine.prompts.PlannerPrompt;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MapViewCoordinator implements PromptHandler {
    //Debugging
    private static final String TAG = "MapViewCoordinator";

    // --- Flow System ---
    private final MapViewContext mapViewContext;
    private final Queue<PlannerPrompt> promptQueue = new LinkedList<>();
    private boolean promptVisible = false;

    public MapViewCoordinator(MapViewContext mapViewContext){
        this.mapViewContext = mapViewContext;
    }

    // --- Observers ---
    private final Handler mainHandler = new Handler(Looper.getMainLooper());
    private Runnable pendingRefresh;
    public void observe(LifecycleOwner owner) {

        Log.d(TAG, "observe START");

        mapViewContext.locationViewModel.getLocations().observe(owner, locations -> {

            Log.d(TAG, "Locations observer fired size=" + (locations != null ? locations.size() : -1));

            if (mapViewContext.mapActions instanceof MapFragment) {
                ((MapFragment) mapViewContext.mapActions).displayLocations(locations);
            }
        });

        Observer<Object> refreshTasks = o -> {

            if (pendingRefresh != null) {
                mainHandler.removeCallbacks(pendingRefresh);
            }

            pendingRefresh = () -> {

                List<TaskItem> tasks = mapViewContext.taskViewModel.getTasks().getValue();
                List<PlannedTask> planned = mapViewContext.plannedTaskViewModel.getPlannedTasks().getValue();

                ((MapFragment) mapViewContext.mapActions).setTasks(tasks, planned);
            };

            mainHandler.postDelayed(pendingRefresh, 300);
        };

        mapViewContext.taskViewModel.getTasks().observe(owner, refreshTasks);
        mapViewContext.plannedTaskViewModel.getPlannedTasks().observe(owner, refreshTasks);

        Log.d(TAG, "observe END");
    }

    // --- Start ---
    public void start() {

        Log.d(TAG, "start()");


        boolean hasSession = mapViewContext.sessionViewModel.hasSessionToday();

        Log.d(TAG, "hasSessionToday = " + hasSession);
        if(hasSession) {

            Log.d(TAG, "Loading existing session");

            mapViewContext.sessionViewModel.loadTodaySession();

        } else {

            Log.d(TAG, "Starting InitialiseSessionFlow");

        }
    }

    // --- PromptHandler ---
    @Override
    public void showPlannerPrompt(PlannerPrompt prompt) {

        promptQueue.add(prompt);

        tryDisplayNextPrompt();
    }

    private void tryDisplayNextPrompt() {

        if (promptVisible) {
            return;
        }

        if (mapViewContext.workflowNavigator.hasActiveWorkflow()) {

            return;
        }

        PlannerPrompt prompt = promptQueue.poll();

        if (prompt == null) {
            return;
        }

        displayPrompt(prompt);
    }
    @Override
    public boolean canDisplayPlannerPrompts() {
        //TODO: Implement logic to determine if prompts can be displayed
        return true;
    }

    private void displayPrompt(PlannerPrompt prompt) {

        promptVisible = true;

        PlannerPromptFragment fragment = PlannerPromptFragment.newInstance(prompt);

        fragment.setListener(
                new PlannerPromptFragment.Listener() {

                    @Override
                    public void onPromptDismissed() {

                        dismissPrompt();
                    }

                    @Override
                    public void onPromptAction(PlannerPrompt prompt) {

                        handlePromptAction(prompt);

                        dismissPrompt();
                    }
                }
        );

        mapViewContext.topSheetController.show(fragment);
    }

    private void dismissPrompt() {

        promptVisible = false;

        mapViewContext.topSheetController.hide();

        tryDisplayNextPrompt();
    }

    private void handlePromptAction(PlannerPrompt prompt) {

        switch (prompt.type) {

            case PREPARE_TO_LEAVE:

                Log.d(TAG, "PREPARE_TO_LEAVE selected");

                break;

            case LEAVE_NOW:

                Log.d(TAG, "LEAVE_NOW selected");

                break;
        }
    }
}