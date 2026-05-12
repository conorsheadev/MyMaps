package com.csws.mymaps.features.map.controllers;

import com.google.android.material.appbar.MaterialToolbar;

public class MapToolbarController {

    public interface Listener {
        void onBackPressed();
    }

    private final MaterialToolbar toolbar;

    private Listener listener;

    public MapToolbarController(MaterialToolbar toolbar) {
        this.toolbar = toolbar;

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
}
