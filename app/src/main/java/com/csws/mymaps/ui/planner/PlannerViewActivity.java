package com.csws.mymaps.ui.planner;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.csws.mymaps.R;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class PlannerViewActivity extends AppCompatActivity {

    ViewPager2 viewPager;
    TabLayout tabLayout;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plannerview);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        toolbar = findViewById(R.id.topAppBar);

        PlannerCollectionAdapter adapter = new PlannerCollectionAdapter(this);
        viewPager.setAdapter(adapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Config");
                            break;
                        case 1:
                            tab.setText("Planner");
                            break;
                        case 2:
                            tab.setText("Tasks");
                            break;
                    }
                }
        ).attach();

        setSupportActionBar(toolbar);
    }
}
