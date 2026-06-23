package com.csws.mymaps.coordinators.scheduling.executors;

import android.text.TextUtils;
import android.util.Log;

import com.csws.mymaps.core.models.plans.PlannedStage;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.coordinators.scheduling.PlannerEngine;
import com.csws.mymaps.coordinators.scheduling.models.PlannerState;
import com.csws.mymaps.core.models.prompts.PlannerPrompt;
import com.csws.mymaps.core.models.prompts.PlannerPromptResult;

import java.util.List;
import java.util.UUID;

public class PackBagExecutor implements StageExecutor {

    @Override
    public void evaluate(PlannedTask plan, PlannedStage stage, PlannerState state, PlannerEngine engine) {
        Log.d("PackBagExecutor", "Evaluating stage: " + stage.title);
        if (stage.status != PlannedStage.StageStatus.PENDING) {
            Log.d("PackBagExecutor", "Stage is("+stage.status+") not pending -> EXITING");
            return;
        }

        List<String> items = StageConfigUtils.getPackItems(stage);
        Log.d("PackBagExecutor", "Items: " + TextUtils.join(", ", items));

        PlannerPrompt prompt = new PlannerPrompt();

        prompt.id = UUID.randomUUID().toString();
        prompt.planId = plan.id;

        prompt.type = PlannerPrompt.Type.PACK_BAGS;

        prompt.title = "Pack Bag";
        prompt.message = "Make sure you have everything.";

        prompt.data.put(
                "items",
                TextUtils.join(",", items)
        );

        engine.postPrompt(prompt);

        stage.status = PlannedStage.StageStatus.ACTIVE;

        Log.d("PackBagExecutor", "Prompt posted, Stage Marked Active -> FINISHED");
    }

    @Override
    public void handleResult(
            PlannedTask plan,
            PlannedStage stage,
            PlannerPromptResult result,
            PlannerEngine engine
    ) {

        switch (result.type) {

            case COMPLETED:

                stage.status = PlannedStage.StageStatus.COMPLETED;

                plan.currentStageIndex++;

                break;

            case SNOOZED:

                stage.status = PlannedStage.StageStatus.PENDING;

                break;
        }
    }
}

