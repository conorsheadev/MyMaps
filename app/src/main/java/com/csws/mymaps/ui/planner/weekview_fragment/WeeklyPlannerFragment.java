package com.csws.mymaps.ui.planner.weekview_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.model.planner.PlannerDay;
import com.csws.mymaps.model.tasks.TaskItem;
import com.csws.mymaps.viewmodel.TaskViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class WeeklyPlannerFragment extends Fragment {
    private TaskViewModel taskViewModel;

    private RecyclerView recyclerView;
    private WeeklyPlannerAdapter adapter;

    public WeeklyPlannerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pagefragment_weeklyplanner, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Init ViewModel
        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        taskViewModel.getTasks().observe(getViewLifecycleOwner(), this::onTasksChanged);

        //Init UI
        recyclerView = view.findViewById(R.id.plannerCarousel);
        adapter = new WeeklyPlannerAdapter();
        recyclerView.setAdapter(adapter);

        //Setup
        setupCarousel();
    }
    private void onTasksChanged(List<TaskItem> tasks) {
        List<PlannerDay> plannerDays = buildPlannerDays(tasks);
        adapter.submitList(plannerDays);
    }

    private void setupCarousel() {
        recyclerView.setAdapter(adapter);

        //Can this be moved to xml
        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(),
                        LinearLayoutManager.HORIZONTAL,
                        false)
        );

        new PagerSnapHelper().attachToRecyclerView(recyclerView);

        //Can this be moved to xml
        recyclerView.setClipToPadding(false);
        recyclerView.setPadding(100, 0, 100, 0);
    }

    private List<PlannerDay> buildPlannerDays(List<TaskItem> allTasks) {

        List<PlannerDay> result = new ArrayList<>();
        List<LocalDate> days = getNext7Days();

        for (LocalDate day : days) {

            List<TaskItem> tasksForDay = new ArrayList<>();

            for (TaskItem task : allTasks) {
                LocalDate taskDate = task.toLocalDate();

                if (taskDate.equals(day)) {
                    tasksForDay.add(task);
                }
            }

            result.add(new PlannerDay(day, tasksForDay));
        }

        return result;
    }





    private List<LocalDate> getNext7Days() {
        List<LocalDate> days = new ArrayList<>();

        LocalDate today = LocalDate.now();

        for (int i = 0; i < 7; i++) {
            days.add(today.plusDays(i));
        }

        return days;
    }
}
