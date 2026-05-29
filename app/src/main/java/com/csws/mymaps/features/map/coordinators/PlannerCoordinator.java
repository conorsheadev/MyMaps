package com.csws.mymaps.features.map.coordinators;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.features.map.planner.PlannerPrompt;
import com.csws.mymaps.features.map.planner.PlannerState;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PlannerCoordinator {

    private final MapViewContext context;

    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable ticker;

    private PlannerState currentState = new PlannerState();
    private final Set<String> shownPrompts = new HashSet<>();

    public PlannerCoordinator(MapViewContext context) {
        this.context = context;
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

        PlannerState state = buildPlannerState(tasks);

        currentState = state;

        updateCountdown(state);

        dispatchPrompts(state);
    }

    private PlannerState buildPlannerState(List<PlannedTask> tasks) {

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

            appendPrompts(state);
        }

        return state;
    }

    private void updateCountdown(PlannerState state) {

        if (state.shouldDisplayCountdown) {

            context.toolbarController.showCountdown(
                    state.millisUntilNextTask
            );

        } else {

            context.toolbarController.hideCountdown();
        }
    }

    private void dispatchPrompts(
            PlannerState state
    ) {

        if (!context.promptHandler
                .canDisplayPlannerPrompts()) {

            return;
        }

        for (PlannerPrompt prompt : state.prompts) {

            context.promptHandler
                    .showPlannerPrompt(prompt);
        }
    }

    private void appendPrompts(
            PlannerState state
    ) {

        long minutes = state.millisUntilNextTask
                        / (1000 * 60);

        PlannedTask task = state.nextTask;

        if (minutes <= 30 && minutes > 25) {

            String id = task.id + "_PREPARE";

            if (shouldShowPrompt(id)) {

                PlannerPrompt prompt =
                        new PlannerPrompt();

                prompt.type =
                        PlannerPrompt.Type
                                .PREPARE_TO_LEAVE;

                prompt.title = "Upcoming Task";

                prompt.message =
                        "You should start preparing soon.";

                state.prompts.add(prompt);
            }
        }

        if (minutes <= 10) {

            String id =
                    task.id + "_LEAVE";

            if (shouldShowPrompt(id)) {

                PlannerPrompt prompt =
                        new PlannerPrompt();

                prompt.type =
                        PlannerPrompt.Type
                                .LEAVE_NOW;

                prompt.title =
                        "Time To Leave";

                prompt.message =
                        "You should leave now.";

                state.prompts.add(prompt);
            }
        }
    }

    private boolean shouldShowPrompt(
            String promptId
    ) {

        if (shownPrompts.contains(promptId)) {
            return false;
        }

        shownPrompts.add(promptId);

        return true;
    }
}
