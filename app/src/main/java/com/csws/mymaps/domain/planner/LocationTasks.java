package com.csws.mymaps.domain.planner;

import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.tasks.TaskItem;

import java.util.List;

public class LocationTasks {
    public final LocationItem location;
    public final List<TaskItem> tasks;

    public LocationTasks(LocationItem location, List<TaskItem> tasks) {
        this.location = location;
        this.tasks = tasks;
    }
}
