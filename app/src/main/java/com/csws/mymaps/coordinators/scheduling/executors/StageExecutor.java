package com.csws.mymaps.coordinators.scheduling.executors;

import com.csws.mymaps.core.models.plans.PlannedStage;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.coordinators.scheduling.PlannerEngine;
import com.csws.mymaps.coordinators.scheduling.models.PlannerState;
import com.csws.mymaps.core.models.prompts.PlannerPromptResult;

public interface StageExecutor {

    void evaluate(PlannedTask plan, PlannedStage stage, PlannerState plannerState, PlannerEngine engine);
    void handleResult(PlannedTask plan, PlannedStage stage, PlannerPromptResult result, PlannerEngine engine);
}

