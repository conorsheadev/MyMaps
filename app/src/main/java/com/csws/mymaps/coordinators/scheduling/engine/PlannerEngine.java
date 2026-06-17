package com.csws.mymaps.coordinators.scheduling.engine;

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

    private final PlannerState state = new PlannerState();

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
    public PlannerState buildState(List<PlannedTask> plans) {

        if (plans == null || plans.isEmpty()) {
            return state;
        }

        long now = System.currentTimeMillis();

        processPromptResults(plans);

        updatePlanStatuses(plans, now);

        //updateStageStatuses(plans);

        state.activePlans = findActivePlans(plans, now);

        state.nextPlan = findNextPlan(plans, now);

        state.upcomingPlans = findUpcomingPlans(plans, now);

        state.millisUntilNextPlan = calculateCountdown(state.nextPlan, now);

        evaluateActiveStages(plans, state);

        return state;
    }

    private List<PlannedTask> findActivePlans(List<PlannedTask> plans, long now) {

        List<PlannedTask> active = new ArrayList<>();

        for (PlannedTask plan : plans) {

            if (plan.startTimeMillis == null || plan.endTimeMillis == null) {continue;}
            if (plan.startTimeMillis <= now && now <= plan.endTimeMillis) {
                active.add(plan);
            }
        }

        return active;
    }
    private PlannedTask findNextPlan(List<PlannedTask> plans, long now) {

        PlannedTask nextPlan = null;

        long smallestDelta = Long.MAX_VALUE;

        for (PlannedTask plan : plans) {

            if (plan.startTimeMillis == null) { continue; }

            long delta = plan.startTimeMillis - now;

            if (delta > 0 && delta < smallestDelta) {

                smallestDelta = delta;
                nextPlan = plan;
            }
        }

        return nextPlan;
    }
    private List<PlannedTask> findUpcomingPlans(List<PlannedTask> plans, long now) {

        List<PlannedTask> upcoming = new ArrayList<>();

        for (PlannedTask plan : plans) {

            if (plan.startTimeMillis == null) {
                continue;
            }

            if (plan.startTimeMillis > now) {

                upcoming.add(plan);
            }
        }

        upcoming.sort(
                Comparator.comparingLong(
                        t -> t.startTimeMillis
                )
        );

        return upcoming;
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

        if (plan.startTimeMillis == null || plan.endTimeMillis == null) {return;}
        if (plan.status == PlannedTask.Status.COMPLETED || plan.status == PlannedTask.Status.SKIPPED) {return;}

        //If overdue and not completed, skip
        if (now > plan.endTimeMillis) {

            if (plan.status != PlannedTask.Status.COMPLETED) {

                plan.status = PlannedTask.Status.SKIPPED;
            }

            return;
        }

        //If currently due update to active
        if (plan.startTimeMillis <= now && now <= plan.endTimeMillis) {

            plan.status = PlannedTask.Status.ACTIVE;
        }
    }

    // --- Stage Status ---
    /// Updates each plans current stage status from PENDING to ACTIVE if necessary
    /*private void updateStageStatuses(List<PlannedTask> plans) {

        for (PlannedTask plan : plans) {

            updateStageStatus(plan);
        }
    }
    private void updateStageStatus(PlannedTask plan) {

        PlannedStage stage = getActiveStage(plan);

        if (stage == null) {
            return;
        }

        if (stage.status == PlannedStage.StageStatus.PENDING) {

            stage.status = PlannedStage.StageStatus.ACTIVE;
        }
    }*/

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

    // --- ---
    private long calculateCountdown(PlannedTask nextPlan, long now) {

        if (nextPlan == null) {return 0;}
        return Math.max(0, nextPlan.startTimeMillis - now);
    }
}
