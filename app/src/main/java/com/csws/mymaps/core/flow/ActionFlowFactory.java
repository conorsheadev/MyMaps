package com.csws.mymaps.core.flow;

import com.csws.mymaps.core.viewmodel.LocationViewModel;
import com.csws.mymaps.core.viewmodel.TaskViewModel;
import com.csws.mymaps.features.map.flows.CreateLocationFlow;
import com.csws.mymaps.features.map.flows.CreateTaskFlow;
import com.csws.mymaps.features.map.flows.DefaultFlow;
import com.csws.mymaps.core.flow.interfaces.ActivityActions;
import com.csws.mymaps.core.flow.interfaces.MapActions;
import com.csws.mymaps.features.map.viewmodels.CreateLocationViewModel;
import com.csws.mymaps.features.map.viewmodels.CreateTaskViewModel;
import com.csws.mymaps.features.map.viewmodels.DefaultFlowViewModel;
import com.csws.mymaps.core.viewmodel.PlannedTaskViewModel;

public class ActionFlowFactory {
    private final TaskViewModel taskViewModel;
    private final PlannedTaskViewModel plannedTaskViewModel;
    private final LocationViewModel locationViewModel;
    private final ActivityActions activityActions;
    private final MapActions mapActions;

    public ActionFlowFactory(TaskViewModel taskViewModel, PlannedTaskViewModel plannedTaskViewModel, LocationViewModel locationViewModel, ActivityActions activityActions, MapActions mapActions) {
        this.taskViewModel = taskViewModel;
        this.plannedTaskViewModel = plannedTaskViewModel;
        this.locationViewModel = locationViewModel;
        this.activityActions = activityActions;
        this.mapActions = mapActions;
    }

    public DefaultFlow createDefaultFlow(DefaultFlowViewModel vm){
        return new DefaultFlow(vm, taskViewModel, plannedTaskViewModel, locationViewModel, activityActions, mapActions);
    }

    public CreateLocationFlow createLocationFlow(CreateLocationViewModel vm){
        return new CreateLocationFlow(vm, activityActions, mapActions);
    }

    public CreateTaskFlow createTaskFlow(CreateTaskViewModel vm){
        return new CreateTaskFlow(vm, activityActions, mapActions);
    }

}
