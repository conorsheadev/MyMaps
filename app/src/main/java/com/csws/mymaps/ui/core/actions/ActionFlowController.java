package com.csws.mymaps.ui.core.actions;

import com.google.android.gms.maps.model.LatLng;

public class ActionFlowController {

    private ActionFlow currentFlow;

    public void startFlow(ActionFlow flow) {
        //if (currentFlow != null) currentFlow.onCancel();
        currentFlow = flow;
        currentFlow.start();
    }

    public void onFabAction(int action) {
        if (currentFlow != null) {
            currentFlow.onAction(action);
        }
    }

    public void clearFlow() {
        currentFlow = null;
    }
}
