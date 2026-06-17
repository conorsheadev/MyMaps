package com.csws.mymaps.services.activities.planner.weekly;

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
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.services.activities.planner.models.PlannerDay;
import com.csws.mymaps.core.models.tasks.TaskItem;
import com.csws.mymaps.core.viewmodels.tasks.TaskViewModel;
import com.csws.mymaps.core.viewmodels.plans.PlannedTaskViewModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeeklyPlannerFragment extends Fragment {
    private TaskViewModel taskViewModel;
    private PlannedTaskViewModel plannedTaskViewModel;

    private RecyclerView recyclerView;
    private WeeklyPlannerAdapter adapter;

    private List<TaskItem> cachedTasks = new ArrayList<>();
    private List<PlannedTask> cachedPlannedTasks = new ArrayList<>();

    public WeeklyPlannerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pagesubfragment_planner_weekly, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Init ViewModels
        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        taskViewModel.getTasks().observe(getViewLifecycleOwner(), this::onTasksChanged);
        plannedTaskViewModel = new ViewModelProvider(requireActivity()).get(PlannedTaskViewModel.class);
        plannedTaskViewModel.getPlannedTasks().observe(getViewLifecycleOwner(), this::onPlannedTasksChanged);

        //Init UI
        recyclerView = view.findViewById(R.id.plannerCarousel);
        adapter = new WeeklyPlannerAdapter();
        recyclerView.setAdapter(adapter);

        //Setup
        setupCarousel();
    }

    // --- SETUP ---
    private void setupCarousel() {
        recyclerView.setAdapter(adapter);

        //Can this be moved to xml
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));

        new PagerSnapHelper().attachToRecyclerView(recyclerView);

        //Can this be moved to xml
        recyclerView.setClipToPadding(false);
        recyclerView.setPadding(100, 0, 100, 0);
    }

    // --- Data Observers ---
    private void onTasksChanged(List<TaskItem> tasks) {
        List<PlannerDay> plannerDays = buildPlannerDays(cachedPlannedTasks, tasks);
        adapter.submitList(plannerDays);
    }
    private void onPlannedTasksChanged(List<PlannedTask> plannedTasks) {
        List<PlannerDay> plannerDays = buildPlannerDays(plannedTasks, cachedTasks);
        adapter.submitList(plannerDays);
    }

    // --- Planner Days ---
    private void refreshPlanner() {
        List<PlannerDay> plannerDays = buildPlannerDays(cachedPlannedTasks, cachedTasks);
        adapter.submitList(plannerDays);
    }

    private List<PlannerDay> buildPlannerDays(List<PlannedTask> allPlannedTasks, List<TaskItem> allTasks) {

        List<PlannerDay> result = new ArrayList<>();
        List<LocalDate> days = getNext7Days();

        for (LocalDate day : days) {

            //PlannedTasks
            String dateString = day.toString();
            List<PlannedTask> plannedForDay = new ArrayList<>();
            for (PlannedTask plannedTask : allPlannedTasks) {

                if (dateString.equals(plannedTask.date)) {
                    plannedForDay.add(plannedTask);
                }
            }

            //TaskItems
            List<TaskItem> resolvedTasks = resolveTasks(plannedForDay,allTasks);

            //PlannerDay
            PlannerDay plannerDay = new PlannerDay(dateString,resolvedTasks,plannedForDay);

            result.add(plannerDay);
        }

        return result;
    }



    private List<TaskItem> resolveTasks(List<PlannedTask> plannedTasks, List<TaskItem> allTasks) {

        List<TaskItem> result = new ArrayList<>();
        Map<String, TaskItem> taskMap = new HashMap<>();

        for (TaskItem task : allTasks) {
            taskMap.put(task.id, task);
        }

        for (PlannedTask plannedTask : plannedTasks) {

            TaskItem task = taskMap.get(plannedTask.taskId);

            if (task != null) {
                result.add(task);
            }
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
