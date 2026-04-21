package com.csws.mymaps.viewmodel;

import com.csws.mymaps.model.tasks.TaskItem;
import com.csws.mymaps.data.TaskRepository;

import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<TaskItem> tasks;
    private final TaskRepository repository;

    public TaskManager(TaskRepository repository) {
        this.repository = repository;
        this.tasks = repository.loadTasks();
    }

    public List<TaskItem> getTasksForLocation(String locationId) {
        List<TaskItem> result = new ArrayList<>();
        for (TaskItem t : tasks) {
            if (t.locationId.equals(locationId)) {
                result.add(t);
            }
        }
        return result;
    }

    public void addTask(TaskItem task) {
        tasks.add(task);
        repository.saveTasks(tasks);
    }
}
