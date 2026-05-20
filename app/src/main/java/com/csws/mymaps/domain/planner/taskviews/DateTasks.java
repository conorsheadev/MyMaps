package com.csws.mymaps.domain.planner.taskviews;

import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;

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
