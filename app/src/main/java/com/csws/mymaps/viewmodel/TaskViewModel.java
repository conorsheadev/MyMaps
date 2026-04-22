package com.csws.mymaps.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csws.mymaps.model.tasks.TaskItem;
import com.csws.mymaps.data.TaskRepository;

import java.util.ArrayList;
import java.util.List;

public class TaskViewModel extends AndroidViewModel {
    private final TaskRepository repository;
    private final MutableLiveData<List<TaskItem>> tasks = new MutableLiveData<>();

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskRepository(application);
        loadTasks();
    }

    private void loadTasks() {
        List<TaskItem> loaded = repository.loadTasks();
        tasks.setValue(loaded);
    }

    public LiveData<List<TaskItem>> getTasks() {
        return tasks;
    }

    public void addTask(TaskItem task) {
        List<TaskItem> current = tasks.getValue();
        if (current == null) current = new ArrayList<>();

        current.add(task);
        repository.saveTasks(current);
        tasks.setValue(current);
    }
    public void removeTask(String taskId) {
        List<TaskItem> current = tasks.getValue();
        if (current == null) return;

        current.removeIf(task -> task.id.equals(taskId));
        repository.saveTasks(current);
        tasks.setValue(current);
    }

    public List<TaskItem> getTasksForLocation(String locationId) {
        List<TaskItem> current = tasks.getValue();
        if (current == null) return new ArrayList<>();

        List<TaskItem> result = new ArrayList<>();
        for (TaskItem task : current) {
            if (task.locationId.equals(locationId)) {
                result.add(task);
            }
        }
        return result;
    }


}
