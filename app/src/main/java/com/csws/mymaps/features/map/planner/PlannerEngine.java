package com.csws.mymaps.features.map.planner;

import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.features.map.planner.plannerrules.LeaveNowRule;
import com.csws.mymaps.features.map.planner.plannerrules.PrepareToLeaveRule;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlannerEngine {

    private final Map<String, TaskPromptState> taskStates = new HashMap<>();
    private final List<PlannerRule> rules =
            Arrays.asList(
                    new PrepareToLeaveRule(),
                    new LeaveNowRule()
            );

    public PlannerState buildState(List<PlannedTask> tasks) {

        PlannerState state = new PlannerState();

        if (tasks == null || tasks.isEmpty()) {

            state.shouldDisplayCountdown = false;
            return state;
        }

        long now = System.currentTimeMillis();

        PlannedTask nextTask = null;

        long smallestDelta = Long.MAX_VALUE;

        for (PlannedTask task : tasks) {

            long delta = task.startTimeMillis - now;

            if (delta > 0 && delta < smallestDelta) {

                smallestDelta = delta;

                nextTask = task;
            }
        }

        state.nextTask = nextTask;
        state.millisUntilNextTask = smallestDelta;

        state.shouldDisplayCountdown = nextTask != null;

        if (nextTask != null) {

            TaskPromptState taskState = getState(nextTask);

            for (PlannerRule rule : rules) {

                rule.evaluate(nextTask, taskState, state);
            }
        }

        return state;
    }

    private boolean shouldShowPrompt(TaskPromptState state, String promptId) {

        if (state.shownPrompts.contains(promptId)) {
            return false;
        }

        state.shownPrompts.add(promptId);

        return true;
    }

    private TaskPromptState getState(PlannedTask task) {

        TaskPromptState state =
                taskStates.get(task.id);

        if (state == null) {

            state = new TaskPromptState();
            state.taskId = task.id;

            taskStates.put(task.id, state);
        }

        return state;
    }
}
