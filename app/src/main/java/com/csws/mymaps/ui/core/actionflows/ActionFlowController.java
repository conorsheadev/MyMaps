package com.csws.mymaps.ui.core.actionflows;

import com.csws.mymaps.ui.core.actionflows.flows.DefaultFlow;
import com.csws.mymaps.ui.core.actionflows.interfaces.ActivityActions;

public class ActionFlowController {

    private ActionFlow currentFlow; public ActionFlow getCurrentFlow(){return currentFlow;}

    public void startFlow(ActionFlow flow) {
        if (currentFlow != null) currentFlow.onCancel();
        currentFlow = flow;
        currentFlow.start();
    }
}
