package com.csws.mymaps.coordinators.scheduling;

import android.util.Log;

import com.csws.mymaps.coordinators.scheduling.models.PlannerState;
import com.csws.mymaps.core.models.plans.PlannedStage;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.core.models.prompts.PlannerPrompt;
import com.csws.mymaps.core.models.prompts.PlannerPromptResult;
import com.csws.mymaps.coordinators.scheduling.executors.ExecutorRegistry;
import com.csws.mymaps.coordinators.scheduling.executors.StageExecutor;
import com.csws.mymaps.core.models.navigation.NavigationRoute;
import com.csws.mymaps.core.models.navigation.NavigationSession;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class PlannerEngine {

    private final ExecutorRegistry registry = new ExecutorRegistry();

    private NavigationSession activeNavigation; public NavigationSession getActiveNavigation() { return activeNavigation;}
    private final List<PlannerPrompt> activePrompts = new ArrayList<>(); public List<PlannerPrompt> getActivePrompts() {return new ArrayList<>(activePrompts);}
    private final Queue<PlannerPromptResult> pendingResults = new LinkedList<>();

    // --- Routes ---
    public void startNavigation(String planId, NavigationRoute route) {
        NavigationSession session =
                new NavigationSession();

        session.planId = planId;
        session.route = route;
        session.active = true;

        activeNavigation = session;
    }

    // --- Prompts ---
    public void postPrompt(PlannerPrompt prompt) {
        Log.d("PromptRecieved", "Prompt Recieved: " + prompt.id);
        activePrompts.add(prompt);
    }
    public void submitPromptResult(PlannerPromptResult result) {
        activePrompts.removeIf(prompt -> prompt.id.equals(result.promptId));
        pendingResults.add(result);
    }

    // --- Build State ---
    public void evaluate(List<PlannedTask> plans, PlannerState state) {

        processPromptResults(plans);

        updatePlanStatuses(plans, System.currentTimeMillis());

        evaluateActiveStages(plans, state);
    }
    // --- Prompt Processing ---

    private void processPromptResults(List<PlannedTask> plans) {

        while (!pendingResults.isEmpty()) {

            PlannerPromptResult result = pendingResults.poll();

            handlePromptResult(plans, result);
        }
    }
    private void handlePromptResult(List<PlannedTask> plans, PlannerPromptResult result) {

        PlannedTask plan = findPlan(plans, result.planId);

        if (plan == null) {
            return;
        }

        PlannedStage stage = getActiveStage(plan);

        if (stage == null) {
            return;
        }

        StageExecutor executor =
                registry.getExecutor(stage.type);

        if (executor == null) {
            return;
        }

        executor.handleResult(
                plan,
                stage,
                result,
                this
        );
    }

    // --- Plan Status ---
    ///  Updates Plan Status from PLANNED/PREPARED to ACTIVE and then from ACTIVE to COMPLETED/SKIPPED
    private void updatePlanStatuses(List<PlannedTask> plans, long now) {

        for (PlannedTask plan : plans) {

            updatePlanStatus(plan, now);
        }
    }
    private void updatePlanStatus(PlannedTask plan, long now) {

        if (plan.targetStartTimeMillis == null || plan.targetEndTimeMillis == null) {return;}
        if (plan.status == PlannedTask.Status.COMPLETED || plan.status == PlannedTask.Status.SKIPPED) {return;}

        //If overdue and not completed, skip
        if (now > plan.targetEndTimeMillis) {

            if (plan.status != PlannedTask.Status.COMPLETED) {

                plan.status = PlannedTask.Status.SKIPPED;
            }

            return;
        }

        //If currently due update to active
        if (plan.targetStartTimeMillis <= now && now <= plan.targetEndTimeMillis) {

            plan.status = PlannedTask.Status.ACTIVE;
        }
    }

    // --- Plan Stages ---
    /// Retrieves executor for each plans current stage and calls the evaluate method
    private void evaluateActiveStages(List<PlannedTask> plans, PlannerState state) {

        for (PlannedTask plan : plans) {

            PlannedStage stage = getActiveStage(plan);
            Log.d("PlannerEngine", "Evaluating stage: " + stage.title);
            Log.d("PlannerEngine", "Stage Status: " + stage.status);
            if (stage == null) {continue;}

            StageExecutor executor = registry.getExecutor(stage.type);
            if (executor == null) {continue;}

            executor.evaluate(plan, stage, state, this);
        }
    }
    // Returns the active stage for a given plan, or null if there is no active stage
    private PlannedStage getActiveStage(PlannedTask plan) {

        if (plan.stages == null) {
            return null;
        }

        if (plan.currentStageIndex < 0) {
            return null;
        }

        if (plan.currentStageIndex >= plan.stages.size()) {
            return null;
        }

        return plan.stages.get(
                plan.currentStageIndex
        );
    }

    private PlannedTask findPlan(List<PlannedTask> plans, String planId) {
        for (PlannedTask plan : plans) {

            if (plan.id.equals(planId)) {
                return plan;
            }
        }

        return null;
    }

}
