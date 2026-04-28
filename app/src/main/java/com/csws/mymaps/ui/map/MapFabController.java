package com.csws.mymaps.ui.map;

import android.content.Context;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.MenuRes;
import androidx.appcompat.widget.PopupMenu;

import com.csws.mymaps.R;
import com.csws.mymaps.model.locations.LocationItem;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MapFabController {
    public interface FabActionListener {
        void onFabAction(int actionId);
    }

    private final Context context;
    private final FloatingActionButton fab;
    private final FrameLayout fabContainer;

    private boolean isExpanded = false;
    private final List<FloatingActionButton> miniFabs = new ArrayList<>();

    private FabActionListener listener;

    public void setListener(FabActionListener listener) {
        this.listener = listener;
    }

    public MapFabController(Context context, FloatingActionButton fab, FrameLayout fabContainer) {
        this.context = context;
        this.fab = fab;
        this.fabContainer = fabContainer;
    }

    // --- PUBLIC STATES ---
    public void showDefault() {
        setMenu(R.menu.fab_defaultactions_menu);
    }

    // --- CORE MENU LOGIC ---

    public void setMenu(@MenuRes int menuRes) {
        clearMiniFabs();

        PopupMenu popup = new PopupMenu(context, fab);
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());

        Menu menu = popup.getMenu();

        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);

            FloatingActionButton miniFab = createMiniFab(item, i);
            miniFabs.add(miniFab);
            fabContainer.addView(miniFab);
        }

        fab.setOnClickListener(v -> toggleMenu());
    }

    private FloatingActionButton createMiniFab(MenuItem item, int index) {
        FloatingActionButton miniFab = new FloatingActionButton(context);
        miniFab.setSize(FloatingActionButton.SIZE_MINI);
        miniFab.setImageDrawable(item.getIcon());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        params.gravity = Gravity.BOTTOM | Gravity.END;
        params.bottomMargin = (index + 1) * 150;

        miniFab.setLayoutParams(params);
        miniFab.setVisibility(View.GONE);

        miniFab.setOnClickListener(v -> handleAction(item.getItemId()));

        return miniFab;
    }

    private void toggleMenu() {
        isExpanded = !isExpanded;

        for (FloatingActionButton fab : miniFabs) {
            fab.setVisibility(isExpanded ? View.VISIBLE : View.GONE);
        }
    }

    private void clearMiniFabs() {
        for (FloatingActionButton fab : miniFabs) {
            fabContainer.removeView(fab);
        }
        miniFabs.clear();
        isExpanded = false;
    }

    private void handleAction(int id) {
        if (listener != null) {
            listener.onFabAction(id);
        }
        collapse();
    }

    public void collapse() {
        isExpanded = false;
        for (FloatingActionButton fab : miniFabs) {
            fab.setVisibility(View.GONE);
        }
    }
}
