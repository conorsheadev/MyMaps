package com.csws.mymaps.features.map.coordinators;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.planner.engine.PlannerEngine;
import com.csws.mymaps.domain.planner.engine.PlannerScheduler;
import com.csws.mymaps.domain.planner.engine.prompts.PlannerPrompt;
import com.csws.mymaps.domain.planner.engine.PlannerState;

import java.util.List;
/// The PlannerCoordinator gathers planner inputs, invokes the PlannerEngine, and applies the resulting PlannerState to the UI.
public class PlannerCoordinator implements PlannerScheduler.Listener{

    private final MapViewContext context;

    private final PlannerEngine plannerEngine;
    private final PlannerScheduler scheduler;

    private PlannerState currentState;

    public PlannerCoordinator(MapViewContext context) {

        this.context = context;

        plannerEngine = new PlannerEngine();
        scheduler = new PlannerScheduler(this);
    }

    public void observe(LifecycleOwner owner) {

        context.plannedTaskViewModel.getPlannedTasks().observe(owner, this::updatePlans);
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
    }
    private void evaluatePlanner() {

        List<PlannedTask> plans = context.plannedTaskViewModel.getPlannedTasks().getValue();

        updatePlans(plans);
        dispatchPrompts(currentState);
    }
    private void updateCountdown() {

        if (currentState.nextPlan != null) {

            context.toolbarController.showCountdown(currentState.millisUntilNextPlan);

        } else {

            context.toolbarController.hideCountdown();
        }
    }

    private void dispatchPrompts(PlannerState state) {

        if (!context.promptHandler.canDisplayPlannerPrompts()) {

            return;
        }

        for (PlannerPrompt prompt : state.prompts) {

            context.promptHandler.showPlannerPrompt(prompt);
        }
    }

}
