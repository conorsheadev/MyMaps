package com.csws.mymaps.coordinators.map.controllers;

import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetBehavior.BottomSheetCallback;

public class BottomSheetController extends BottomSheetCallback {

    public interface Listener {
        void onSheetShown();
        void onSheetHidden();
    }
    private Listener listener; public void setListener(Listener listener){this.listener = listener;}


    private final View sheet;
    private final int containerId;
    private final FragmentManager fragmentManager;

    private final BottomSheetBehavior<View> behavior;

    public BottomSheetController(View sheet, int containerId, FragmentManager fragmentManager) {
        this.sheet = sheet;
        this.containerId = containerId;
        this.fragmentManager = fragmentManager;

        this.behavior = BottomSheetBehavior.from(sheet);

        setup();
    }

    private void setup() {
        behavior.setHideable(true);

        behavior.setFitToContents(false);
        behavior.setSkipCollapsed(false);

        behavior.addBottomSheetCallback(this);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void onStateChanged(@NonNull View bottomSheet, int newState) {
        if (listener == null) return;

        if (newState == BottomSheetBehavior.STATE_EXPANDED) {
            listener.onSheetShown();
        } else if (newState == BottomSheetBehavior.STATE_HIDDEN) {
            listener.onSheetHidden();
        }
    }

    @Override
    public void onSlide(@NonNull View bottomSheet, float slideOffset) {

    }

    public void show(Fragment fragment) {

        fragmentManager.beginTransaction()
                .replace(containerId, fragment)
                .commit();

        behavior.setPeekHeight(1050, true);
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    public void hide() {
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    public boolean isVisible() {
        return behavior.getState() != BottomSheetBehavior.STATE_HIDDEN;
    }

    public int getState() {
        return behavior.getState();
    }
}
