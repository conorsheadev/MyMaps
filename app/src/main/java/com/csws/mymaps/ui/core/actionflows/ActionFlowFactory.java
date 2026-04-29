package com.csws.mymaps.ui.core.actionflows;

import com.csws.mymaps.ui.core.actionflows.flows.CreateLocationFlow;
import com.csws.mymaps.ui.core.actionflows.flows.DefaultFlow;
import com.csws.mymaps.ui.core.actionflows.interfaces.ActivityActions;
import com.csws.mymaps.ui.core.actionflows.interfaces.MapActions;
import com.csws.mymaps.viewmodel.flows.CreateLocationViewModel;
import com.csws.mymaps.viewmodel.flows.DefaultFlowViewModel;

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

}
