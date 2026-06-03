package com.csws.mymaps.features.map.planner;

import com.csws.mymaps.domain.planner.PlannedTask;

public interface PlannerRule {

    void evaluate(PlannedTask task, TaskPromptState taskState, PlannerState plannerState);
}
