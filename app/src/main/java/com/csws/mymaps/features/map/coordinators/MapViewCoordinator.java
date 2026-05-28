package com.csws.mymaps.features.map.coordinators;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.csws.mymaps.core.flow.interfaces.SessionActions;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.features.map.controllers.map.MapFragment;
import com.csws.mymaps.features.map.workflow.workflows.InitialiseSessionWorkflow;

import java.util.List;

public class MapViewCoordinator implements SessionActions {
    //Debugging
    private static final String TAG = "MapViewCoordinator";

    // --- Flow System ---
    private final FlowContext flowContext;

    public final WorkflowController flowController;

    public MapViewCoordinator(AppCompatActivity activity, FlowContext flowContext){
        this.flowContext = flowContext;
        this.flowController = new WorkflowController(activity, flowContext);
    }

    // --- Observers ---
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    private Runnable pendingRefresh;
    public void observe(LifecycleOwner owner) {

        Log.d(TAG, "observe START");

        flowContext.locationViewModel.getLocations().observe(owner, locations -> {

            Log.d(TAG, "Locations observer fired size=" + (locations != null ? locations.size() : -1));

            if (flowContext.mapActions instanceof MapFragment) {
                ((MapFragment) flowContext.mapActions).displayLocations(locations);
            }
        });

        Observer<Object> refreshTasks = o -> {

            if (pendingRefresh != null) {
                mainHandler.removeCallbacks(pendingRefresh);
            }

            pendingRefresh = () -> {

                List<TaskItem> tasks = flowContext.taskViewModel.getTasks().getValue();
                List<PlannedTask> planned = flowContext.plannedTaskViewModel.getPlannedTasks().getValue();

                ((MapFragment) flowContext.mapActions).setTasks(tasks, planned);
            };

            mainHandler.postDelayed(pendingRefresh, 300);
        };

        flowContext.taskViewModel.getTasks().observe(owner, refreshTasks);
        flowContext.plannedTaskViewModel.getPlannedTasks().observe(owner, refreshTasks);

        Log.d(TAG, "observe END");
    }

    // --- Start ---
    public void start() {

        Log.d(TAG, "start()");

        flowController.cancelCurrentFlow();

        boolean hasSession = flowContext.sessionViewModel.hasSessionToday();

        Log.d(TAG, "hasSessionToday = " + hasSession);
        if(hasSession) {

            Log.d(TAG, "Loading existing session");

            flowContext.sessionViewModel.loadTodaySession();

            Log.d(TAG, "Starting default flow");

            flowController.startDefaultFlow();

        } else {

            Log.d(TAG, "Starting InitialiseSessionFlow");

            flowController.startWorkflow(new InitialiseSessionWorkflow(flowContext));
        }
    }



    // --- SessionActions ---
    @Override
    public void createNewLocation(LocationItem locationItem) {

        flowContext.locationViewModel.addLocation(locationItem);
    }

    @Override
    public void createNewTask(TaskItem taskItem) {

        flowContext.taskViewModel.addTask(taskItem);
    }

    @Override
    public void createNewPlannedTask(PlannedTask plannedTask) {

        flowContext.plannedTaskViewModel.addPlannedTask(plannedTask);
    }
}