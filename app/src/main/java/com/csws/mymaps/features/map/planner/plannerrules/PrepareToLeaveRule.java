package com.csws.mymaps.features.map.planner.plannerrules;

import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.features.map.planner.PlannerPrompt;
import com.csws.mymaps.features.map.planner.PlannerRule;
import com.csws.mymaps.features.map.planner.PlannerState;
import com.csws.mymaps.features.map.planner.TaskPromptState;

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
