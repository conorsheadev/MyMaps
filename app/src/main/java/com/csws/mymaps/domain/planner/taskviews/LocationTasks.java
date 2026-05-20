package com.csws.mymaps.domain.planner.taskviews;

import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;

import java.util.List;
import java.util.Map;

public class LocationTasks {

    public final LocationItem location;
    public final List<PlannedTask> plannedTasks;
    public Map<String, TaskItem> tasks; // <taskId, task>

    public LocationTasks(LocationItem location, List<PlannedTask> plannedTasks, Map<String, TaskItem> tasks) {

        this.location = location;
        this.plannedTasks = plannedTasks;
        this.tasks = tasks;
    }
}
