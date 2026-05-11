package com.csws.mymaps.features.map.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;

public class CreateTaskViewModel extends ViewModel {

    public enum Stage {
        SELECT_LOCATION,
        CONFIGURE_TASK,
        CONFIGURE_PLAN
    }

    private final MutableLiveData<Stage> stage = new MutableLiveData<>(Stage.SELECT_LOCATION);
    private final MutableLiveData<LocationItem> selectedLocation = new MutableLiveData<>();
    private final MutableLiveData<TaskItem> draftTask = new MutableLiveData<>();
    private final MutableLiveData<PlannedTask> draftPlan = new MutableLiveData<>();

    // --- Stage ---
    public LiveData<Stage> getStage() {
        return stage;
    }
    public void setStage(Stage newStage) {
        stage.setValue(newStage);
    }
    public void reset() {
        stage.setValue(Stage.SELECT_LOCATION);
        selectedLocation.setValue(null);
        draftTask.setValue(null);
        draftPlan.setValue(null);
    }


    // --- SelectedLocation ---
    public void setLocation(LocationItem location) {
        selectedLocation.setValue(location);
    }
    public LiveData<LocationItem> getLocation() {
        return selectedLocation;
    }
    public LocationItem getCurrentLocation() {
        return selectedLocation.getValue();
    }

    // --- Task Draft ---
    public void setDraftTask(TaskItem task) {
        draftTask.setValue(task);
    }
    public LiveData<TaskItem> getDraftTask() {
        return draftTask;
    }
    public TaskItem getCurrentTask() {
        return draftTask.getValue();
    }

    // --- Planned Task ---
    public void setDraftPlan(PlannedTask plannedTask) {
        draftPlan.setValue(plannedTask);
    }
    public LiveData<PlannedTask> getDraftPlan() {
        return draftPlan;
    }
    public PlannedTask getCurrentPlan() {
        return draftPlan.getValue();
    }


}