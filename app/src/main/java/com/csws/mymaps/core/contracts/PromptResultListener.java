package com.csws.mymaps.core.contracts;

import com.csws.mymaps.core.models.prompts.PlannerPromptResult;
/**
 * Receives user responses generated from planner prompts.
 *
 * Implementations are responsible for updating scheduling state
 * in response to prompt actions.
 */
public interface PromptResultListener {
    void submitPromptResult(PlannerPromptResult result);
}
