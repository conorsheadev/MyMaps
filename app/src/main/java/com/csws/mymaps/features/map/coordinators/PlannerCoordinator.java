package com.csws.mymaps.features.map.coordinators;

import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csws.mymaps.core.viewmodel.PlannedTaskViewModel;
import com.csws.mymaps.domain.planner.PlannedTask;

import java.util.List;

public class PlannerCoordinator {

    public static class PlannerState {

        public PlannedTask nextTask;
        public long millisUntilNextTask;
        public boolean shouldDisplayCountdown;
    }

    private final PlannedTaskViewModel plannedTaskViewModel;

    private final MutableLiveData<PlannerState> plannerState = new MutableLiveData<>(new PlannerState());
    public LiveData<PlannerState> getPlannerState() {
        return plannerState;
    }

    public PlannerCoordinator(PlannedTaskViewModel plannedTaskViewModel) {
        this.plannedTaskViewModel = plannedTaskViewModel;
    }


    // --- Observers ---
    private final Handler handler = new Handler(Looper.getMainLooper());
    private Runnable ticker;
    public void observe(LifecycleOwner owner) {
        plannedTaskViewModel.getPlannedTasks().observe(owner, tasks -> {
            recalculate(tasks);
        });
    }

    // --- Start ---
    public void start() {
        startTicker();
    }

    private void startTicker() {

        ticker = new Runnable() {
            @Override
            public void run() {

                List<PlannedTask> tasks = plannedTaskViewModel.getPlannedTasks().getValue();

                recalculate(tasks);

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(ticker);
    }

    private void recalculate(List<PlannedTask> tasks) {

        if (tasks == null || tasks.isEmpty()) {

            PlannerState state = new PlannerState();

            state.shouldDisplayCountdown = false;

            plannerState.postValue(state);

            return;
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

        PlannerState state = new PlannerState();

        state.nextTask = nextTask;

        state.millisUntilNextTask = smallestDelta;

        state.shouldDisplayCountdown = nextTask != null;

        plannerState.postValue(state);
    }
}
