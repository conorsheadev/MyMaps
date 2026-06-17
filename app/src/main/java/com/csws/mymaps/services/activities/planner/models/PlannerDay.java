package com.csws.mymaps.services.activities.planner.models;

import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.core.models.tasks.TaskItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PlannerDay {
    public String date;
    public List<TaskItem> tasks;
    public List<PlannedTask> plannedTasks;

    public PlannerDay(String date) {
        this.date = date;
        this.tasks = new ArrayList<>();
    }
    public PlannerDay(String date, List<TaskItem> tasks, List<PlannedTask> plannedTasks) {
        this.date = date;
        this.tasks = tasks;
        this.plannedTasks = plannedTasks;
    }

    public String getDayName() {

        LocalDate localDate = LocalDate.parse(date);
        return localDate.getDayOfWeek().toString();
    }

    public String getFormattedDate() {
        //TODO: Setup DateTime Utils for Formatting
        LocalDate localDate = LocalDate.parse(date);
        return localDate.format(DateTimeFormatter.ofPattern("dd MMM"));
    }

    public List<TaskItem> getTasks() {
        return tasks;
    }
    public List<PlannedTask> getPlannedTasks() {
        return plannedTasks;
    }
}
