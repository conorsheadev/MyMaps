package com.csws.mymaps.services.activities.planner.daily;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.csws.mymaps.R;
import com.csws.mymaps.core.ui.timeline.TimelineView;
import com.csws.mymaps.core.viewmodels.plans.PlannedTaskViewModel;
import com.csws.mymaps.core.viewmodels.tasks.TaskViewModel;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.core.models.tasks.TaskItem;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DailyPlannerFragment extends Fragment {

    private TaskViewModel taskViewModel;
    private PlannedTaskViewModel plannedTaskViewModel;

    private TextView daySubtitle;
    private TextView sessionStatus;
    private TimelineView timelineView;

    private List<TaskItem> cachedTasks = new ArrayList<>();
    private List<PlannedTask> cachedPlannedTasks = new ArrayList<>();

    public DailyPlannerFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pagesubfragment_planner_daily, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Init ViewModels
        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        taskViewModel.getTasks().observe(getViewLifecycleOwner(), this::onTasksChanged);
        plannedTaskViewModel = new ViewModelProvider(requireActivity()).get(PlannedTaskViewModel.class);
        plannedTaskViewModel.getPlannedTasks().observe(getViewLifecycleOwner(), this::onPlannedTasksChanged);

        //Init UI
        daySubtitle = view.findViewById(R.id.daySubtitle);
        sessionStatus = view.findViewById(R.id.sessionStatus);
        timelineView = view.findViewById(R.id.timelineContainer);

        setupUI(view);
    }

    private void setupUI(View view) {
        LocalDate today = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d");
        daySubtitle.setText(today.format(formatter));
        sessionStatus.setText("Ready to plan your day");//TODO Implement Session ViewModel fully and setup controllers.
    }

    // --- Data Observers ---
    private void onTasksChanged(List<TaskItem> tasks) {
        cachedTasks = tasks;
        refreshTimeline();
    }
    private void onPlannedTasksChanged(List<PlannedTask> plannedTasks) {
        cachedPlannedTasks = filterToday(plannedTasks);
        refreshTimeline();
    }

    private void refreshTimeline() {

        Map<String, TaskItem> taskLookup = new HashMap<>();

        for (TaskItem task : cachedTasks) {
            taskLookup.put(task.id, task);
        }

        timelineView.render(cachedPlannedTasks, taskLookup);
    }

    private List<PlannedTask> filterToday(List<PlannedTask> plannedTasks) {

        List<PlannedTask> result = new ArrayList<>();
        String today = LocalDate.now().toString();
        for (PlannedTask plannedTask : plannedTasks) {

            if (today.equals(plannedTask.date)) {
                result.add(plannedTask);
            }
        }

        return result;
    }
}
