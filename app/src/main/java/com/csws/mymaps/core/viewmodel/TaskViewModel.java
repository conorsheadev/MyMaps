package com.csws.mymaps.core.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.data.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    public TaskItem getTask(String taskId) {

        List<TaskItem> current = tasks.getValue();

        if (current == null) {
            return null;
        }

        for (TaskItem task : current) {

            if (task.id.equals(taskId)) {
                return task;
            }
        }

        return null;
    }
    public List<TaskItem> getTasksByIds(Set<String> taskIds) {

        List<TaskItem> current = tasks.getValue();
        List<TaskItem> result = new ArrayList<>();

        if (current == null) {
            return result;
        }

        for (TaskItem task : current) {

            if (taskIds.contains(task.id)) {
                result.add(task);
            }
        }

        return result;
    }


}
