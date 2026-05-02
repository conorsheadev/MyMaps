package com.csws.mymaps.domain.planner;

import com.csws.mymaps.domain.tasks.TaskItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PlannerDay {
    public LocalDate date;
    public List<TaskItem> tasks;

    public PlannerDay(LocalDate date, List<TaskItem> tasks) {
        this.date = date;
        this.tasks = tasks;
    }

    public String getDayName() {
        return date.getDayOfWeek().toString();
    }
    public String getFormattedDate() {
        return date.format(DateTimeFormatter.ofPattern("dd MMM"));
    }
    public List<TaskItem> getTasks() {return tasks;}
}
