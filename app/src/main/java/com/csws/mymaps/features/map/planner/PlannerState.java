package com.csws.mymaps.features.map.planner;

import com.csws.mymaps.domain.planner.PlannedTask;

import java.util.ArrayList;
import java.util.List;

public class PlannerState {

    public PlannedTask nextTask;
    public long millisUntilNextTask;
    public boolean shouldDisplayCountdown;
    public List<PlannerPrompt> prompts = new ArrayList<>();
}
