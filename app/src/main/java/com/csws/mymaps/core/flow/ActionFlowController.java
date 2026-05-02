package com.csws.mymaps.core.flow;

public class ActionFlowController {

    private ActionFlow currentFlow; public ActionFlow getCurrentFlow(){return currentFlow;}

    public void startFlow(ActionFlow flow) {
        if (currentFlow != null) currentFlow.onCancel();
        currentFlow = flow;
        currentFlow.start();
    }
}
