package com.csws.mymaps.coordinators.scheduling.executors;

import com.csws.mymaps.core.models.plans.PlannedStage;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.coordinators.scheduling.PlannerEngine;
import com.csws.mymaps.coordinators.scheduling.models.PlannerState;
import com.csws.mymaps.core.models.prompts.PlannerPrompt;
import com.csws.mymaps.core.models.prompts.PlannerPromptResult;
import com.csws.mymaps.core.models.navigation.NavigationRoute;

import java.util.UUID;

public class NavigationExecutor implements StageExecutor {

    @Override
    public void evaluate(
            PlannedTask plan,
            PlannedStage stage,
            PlannerState state,
            PlannerEngine engine
    ) {

        if (stage.status != PlannedStage.StageStatus.PENDING) {
            return;
        }

        PlannerPrompt prompt = new PlannerPrompt();

        prompt.id = UUID.randomUUID().toString();
        prompt.planId = plan.id;

        prompt.type = PlannerPrompt.Type.START_NAVIGATION;

        prompt.title = "Start Navigation";
        prompt.message = "Ready to begin navigation?";

        String destinationId = StageConfigUtils.getDestinationId(stage);

        prompt.data.put(
                "destinationId",
                destinationId
        );

        prompt.data.put(
                "travelMode",
                plan.travelMode
        );

        engine.postPrompt(prompt);

        stage.status = PlannedStage.StageStatus.ACTIVE;
    }

    @Override
    public void handleResult(
            PlannedTask plan,
            PlannedStage stage,
            PlannerPromptResult result,
            PlannerEngine engine
    ) {

        if (result.type != PlannerPromptResult.ResultType.COMPLETED) {
            return;
        }

        NavigationRoute route = NavigationRoute.fromPrompt(result);

        engine.startNavigation(
                plan.id,
                route
        );

        stage.status =
                PlannedStage.StageStatus.ACTIVE;
    }
}
