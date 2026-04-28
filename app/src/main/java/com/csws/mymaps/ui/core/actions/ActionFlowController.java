package com.csws.mymaps.ui.core.actions;

import com.csws.mymaps.ui.core.actions.flows.DefaultFlow;
import com.csws.mymaps.ui.map.ActivityActions;
import com.google.android.gms.maps.model.LatLng;

public class ActionFlowController {

    private ActionFlow currentFlow; public ActionFlow getCurrentFlow(){return currentFlow;}

    public void startFlow(ActionFlow flow) {
        if (currentFlow != null) currentFlow.onCancel();
        currentFlow = flow;
        currentFlow.start();
    }

    public void onFabAction(int action) {
        if (currentFlow != null) {
            currentFlow.onAction(action);
        }
    }

    public void endFlow(ActivityActions actions) {
        currentFlow = new DefaultFlow(actions);
    }
}
