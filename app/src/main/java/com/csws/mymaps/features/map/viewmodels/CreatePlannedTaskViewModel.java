package com.csws.mymaps.features.map.viewmodels;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;

public class CreatePlannedTaskViewModel extends ViewModel {

    private final MutableLiveData<TaskItem> selectedTask = new MutableLiveData<>();

    private final MutableLiveData<PlannedTask> draftPlan = new MutableLiveData<>();

    public void reset() {

        selectedTask.setValue(null);
        draftPlan.setValue(null);
    }

    public void setTask(TaskItem task) {
        selectedTask.setValue(task);
    }

    public TaskItem getCurrentTask() {
        return selectedTask.getValue();
    }

    public void setDraftPlan(PlannedTask plan) {
        draftPlan.setValue(plan);
    }

    public PlannedTask getCurrentPlan() {
        return draftPlan.getValue();
    }
}