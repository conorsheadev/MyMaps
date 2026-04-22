package com.csws.mymaps.ui.planner;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.csws.mymaps.R;
import com.csws.mymaps.viewmodel.LocationViewModel;
import com.csws.mymaps.viewmodel.TaskViewModel;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class PlannerViewActivity extends AppCompatActivity {

    private LocationViewModel locationViewModel;
    private TaskViewModel taskViewModel;

    ViewPager2 viewPager;
    TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plannerview);

        //Init View Models
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);

        //Init UI
        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        setupViewPager();
        setupTabs();

    }

    // --- SETUP ---
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
