package com.csws.mymaps.core.flow.interfaces.coordinator_interfaces;

import com.csws.mymaps.core.flow.Workflow;
import com.csws.mymaps.domain.locations.LocationItem;

public interface WorkflowNavigator {
    void startWorkflow(Workflow workflow);
    boolean hasActiveWorkflow();
    void startCreateLocationFlow();
    void startCreatePlannedTaskFlow();
    void startCreateTaskFlow();
    void startCreateCollectionFlow();
    void startCreateTaskFromLocationFlow(LocationItem location);
    void finishWorkflow();
}
