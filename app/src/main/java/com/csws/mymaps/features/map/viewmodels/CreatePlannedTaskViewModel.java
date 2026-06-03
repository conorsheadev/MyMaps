package com.csws.mymaps.features.map.viewmodels;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;

public class CreatePlannedTaskViewModel extends ViewModel {

    public enum Stage {
        SELECT_TASK,
        SELECT_LOCATION,
        CONFIGURE_PLAN
    }

    private final MutableLiveData<Stage> stage =
            new MutableLiveData<>(Stage.SELECT_TASK);

    private final MutableLiveData<TaskItem> selectedTask =
            new MutableLiveData<>();

    private final MutableLiveData<LocationItem> selectedLocation =
            new MutableLiveData<>();

    private final MutableLiveData<PlannedTask> draftPlan =
            new MutableLiveData<>();

    public void reset() {

        stage.setValue(Stage.SELECT_TASK);

        selectedTask.setValue(null);
        selectedLocation.setValue(null);
        draftPlan.setValue(null);
    }

    // -------------------------
    // Stage
    // -------------------------

    public LiveData<Stage> getStage() {
        return stage;
    }

    public void setStage(Stage stage) {
        this.stage.setValue(stage);
    }

    // -------------------------
    // Task
    // -------------------------

    public void setTask(TaskItem task) {
        selectedTask.setValue(task);
    }

    public LiveData<TaskItem> getTask() {
        return selectedTask;
    }

    public TaskItem getCurrentTask() {
        return selectedTask.getValue();
    }

    // -------------------------
    // Location
    // -------------------------

    public void setLocation(LocationItem location) {
        selectedLocation.setValue(location);
    }

    public LiveData<LocationItem> getLocation() {
        return selectedLocation;
    }

    public LocationItem getCurrentLocation() {
        return selectedLocation.getValue();
    }

    // -------------------------
    // Plan
    // -------------------------

    public void setDraftPlan(PlannedTask plan) {
        draftPlan.setValue(plan);
    }

    public LiveData<PlannedTask> getDraftPlan() {
        return draftPlan;
    }

    public PlannedTask getCurrentPlan() {
        return draftPlan.getValue();
    }
}