package com.csws.mymaps.coordinators.session.workflows;

import com.csws.mymaps.core.models.sessions.SessionStartType;
import com.csws.mymaps.activities.map.fragments.top_sheets.SessionStartFragment;
import com.csws.mymaps.coordinators.CoordinatorContext;
import com.csws.mymaps.coordinators.workflows.BaseWorkflow;

public class InitialiseSessionWorkflow extends BaseWorkflow implements SessionStartFragment.Listener {

    private final SessionStartFragment fragment;

    public InitialiseSessionWorkflow(CoordinatorContext context) {

        super(context);

        fragment = new SessionStartFragment();

        fragment.setListener(this);
    }

    @Override
    public void start() {
        context.uiCoordinator.showTopSheet(fragment);
    }

    @Override
    public void stop() {
        context.uiCoordinator.hideTopSheet();
    }

    @Override
    public void onSessionStartSelected(SessionStartType startType) {

        if (startType != SessionStartType.CONTINUED) {

            context.sessionManager.createSession(startType);

        } else {

            context.sessionManager.loadLatestSession();
        }

        context.workflowManager.finishWorkflow();
    }
}