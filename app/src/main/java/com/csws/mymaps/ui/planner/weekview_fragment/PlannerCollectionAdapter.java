package com.csws.mymaps.ui.planner.weekview_fragment;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.csws.mymaps.ui.planner.ConfigFragment;
import com.csws.mymaps.ui.planner.taskview_fragment.TasksFragment;

import java.util.Arrays;
import java.util.List;

public class PlannerCollectionAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragments = Arrays.asList(
            new ConfigFragment(),
            new WeeklyPlannerFragment(),
            new TasksFragment()
    );

    public PlannerCollectionAdapter(FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return fragments.get(position);
    }

    @Override
    public int getItemCount() {
        return fragments.size();
    }
}
