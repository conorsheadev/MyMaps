package com.csws.mymaps.map;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.MenuRes;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.drawerlayout.widget.DrawerLayout;

import com.csws.mymaps.R;
import com.csws.mymaps.data.locations.LocationItem;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class UIController {
    private final AppCompatActivity activity;
    public interface ToolbarListener {
        void onAddLocationClicked();
        void onAddTaskClicked();
    }
    public interface LocationActionsListener {
        void onAddTaskToLocation(LocationItem location);
    }
    public interface PolygonActionsListener {
        void onConfirmPolygon();
        void onUndoPolygonPoint();
        void onCancelPolygon();
    }

    private ToolbarListener listener;
    private LocationActionsListener locationActionsListener; public void setLocationActionsListener(LocationActionsListener listener) {locationActionsListener = listener;}
    private PolygonActionsListener polygonListener;

    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;
    private FloatingActionButton fab;
    private FrameLayout fabContainer;
    private boolean isFabExpanded = false;
    private List<FloatingActionButton> miniFabs = new ArrayList<>();

    public UIController(AppCompatActivity activity, DrawerLayout drawerLayout, MaterialToolbar toolbar, FloatingActionButton fab, FrameLayout fabContainer) {
        this.activity = activity;
        this.listener = (ToolbarListener) activity;
        this.locationActionsListener = (LocationActionsListener) activity;
        this.polygonListener = (PolygonActionsListener) activity;
        this.drawerLayout = drawerLayout;
        this.toolbar = toolbar;
        this.fab = fab;
        this.fabContainer = fabContainer;
    }

    public void init() {
        //Toolbar & Drawer Layout
        setupToolbar();
        setupDrawer();
        showDefaultFab();
    }
    private void setupToolbar() {
        //activity.setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(item -> {
            if(listener != null) {
                int id = item.getItemId();

                if(id == R.id.action_add_location){
                    listener.onAddLocationClicked();
                    return true;
                } else if (id == R.id.action_add_task) {
                    listener.onAddTaskClicked();
                    return true;
                }
            }
            return false;
        });
    }
    private void setupDrawer(){
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                activity,
                drawerLayout,
                toolbar,
                R.string.open_drawer,
                R.string.close_drawer
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    // --- FAB Menu Control ---
    public void showDefaultFab() {
        setFabMenu(R.menu.fab_defaultactions_menu);
    }
    private LocationItem currentLocation;
    public void showLocationActions(LocationItem location) {
        this.currentLocation = location;
        setFabMenu(R.menu.fab_locationactions_menu);
    }
    public void showPolygonActions() {
        setFabMenu(R.menu.fab_polyeditactions_menu);
    }

    // --- FAB Menu Internals ---
    public void setFabMenu(@MenuRes int menuRes) {

        clearMiniFabs();

        PopupMenu popup = new PopupMenu(activity, fab);
        popup.getMenuInflater().inflate(menuRes, popup.getMenu());

        Menu menu = popup.getMenu();

        for (int i = 0; i < menu.size(); i++) {
            MenuItem item = menu.getItem(i);

            FloatingActionButton miniFab = createMiniFab(item, i);
            miniFabs.add(miniFab);
            fabContainer.addView(miniFab);
        }

        fab.setOnClickListener(v -> toggleFabMenu());
    }

    private FloatingActionButton createMiniFab(MenuItem item, int index) {

        FloatingActionButton miniFab = new FloatingActionButton(activity);
        miniFab.setSize(FloatingActionButton.SIZE_MINI);
        miniFab.setImageDrawable(item.getIcon());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        params.gravity = Gravity.BOTTOM | Gravity.END;
        params.bottomMargin = (index + 1) * 150; // spacing

        miniFab.setLayoutParams(params);
        miniFab.setVisibility(View.GONE);

        miniFab.setOnClickListener(v -> handleFabAction(item.getItemId()));

        return miniFab;
    }
    private void toggleFabMenu() {

        isFabExpanded = !isFabExpanded;

        for (FloatingActionButton miniFab : miniFabs) {
            miniFab.setVisibility(isFabExpanded ? View.VISIBLE : View.GONE);
        }
    }
    private void clearMiniFabs() {
        for (FloatingActionButton fab : miniFabs) {
            fabContainer.removeView(fab);
        }
        miniFabs.clear();
    }

    private void handleFabAction(int id) {

        if (id == R.id.fab_add_location) {
            listener.onAddLocationClicked();
        }
        else if (id == R.id.fab_add_task) {
            listener.onAddTaskClicked();
        }
        else if (id == R.id.fab_add_task_to_location) {
            if (locationActionsListener != null && currentLocation != null) {
                locationActionsListener.onAddTaskToLocation(currentLocation);
            }
        }
        else if (id == R.id.fab_confirm_polygon) {
            polygonListener.onConfirmPolygon();
        }
        else if (id == R.id.fab_undo_polygon) {
            polygonListener.onUndoPolygonPoint();
        }
        else if (id == R.id.fab_cancel_polygon) {
            polygonListener.onCancelPolygon();
        }

        toggleFabMenu(); // collapse after action
    }

}
