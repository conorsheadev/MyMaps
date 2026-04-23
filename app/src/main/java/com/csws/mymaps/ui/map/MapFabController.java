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

    public interface DefaultActionsListener {
        void onAddLocation();
        void onAddTask();
    }
    public interface LocationActionsListener {
        void onAddTaskToLocation(LocationItem location);
    }

    public interface PolygonActionsListener {
        void onConfirmPolygon();
        void onUndoPolygon();
        void onCancelPolygon();
    }

    private final Context context;
    private final FloatingActionButton fab;
    private final FrameLayout fabContainer;

    private DefaultActionsListener defaultActionsListener;
    private LocationActionsListener locationActionsListener;
    private PolygonActionsListener polygonActionsListener;

    private boolean isExpanded = false;
    private final List<FloatingActionButton> miniFabs = new ArrayList<>();

    private LocationItem currentLocation;

    public MapFabController(Context context, FloatingActionButton fab, FrameLayout fabContainer) {
        this.context = context;
        this.fab = fab;
        this.fabContainer = fabContainer;
    }

    public void setListeners(DefaultActionsListener defaultActionsListener, LocationActionsListener locationActionsListener) {
        this.defaultActionsListener = defaultActionsListener;
        this.locationActionsListener = locationActionsListener;
    }
    public void setListeners(DefaultActionsListener defaultActionsListener, LocationActionsListener locationActionsListener, PolygonActionsListener polygonActionsListener) {
        this.defaultActionsListener = defaultActionsListener;
        this.locationActionsListener = locationActionsListener;
        this.polygonActionsListener = polygonActionsListener;
    }

    // --- PUBLIC STATES ---

    public void showDefault() {
        setMenu(R.menu.fab_defaultactions_menu);
    }

    public void showLocationActions(LocationItem location) {
        this.currentLocation = location;
        setMenu(R.menu.fab_locationactions_menu);
    }

    public void showPolygonActions() {
        setMenu(R.menu.fab_polyeditactions_menu);
    }

    // --- CORE MENU LOGIC ---

    private void setMenu(@MenuRes int menuRes) {
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

    // --- ACTION HANDLING ---

    private void handleAction(int id) {

        //DEFAULT actions
        if(defaultActionsListener != null) {
            if (id == R.id.fab_add_location) {
                defaultActionsListener.onAddLocation();
            } else if (id == R.id.fab_add_task) {
                defaultActionsListener.onAddTask();
            }
        }

        //LOCATION actions
        if(locationActionsListener != null) {
            if (id == R.id.fab_add_task_to_location) {
                if (currentLocation != null) {
                    locationActionsListener.onAddTaskToLocation(currentLocation);
                }
            }
        }

        //POLYGON actions
        if(polygonActionsListener != null) {
            if (id == R.id.fab_confirm_polygon) {
                polygonActionsListener.onConfirmPolygon();
            } else if (id == R.id.fab_undo_polygon) {
                polygonActionsListener.onUndoPolygon();
            } else if (id == R.id.fab_cancel_polygon) {
                polygonActionsListener.onCancelPolygon();
            }
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
