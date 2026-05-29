package com.csws.mymaps.core.flow.interfaces;

import com.csws.mymaps.core.flow.Workflow;
import com.csws.mymaps.domain.locations.LocationItem;

public interface WorkflowNavigator {
    void startWorkflow(Workflow workflow);
    void startCreateLocationFlow();
    void startCreateTaskFlow();
    void startCreateTaskFromLocationFlow(LocationItem location);
    void finishWorkflow();
}
