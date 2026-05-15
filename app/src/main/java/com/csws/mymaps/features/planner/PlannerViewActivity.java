package com.csws.mymaps.features.planner;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.csws.mymaps.R;
import com.csws.mymaps.features.map.MapViewActivity;
import com.csws.mymaps.features.planner.config.PlannerConfigFragment;
import com.csws.mymaps.features.planner.planner.PlannerFragment;
import com.csws.mymaps.features.planner.tasks.TasksFragment;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class PlannerViewActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_plannerview);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.root), (view, insets) -> {
                    Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                    view.setPadding(view.getPaddingLeft(), systemBars.top, view.getPaddingRight(), view.getPaddingBottom());
                    return insets;
                });


        //Init UI
        toolbar = findViewById(R.id.top_app_bar);
        bottomNav = findViewById(R.id.bottom_nav);

        setupToolbar();
        setupBottomNav();
    }

    // --- USER ACTIONS ---
    private void showFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.nav_host_container, fragment)
                .commit();
    }

    private void openMap(){
        Intent intent = new Intent(this, MapViewActivity.class);
        startActivity(intent);
    }

    // --- SETUP ---
    private void setupToolbar() {
        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_open_map) {
                openMap();
                return true; //Consume Click
            }
            return false; //Do nothing
        });
    }

    private void setupBottomNav() {
        bottomNav.setOnItemSelectedListener(this::handleBottomNavPress);
        bottomNav.setSelectedItemId(R.id.nav_planner);
    }

    // --- Handlers ---
    private boolean handleBottomNavPress(MenuItem item){
        int itemId = item.getItemId();
        if (itemId == R.id.nav_planner) {
            showFragment(new PlannerFragment());
            return true;
        }
        if (itemId == R.id.nav_tasks) {
            showFragment(new TasksFragment());
            return true;
        }
        if (itemId == R.id.nav_config) {
            showFragment(new PlannerConfigFragment());
            return true;
        }

        return false;
    }

}
