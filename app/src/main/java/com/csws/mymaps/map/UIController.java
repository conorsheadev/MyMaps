package com.csws.mymaps.map;

import android.content.Context;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.csws.mymaps.R;
import com.google.android.material.appbar.MaterialToolbar;

public class UIController {
    private final AppCompatActivity activity;
    public interface ToolbarListener {
        void onAddLocationClicked();
        void onAddTaskClicked();
    }
    private ToolbarListener listener;

    private DrawerLayout drawerLayout;
    private MaterialToolbar toolbar;

    public UIController(AppCompatActivity activity, DrawerLayout drawerLayout, MaterialToolbar toolbar) {
        this.activity = activity;
        this.listener = (ToolbarListener) activity;
        this.drawerLayout = drawerLayout;
        this.toolbar = toolbar;
    }

    public void init()
    {
        //Toolbar & Drawer Layout

        setupToolbar();
        setupDrawer();

    }
    private void setupToolbar()
    {
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


}
