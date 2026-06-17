package com.csws.mymaps.services.entity_creation;

import com.csws.mymaps.core.contracts.services.EntityCreationService;
import com.csws.mymaps.core.models.locations.LocationItem;
import com.csws.mymaps.core.viewmodels.locations.LocationViewModel;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.core.viewmodels.plans.PlannedTaskViewModel;
import com.csws.mymaps.core.models.tasks.TaskCollection;
import com.csws.mymaps.core.models.tasks.TaskItem;
import com.csws.mymaps.core.viewmodels.collections.TaskCollectionViewModel;
import com.csws.mymaps.core.viewmodels.tasks.TaskViewModel;

public class DefaultEntityCreationService implements EntityCreationService {

    private final LocationViewModel locationViewModel;
    private final TaskViewModel taskViewModel;
    private final PlannedTaskViewModel plannedTaskViewModel;
    private final TaskCollectionViewModel taskCollectionViewModel;

    public DefaultEntityCreationService(
            LocationViewModel locationViewModel,
            TaskViewModel taskViewModel,
            PlannedTaskViewModel plannedTaskViewModel,
            TaskCollectionViewModel taskCollectionViewModel) {

        this.locationViewModel = locationViewModel;
        this.taskViewModel = taskViewModel;
        this.plannedTaskViewModel = plannedTaskViewModel;
        this.taskCollectionViewModel = taskCollectionViewModel;
    }

    @Override
    public void createLocation(LocationItem location) {
        locationViewModel.addLocation(location);
    }

    @Override
    public void createTask(TaskItem task) {
        taskViewModel.addTask(task);
    }

    @Override
    public void createPlannedTask(PlannedTask task) {
        plannedTaskViewModel.addPlannedTask(task);
    }

    @Override
    public void createTaskCollection(TaskCollection collection) {
        taskCollectionViewModel.addCollection(collection);
    }
}