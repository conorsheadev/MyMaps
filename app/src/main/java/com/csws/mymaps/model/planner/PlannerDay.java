package com.csws.mymaps.model.planner;

import com.csws.mymaps.model.tasks.TaskItem;

import java.time.LocalDate;
import java.util.List;

public class PlannerDay {
    public LocalDate date;
    public List<TaskItem> tasks;

    public PlannerDay(LocalDate date, List<TaskItem> tasks) {
        this.date = date;
        this.tasks = tasks;
    }
}
