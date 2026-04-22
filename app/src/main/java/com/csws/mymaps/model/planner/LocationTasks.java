package com.csws.mymaps.model.planner;

import com.csws.mymaps.model.locations.LocationItem;
import com.csws.mymaps.model.tasks.TaskItem;

import java.util.List;

public class LocationTasks {
    public final LocationItem location;
    public final List<TaskItem> tasks;

    public LocationTasks(LocationItem location, List<TaskItem> tasks) {
        this.location = location;
        this.tasks = tasks;
    }
}
