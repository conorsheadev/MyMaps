package com.csws.mymaps.features.map.workflow.workflows;

import com.csws.mymaps.domain.session.SessionStartType;
import com.csws.mymaps.features.map.ui.top_sheets.SessionStartFragment;
import com.csws.mymaps.features.map.coordinators.FlowContext;
import com.csws.mymaps.features.map.workflow.BaseWorkflow;

public class InitialiseSessionWorkflow extends BaseWorkflow implements SessionStartFragment.Listener {

    private final SessionStartFragment fragment;

    public InitialiseSessionWorkflow(FlowContext context) {

        super(context);

        fragment = new SessionStartFragment();

        fragment.setListener(this);
    }

    @Override
    public void start() {
        context.topSheetController.show(fragment);
    }

    @Override
    public void stop() {
        context.topSheetController.hide();
    }

    @Override
    public void onSessionStartSelected(SessionStartType startType) {

        if (startType != SessionStartType.CONTINUED) {

            context.sessionViewModel.createSession(startType);

        } else {

            context.sessionViewModel.loadLatestSession();
        }

        context.workflowNavigator.finishWorkflow();
    }
}