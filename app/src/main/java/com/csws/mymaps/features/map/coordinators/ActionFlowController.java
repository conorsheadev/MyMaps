package com.csws.mymaps.features.map.coordinators;

import com.csws.mymaps.core.flow.ActionFlow;

public class ActionFlowController {

    private ActionFlow currentFlow; public ActionFlow getCurrentFlow(){return currentFlow;}

    public void startFlow(ActionFlow flow) {
        if (currentFlow != null) currentFlow.onCancel();
        currentFlow = flow;
        currentFlow.start();
    }
}
