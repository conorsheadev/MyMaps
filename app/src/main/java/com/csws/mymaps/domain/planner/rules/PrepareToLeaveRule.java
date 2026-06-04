package com.csws.mymaps.domain.planner.rules;

import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.planner.engine.PlannerPrompt;
import com.csws.mymaps.domain.planner.engine.PlannerRule;
import com.csws.mymaps.domain.planner.engine.PlannerState;
import com.csws.mymaps.domain.planner.engine.TaskPromptState;

public class PrepareToLeaveRule
        implements PlannerRule {

    @Override
    public void evaluate(PlannedTask task, TaskPromptState taskState, PlannerState plannerState) {

        long minutes = plannerState.millisUntilNextTask / (1000 * 60);

        if (minutes > 30 || minutes <= 25) {
            return;
        }

        String promptId = "PREPARE";

        if (taskState.shownPrompts.contains(promptId)) {
            return;
        }

        taskState.shownPrompts.add(promptId);

        PlannerPrompt prompt = new PlannerPrompt();

        prompt.taskId = task.id;

        prompt.type =
                PlannerPrompt.Type.PREPARE_TO_LEAVE;

        prompt.title =
                "Upcoming Task";

        prompt.message =
                "You should start preparing soon.";

        plannerState.prompts.add(prompt);
    }
}
