package com.csws.mymaps.domain.planner.engine;

import com.csws.mymaps.domain.planner.PlannedTask;

public interface PlannerRule {

    void evaluate(PlannedTask task, PlanPromptState taskState, PlannerState plannerState);
}
