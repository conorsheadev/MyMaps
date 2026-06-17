package com.csws.mymaps.coordinators.scheduling.executors;

import android.util.Log;

import com.csws.mymaps.core.models.plans.PlannedStage;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.coordinators.scheduling.engine.PlannerEngine;
import com.csws.mymaps.coordinators.scheduling.models.PlannerState;
import com.csws.mymaps.core.models.prompts.PlannerPrompt;
import com.csws.mymaps.core.models.prompts.PlannerPromptResult;

public class LeaveExecutor implements StageExecutor {

    @Override
    public void evaluate(
            PlannedTask plan,
            PlannedStage stage,
            PlannerState state,
            PlannerEngine engine
    ) {
        Log.d("LeaveExecutor", "Evaluating stage: " + stage.title);
        if (stage.status != PlannedStage.StageStatus.PENDING) {
            Log.d("LeaveExecutor", "Stage is not pending -> EXITING");
            return;
        }

        int minutesBefore = StageConfigUtils.getMinutesBefore(stage, 5);
        long triggerTime = plan.startTimeMillis - (minutesBefore * 60_000L);

        long now = System.currentTimeMillis();
        Log.d("LeaveExecutor", "Calculated Trigger Time: " + triggerTime + " Current Time: "+ now);

        if (now < triggerTime) {
            Log.d("LeaveExecutor", "Trigger Time not yet reached -> SLEEPING");
            return;
        }

        PlannerPrompt prompt = new PlannerPrompt();

        prompt.id = plan.id + "_LEAVE";
        prompt.planId = plan.id;

        prompt.type = PlannerPrompt.Type.LEAVE_NOW;

        prompt.title = "Time to leave";

        prompt.message = "Leave now to arrive on time.";

        engine.postPrompt(prompt);

        stage.status = PlannedStage.StageStatus.ACTIVE;

        Log.d("LeaveExecutor", "Prompt posted, Stage Marked Active -> FINISHED");
    }

    @Override
    public void handleResult(
            PlannedTask plan,
            PlannedStage stage,
            PlannerPromptResult result,
            PlannerEngine engine
    ) {

        Log.d(
                "LeaveExecutor",
                "Received result: "
                        + result.promptId
                        + " -> "
                        + result.type
        );

        switch (result.type) {

            case COMPLETED:

                // User confirmed they are leaving
                stage.status = PlannedStage.StageStatus.COMPLETED;

                // Advance to next stage
                plan.currentStageIndex++;

                Log.d(
                        "LeaveExecutor",
                        "Leave confirmed -> stage completed"
                );

                break;

            case DISMISSED:

                Log.d(
                        "LeaveExecutor",
                        "Prompt dismissed"
                );

                break;

            case SNOOZED:

                Log.d(
                        "LeaveExecutor",
                        "Prompt snoozed"
                );

                // TODO:
                // Store snooze timestamp
                // Re-show prompt later

                break;

            default:

                Log.d(
                        "LeaveExecutor",
                        "Unhandled result type: " + result.type
                );
        }
    }
}