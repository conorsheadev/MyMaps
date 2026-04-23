package com.csws.mymaps.ui.planner;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.csws.mymaps.R;
import com.csws.mymaps.ui.map.MapViewActivity;
import com.csws.mymaps.ui.planner.weekview_fragment.PlannerCollectionAdapter;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class PlannerViewActivity extends AppCompatActivity {

    private MaterialToolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plannerview);

        //Init UI
        toolbar = findViewById(R.id.topAppBar);
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);


        setupToolbar();
        setupViewPager();
        setupTabs();
    }

    // --- USER ACTIONS ---
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
    private void setupViewPager() {
        PlannerCollectionAdapter adapter = new PlannerCollectionAdapter(this);
        viewPager.setAdapter(adapter);
    }

    private void setupTabs() {
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0: tab.setText("Config"); break;
                        case 1: tab.setText("Planner"); break;
                        case 2: tab.setText("Tasks"); break;
                    }
                }
        ).attach();
    }
}
