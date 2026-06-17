package com.csws.mymaps.services.activities.planner.models;

import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.core.models.tasks.TaskItem;

import java.util.List;
import java.util.Map;

public class DateTasks {

    public final String date;
    public final List<PlannedTask> plannedTasks;
    public final Map<String, TaskItem> tasks;

    public DateTasks(String date, List<PlannedTask> plannedTasks, Map<String, TaskItem> tasks) {

        this.date = date;
        this.plannedTasks = plannedTasks;
        this.tasks = tasks;
    }
}
