package com.csws.mymaps.ui.core.actions;

import com.csws.mymaps.ui.core.actions.flows.CreateLocationFlow;
import com.csws.mymaps.ui.map.ActivityActions;
import com.csws.mymaps.ui.map.MapActions;
import com.csws.mymaps.viewmodel.flows.CreateLocationViewModel;

public class ActionFlowFactory {
    private final ActivityActions activityActions;
    private final MapActions mapActions;

    public ActionFlowFactory(ActivityActions activityActions, MapActions mapActions) {
        this.activityActions = activityActions;
        this.mapActions = mapActions;
    }

    public CreateLocationFlow createLocationFlow(CreateLocationViewModel vm){
        return new CreateLocationFlow(vm, activityActions, mapActions);
    }

}
