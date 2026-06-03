package com.csws.mymaps.features.map.coordinators;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LifecycleOwner;

import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.features.map.planner.PlannerEngine;
import com.csws.mymaps.features.map.planner.PlannerPrompt;
import com.csws.mymaps.features.map.planner.PlannerState;

import java.util.List;

public class PlannerCoordinator {

    private final MapViewContext context;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable ticker;

    private final PlannerEngine plannerEngine;

    private PlannerState currentState = new PlannerState();

    public PlannerCoordinator(MapViewContext context) {

        this.context = context;

        this.plannerEngine = new PlannerEngine();
    }

    public void observe(LifecycleOwner owner) {

        context.plannedTaskViewModel.getPlannedTasks().observe(owner, this::recalculate);
    }

    public void start() {
        startTicker();
    }

    private void startTicker() {

        ticker = new Runnable() {
            @Override
            public void run() {

                List<PlannedTask> tasks = context.plannedTaskViewModel.getPlannedTasks().getValue();

                recalculate(tasks);

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(ticker);
    }

    private void recalculate(List<PlannedTask> tasks) {

        PlannerState state = plannerEngine.buildState(tasks);

        currentState = state;
        updateCountdown(state);
        dispatchPrompts(state);
    }

    private void updateCountdown(PlannerState state) {

        if (state.shouldDisplayCountdown) {

            context.toolbarController.showCountdown(state.millisUntilNextTask);

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
