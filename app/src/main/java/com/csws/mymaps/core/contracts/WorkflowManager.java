package com.csws.mymaps.core.contracts;

import com.csws.mymaps.coordinators.workflows.Workflow;
import com.csws.mymaps.core.models.locations.LocationItem;

/**
 * Responsible for launching and managing application workflows.
 * Only one workflow may be active at a time.
 * TODO: move functions() to a new interface e.g. WorkflowFactory (startCreateLocationFlow(), startCreatePlannedTaskFlow(), startCreateTaskFlow(), startCreateCollectionFlow(), startCreateTaskFromLocationFlow())
 */
public interface WorkflowManager {
    void startWorkflow(Workflow workflow);
    boolean hasActiveWorkflow();
    Workflow getActiveWorkflow();
    void startCreateLocationFlow();
    void startCreatePlannedTaskFlow();
    void startCreateTaskFlow();
    void startCreateCollectionFlow();
    void startCreateTaskFromLocationFlow(LocationItem location);
    void finishWorkflow();
}
