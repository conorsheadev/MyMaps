package com.csws.mymaps.features.planner.planner;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.csws.mymaps.features.planner.planner.calendar.CalendarFragment;
import com.csws.mymaps.features.planner.planner.daily.DailyPlannerFragment;
import com.csws.mymaps.features.planner.planner.weekly.WeeklyPlannerFragment;

import java.util.Arrays;
import java.util.List;

public class PlannerTabsAdapter extends FragmentStateAdapter {
    private final List<Fragment> fragments = Arrays.asList(
            new DailyPlannerFragment(),
            new WeeklyPlannerFragment(),
            new CalendarFragment()
    );

    public PlannerTabsAdapter(FragmentActivity fragmentActivity) {
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
