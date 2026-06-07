package com.csws.mymaps.domain.planner.engine;

import android.os.Handler;
import android.os.Looper;

public class PlannerScheduler {

    public interface Listener {
        void onPlannerTick();
        void onCountdownTick();
    }

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Listener listener;

    private Runnable plannerTicker;
    private Runnable countdownTicker;

    public PlannerScheduler(Listener listener) {
        this.listener = listener;
    }

    public void start() {

        startCountdownTicker();
        startPlannerTicker();
    }

    public void stop() {

        if (plannerTicker != null) {
            handler.removeCallbacks(plannerTicker);
        }

        if (countdownTicker != null) {
            handler.removeCallbacks(countdownTicker);
        }
    }

    private void startCountdownTicker() {

        countdownTicker = new Runnable() {
            @Override
            public void run() {

                listener.onCountdownTick();

                handler.postDelayed(this, 1000);
            }
        };

        handler.post(countdownTicker);
    }

    private void startPlannerTicker() {

        plannerTicker = new Runnable() {
            @Override
            public void run() {

                listener.onPlannerTick();

                handler.postDelayed(this, 60000);
            }
        };

        handler.post(plannerTicker);
    }
}
