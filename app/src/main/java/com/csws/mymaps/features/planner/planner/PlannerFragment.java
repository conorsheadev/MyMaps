package com.csws.mymaps.features.planner.planner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.csws.mymaps.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class PlannerFragment extends Fragment {

    private TabLayout tabLayout;
    private ViewPager2 viewPager;

    public PlannerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pagefragment_planner, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        setupViewPager();
        setupTabs();
    }
    private void setupViewPager() {

        PlannerTabsAdapter adapter = new PlannerTabsAdapter(requireActivity());
        viewPager.setAdapter(adapter);
    }

    private void setupTabs() {

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
                    switch (position) {
                        case 0:
                            tab.setText("Today");
                            break;
                        case 1:
                            tab.setText("Week");
                            break;
                        case 2:
                            tab.setText("Long Term");
                            break;
                    }
                }
        ).attach();
    }
}
