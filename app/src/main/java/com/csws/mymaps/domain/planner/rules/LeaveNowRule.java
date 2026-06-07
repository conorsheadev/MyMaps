package com.csws.mymaps.domain.planner.rules;

import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.planner.engine.prompts.PlannerPrompt;
import com.csws.mymaps.domain.planner.engine.PlannerRule;
import com.csws.mymaps.domain.planner.engine.PlannerState;
import com.csws.mymaps.domain.planner.engine.PlanPromptState;

public class LeaveNowRule implements PlannerRule {

    @Override
    public void evaluate(PlannedTask task, PlanPromptState taskState, PlannerState plannerState) {

        long minutes = plannerState.millisUntilNextPlan / (1000 * 60);

        if (minutes > 10) {
            return;
        }

        String promptId = "LEAVE";

        if (taskState.shownPrompts.contains(promptId)) {
            return;
        }

        taskState.shownPrompts.add(promptId);

        PlannerPrompt prompt = new PlannerPrompt();

        prompt.taskId = task.id;

        prompt.type = PlannerPrompt.Type.LEAVE_NOW;

        prompt.title = "Time To Leave";

        prompt.message = "You should leave now.";

        plannerState.prompts.add(prompt);
    }
}
