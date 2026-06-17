package com.csws.mymaps.coordinators.scheduling;

import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.csws.mymaps.core.contracts.PromptResultListener;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.coordinators.scheduling.engine.PlannerEngine;
import com.csws.mymaps.coordinators.scheduling.engine.PlannerScheduler;
import com.csws.mymaps.coordinators.scheduling.models.PlannerState;
import com.csws.mymaps.core.models.prompts.PlannerPromptResult;
import com.csws.mymaps.core.models.navigation.NavigationSession;
import com.csws.mymaps.coordinators.map.MapViewContext;

import java.util.List;
/**
 * Coordinates scheduling operations for the application.
 *
 * Responsibilities:
 *
 * - Observes planned task changes.
 * - Builds planner state using PlannerEngine.
 * - Evaluates schedules at regular intervals.
 * - Dispatches prompts and navigation updates.
 * - Receives user responses to planner prompts.
 *
 * Acts as the bridge between PlannerEngine and the UI layer.
 */
public class SchedulingManager implements PromptResultListener, PlannerScheduler.Listener{

    private final MapViewContext context;

    private final PlannerEngine plannerEngine;
    private final PlannerScheduler scheduler;

    private PlannerState currentState;

    public SchedulingManager(MapViewContext context) {

        this.context = context;

        plannerEngine = new PlannerEngine();
        scheduler = new PlannerScheduler(this);
    }

    public void observe(LifecycleOwner owner) {

        context.sessionManager.getPlansLiveData().observe(owner, this::updatePlans);
    }

    public void start() {
        scheduler.start();
    }
    @Override
    public void onPlannerTick() {
        evaluatePlanner();
    }

    @Override
    public void onCountdownTick() {
        updateCountdown();
    }

    private void updatePlans(List<PlannedTask> updatedPlans){
        Log.d("PlannerCoordinator", "calling updatePlans()");
        long start = System.currentTimeMillis();
        currentState = plannerEngine.buildState(updatedPlans);
        Log.d(
                "Planner",
                "buildState took "
                        + (System.currentTimeMillis() - start)
                        + "ms"
        );
        dispatchPrompts(currentState);
    }
    private void evaluatePlanner() {

        List<PlannedTask> plans = context.sessionManager.getAllPlans();

        updatePlans(plans);
        dispatchPrompts(currentState);
        dispatchNavigation(plannerEngine.getActiveNavigation());
    }
    private void updateCountdown() {

        if (currentState.nextPlan != null) {

            context.uiCoordinator.showToolbarCountdown(currentState.millisUntilNextPlan);

        } else {

            context.uiCoordinator.hideToolbarCountdown();
        }
    }

    private void dispatchPrompts(PlannerState state) {

        if (!context.uiCoordinator.canDisplayPlannerPrompts()) {

            return;
        }

        context.uiCoordinator.setPlannerPrompts(plannerEngine.getActivePrompts());
    }

    private void dispatchNavigation(NavigationSession session){

        context.uiCoordinator.setNavigationSession(session);
    }

    @Override
    public void submitPromptResult(PlannerPromptResult result) {

        Log.d("PlannerCoordinator", "Prompt Result: " + result.promptId + " -> " + result.type);
        plannerEngine.submitPromptResult(result);

        evaluatePlanner();
    }

}
