package com.csws.mymaps.activities.map.controllers;

import android.view.View;
import android.widget.TextView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

import java.util.Locale;

public class MapToolbarController {

    public interface Listener {
        void onBackPressed();
    }

    private final MaterialToolbar toolbar;
    private final MaterialCardView countdownCard;
    private final TextView countdownText;

    private Listener listener;

    public MapToolbarController(MaterialToolbar toolbar, MaterialCardView countdownCard, TextView countdownText) {

        this.toolbar = toolbar;
        this.countdownCard = countdownCard;
        this.countdownText = countdownText;

        setupToolbar();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private void setupToolbar() {

        toolbar.setNavigationOnClickListener(v -> {

            if (listener != null) {
                listener.onBackPressed();
            }
        });
    }

    // ---------------------------------------------------
    // Planner Countdown
    // ---------------------------------------------------

    public void showCountdown(long millisRemaining) {

        countdownCard.setVisibility(View.VISIBLE);
        countdownText.setText(formatDuration(millisRemaining));
    }

    public void hideCountdown() {

        countdownCard.setVisibility(View.GONE);
    }

    private String formatDuration(long millis) {

        long totalSeconds = millis / 1000;

        long hours = totalSeconds / 3600;

        long minutes = (totalSeconds % 3600) / 60;

        long seconds = totalSeconds % 60;

        if (hours > 0) {
            return String.format(Locale.getDefault(), "%02d:%02d", hours, minutes);
        }

        if (minutes < 15){
            return String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        }

        return String.format(Locale.getDefault(), "%02d", minutes);
    }
}
