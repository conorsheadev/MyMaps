package com.csws.mymaps.core.flow;

import com.csws.mymaps.features.map.flows.CreateLocationFlow;
import com.csws.mymaps.features.map.flows.CreateTaskFlow;
import com.csws.mymaps.features.map.flows.DefaultFlow;
import com.csws.mymaps.core.flow.interfaces.ActivityActions;
import com.csws.mymaps.core.flow.interfaces.MapActions;
import com.csws.mymaps.features.map.viewmodels.CreateLocationViewModel;
import com.csws.mymaps.features.map.viewmodels.CreateTaskViewModel;
import com.csws.mymaps.features.map.viewmodels.DefaultFlowViewModel;

public class ActionFlowFactory {
    private final ActivityActions activityActions;
    private final MapActions mapActions;

    public ActionFlowFactory(ActivityActions activityActions, MapActions mapActions) {
        this.activityActions = activityActions;
        this.mapActions = mapActions;
    }

    public DefaultFlow createDefaultFlow(DefaultFlowViewModel vm){
        return new DefaultFlow(vm, activityActions, mapActions);
    }

    public CreateLocationFlow createLocationFlow(CreateLocationViewModel vm){
        return new CreateLocationFlow(vm, activityActions, mapActions);
    }

    public CreateTaskFlow createTaskFlow(CreateTaskViewModel vm){
        return new CreateTaskFlow(vm, activityActions, mapActions);
    }

}
