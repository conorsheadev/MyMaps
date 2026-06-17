package com.csws.mymaps.core.contracts.ui_coordinator;

import com.csws.mymaps.core.models.prompts.PlannerPrompt;
import com.csws.mymaps.core.models.navigation.NavigationSession;

import java.util.List;

public interface PromptHandler {

    void setNavigationSession(NavigationSession session);
    void setPlannerPrompts(List<PlannerPrompt> prompts);

    boolean canDisplayPlannerPrompts();
}