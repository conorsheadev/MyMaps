package com.csws.mymaps.domain.planner.taskviews;

import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;

import java.util.List;
import java.util.Map;

public class TypeTasks {

    public final String type;
    public final List<PlannedTask> plannedTasks;
    public final Map<String, TaskItem> tasks;

    public TypeTasks(String type, List<PlannedTask> plannedTasks, Map<String, TaskItem> tasks) {

        this.type = type;
        this.plannedTasks = plannedTasks;
        this.tasks = tasks;
    }
}
