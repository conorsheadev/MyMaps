package com.csws.mymaps.coordinators.map.controllers;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class TopSheetController {

    public interface Listener {
        void onSheetShown();
        void onSheetHidden();
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private final View sheet;
    private final int containerId;
    private final FragmentManager fragmentManager;

    private boolean visible = false;

    public TopSheetController(View sheet, int containerId, FragmentManager fragmentManager) {

        this.sheet = sheet;
        this.containerId = containerId;
        this.fragmentManager = fragmentManager;

        setup();
    }

    private void setup() {

        sheet.setVisibility(View.GONE);

        sheet.post(() -> {
            sheet.setTranslationY(-sheet.getHeight());
        });
    }

    public void show(Fragment fragment) {

        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .commit();

        sheet.setVisibility(View.VISIBLE);

        sheet.animate()
                .translationY(0)
                .setDuration(250)
                .start();

        visible = true;

        if (listener != null) {
            listener.onSheetShown();
        }
    }

    public void hide() {

        sheet.animate()
                .translationY(-sheet.getHeight())
                .setDuration(250)
                .withEndAction(() -> {
                    sheet.setVisibility(View.GONE);
                })
                .start();

        visible = false;

        if (listener != null) {
            listener.onSheetHidden();
        }
    }

    public boolean isVisible() {
        return visible;
    }
}
