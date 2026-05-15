package com.csws.mymaps.features.planner.planner.daily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.csws.mymaps.R;
import com.csws.mymaps.core.viewmodel.PlannedTaskViewModel;
import com.csws.mymaps.core.viewmodel.TaskViewModel;

public class DailyPlannerFragment extends Fragment {

    private TaskViewModel taskViewModel;
    private PlannedTaskViewModel plannedTaskViewModel;

    public DailyPlannerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pagefragment_planner_daily, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        plannedTaskViewModel = new ViewModelProvider(requireActivity()).get(PlannedTaskViewModel.class);

        setupUI(view);
    }

    private void setupUI(View view) {

    }
}
