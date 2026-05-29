package com.csws.mymaps.core.flow.interfaces.coordinator_interfaces;

import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;

public interface SessionActions {
    void createNewLocation(LocationItem locationItem);
    void createNewTask(TaskItem taskItem);
    void createNewPlannedTask(PlannedTask plannedTask);

}
