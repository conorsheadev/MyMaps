package com.csws.mymaps.core.flow.interfaces.coordinator_interfaces;

import com.csws.mymaps.features.map.planner.PlannerPrompt;

public interface PromptHandler {

    void showPlannerPrompt(PlannerPrompt prompt);

    boolean canDisplayPlannerPrompts();
}