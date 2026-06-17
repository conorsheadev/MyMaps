package com.csws.mymaps.core.contracts.services;

import com.csws.mymaps.core.models.locations.LocationItem;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.core.models.tasks.TaskCollection;
import com.csws.mymaps.core.models.tasks.TaskItem;

public interface EntityCreationService {
    void createLocation(LocationItem locationItem);
    void createTask(TaskItem taskItem);
    void createPlannedTask(PlannedTask plannedTask);
    void createTaskCollection(TaskCollection collection);
}
