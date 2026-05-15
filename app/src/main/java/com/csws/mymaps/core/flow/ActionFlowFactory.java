package com.csws.mymaps.core.flow;

import com.csws.mymaps.core.flow.interfaces.FlowActions;
import com.csws.mymaps.core.flow.interfaces.SessionActions;
import com.csws.mymaps.core.viewmodel.LocationViewModel;
import com.csws.mymaps.core.viewmodel.TaskViewModel;
import com.csws.mymaps.features.map.coordinators.flows.CreateLocationFlow;
import com.csws.mymaps.features.map.coordinators.flows.CreateTaskFlow;
import com.csws.mymaps.features.map.coordinators.flows.DefaultFlow;
import com.csws.mymaps.core.flow.interfaces.ActivityActions;
import com.csws.mymaps.core.flow.interfaces.MapActions;
import com.csws.mymaps.features.map.viewmodels.CreateLocationViewModel;
import com.csws.mymaps.features.map.viewmodels.CreateTaskViewModel;
import com.csws.mymaps.features.map.viewmodels.DefaultFlowViewModel;
import com.csws.mymaps.core.viewmodel.PlannedTaskViewModel;
import com.csws.mymaps.features.map.viewmodels.SessionViewModel;

public class ActionFlowFactory {
    private final TaskViewModel taskViewModel;
    private final PlannedTaskViewModel plannedTaskViewModel;
    private final LocationViewModel locationViewModel;
    private final SessionViewModel sessionViewModel;

    private final ActivityActions activityActions;
    private final SessionActions sessionActions;
    private final FlowActions flowActions;
    private final MapActions mapActions;

    public ActionFlowFactory(TaskViewModel taskViewModel, PlannedTaskViewModel plannedTaskViewModel, LocationViewModel locationViewModel, SessionViewModel sessionViewModel, ActivityActions activityActions, SessionActions sessionActions, FlowActions flowActions, MapActions mapActions) {
        this.taskViewModel = taskViewModel;
        this.plannedTaskViewModel = plannedTaskViewModel;
        this.locationViewModel = locationViewModel;
        this.sessionViewModel = sessionViewModel;

        this.activityActions = activityActions;
        this.sessionActions = sessionActions;
        this.flowActions = flowActions;
        this.mapActions = mapActions;
    }

    public DefaultFlow createDefaultFlow(DefaultFlowViewModel vm){
        return new DefaultFlow(vm, taskViewModel, plannedTaskViewModel, locationViewModel, sessionViewModel, activityActions, sessionActions, flowActions, mapActions);
    }

    public CreateLocationFlow createLocationFlow(CreateLocationViewModel vm){
        return new CreateLocationFlow(vm, activityActions, sessionActions, flowActions, mapActions);
    }

    public CreateTaskFlow createTaskFlow(CreateTaskViewModel vm){
        return new CreateTaskFlow(vm, activityActions, sessionActions, flowActions, mapActions);
    }

}
