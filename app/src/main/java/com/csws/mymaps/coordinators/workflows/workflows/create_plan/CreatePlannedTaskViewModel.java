package com.csws.mymaps.coordinators.workflows.workflows.create_plan;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.csws.mymaps.core.models.locations.LocationItem;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.core.models.tasks.TaskItem;

public class CreatePlannedTaskViewModel extends ViewModel {

    private final MutableLiveData<TaskItem> selectedTask = new MutableLiveData<>();
    private final MutableLiveData<LocationItem> selectedLocation = new MutableLiveData<>();
    private final MutableLiveData<PlannedTask> draftPlan = new MutableLiveData<>();

    public void reset() {

        selectedTask.setValue(null);
        selectedLocation.setValue(null);
        draftPlan.setValue(null);
    }

    // Task
    public void setTask(TaskItem task) {
        selectedTask.setValue(task);
    }

    public TaskItem getCurrentTask() {
        return selectedTask.getValue();
    }

    // Location
    public void setLocation(LocationItem location) {
        selectedLocation.setValue(location);
    }

    public LocationItem getCurrentLocation() {
        return selectedLocation.getValue();
    }

    // Plan
    public void setDraftPlan(PlannedTask plan) {
        draftPlan.setValue(plan);
    }

    public PlannedTask getCurrentPlan() {
        return draftPlan.getValue();
    }
}