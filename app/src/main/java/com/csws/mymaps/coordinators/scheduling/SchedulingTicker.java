package com.csws.mymaps.coordinators.scheduling;

import android.os.Handler;
import android.os.Looper;

public class SchedulingTicker {

    public interface Listener {
        void onPlannerTick();
        void onCountdownTick();
        void onTravelEstimateTick();
    }

    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Listener listener;

    private Runnable plannerTicker;
    private Runnable countdownTicker;
    private Runnable estimateTicker;

    public SchedulingTicker(Listener listener) {
        this.listener = listener;
    }

    public void start() {

        startCountdownTicker();
        startPlannerTicker();
        startEstimateTicker();
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

    private void startEstimateTicker() {

        estimateTicker = new Runnable() {
            @Override
            public void run() {

                listener.onTravelEstimateTick();

                handler.postDelayed(this, 60000);
            }
        };

        handler.post(estimateTicker);
    }
}
