package com.csws.mymaps.core.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csws.mymaps.data.PlannedTaskRepository;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlannedTaskViewModel extends AndroidViewModel {

    //TODO: TEST & Clean Up
    private final PlannedTaskRepository repository;

    private final MutableLiveData<List<PlannedTask>> plannedTasks = new MutableLiveData<>();

    public PlannedTaskViewModel(@NonNull Application application) {
        super(application);
        repository = new PlannedTaskRepository(application);
        loadPlannedTasks();
    }
    private void loadPlannedTasks() {
        List<PlannedTask> loaded = repository.loadPlannedTasks();
        plannedTasks.setValue(loaded);
    }
    public LiveData<List<PlannedTask>> getPlannedTasks() {
        return plannedTasks;
    }

    // --- ADD/REMOVE funcs ---
    public void addPlannedTask(PlannedTask task) {

        List<PlannedTask> current = plannedTasks.getValue();
        if (current == null) {
            current = new ArrayList<>();
        }

        current.add(task);

        repository.savePlannedTasks(current);

        plannedTasks.setValue(current);
    }

    public void removePlannedTask(String plannedTaskId) {

        List<PlannedTask> current = plannedTasks.getValue();

        if (current == null) {
            return;
        }

        current.removeIf(task -> task.id.equals(plannedTaskId));
        repository.savePlannedTasks(current);
        plannedTasks.setValue(current);
    }

    // --- Queries ---
    public List<PlannedTask> getTasksForDate(String date) {

        List<PlannedTask> current = plannedTasks.getValue();

        List<PlannedTask> result = new ArrayList<>();

        if (current == null) {
            return result;
        }

        for (PlannedTask task : current) {

            if (date.equals(task.date)) {
                result.add(task);
            }
        }

        return result;
    }

    public List<PlannedTask> getPlansForTask(String taskId) {

        List<PlannedTask> current = plannedTasks.getValue();
        List<PlannedTask> result = new ArrayList<>();

        if (current == null) {
            return result;
        }
        for (PlannedTask task : current) {

            if (task.taskId.equals(taskId)) {
                result.add(task);
            }
        }

        return result;
    }
    public List<PlannedTask> getPlansForTasks(List<TaskItem> tasks) {

        List<PlannedTask> result = new ArrayList<>();
        if (tasks == null || tasks.isEmpty()) {
            return result;
        }

        List<PlannedTask> current = plannedTasks.getValue();

        if (current == null) {
            return result;
        }

        Set<String> taskIds = new HashSet<>();

        for (TaskItem task : tasks) {
            taskIds.add(task.id);
        }

        for (PlannedTask plannedTask : current) {

            if (taskIds.contains(plannedTask.taskId)) {
                result.add(plannedTask);
            }
        }

        return result;
    }

    public List<PlannedTask> getPlansForLocation(String locationId) {

        List<PlannedTask> current = plannedTasks.getValue();
        List<PlannedTask> result = new ArrayList<>();

        if (current == null) {
            return result;
        }

        for (PlannedTask plan : current) {

            if (locationId.equals(plan.locationId)) {
                result.add(plan);
            }
        }

        return result;
    }

    public List<PlannedTask> getPlansForLocationAndDate(
            String locationId,
            String date
    ) {

        List<PlannedTask> current = plannedTasks.getValue();
        List<PlannedTask> result = new ArrayList<>();

        if (current == null) {
            return result;
        }

        for (PlannedTask plan : current) {

            if (locationId.equals(plan.locationId)
                    && date.equals(plan.date)) {

                result.add(plan);
            }
        }

        return result;
    }
}
