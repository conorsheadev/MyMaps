package com.csws.mymaps.domain.planner.engine;

import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.planner.engine.prompts.PlannerPrompt;
import com.csws.mymaps.domain.planner.engine.prompts.PlannerPromptResult;

import java.util.ArrayList;
import java.util.List;

public class PlannerState {

    public List<PlannedTask> activePlans;

    public PlannedTask nextPlan;

    public long millisUntilNextPlan;

    public List<PlannedTask> upcomingPlans = new ArrayList<>();

    public List<PlannerPrompt> prompts = new ArrayList<>();

    public List<PlannerPromptResult> promptResults = new ArrayList<>();
}
