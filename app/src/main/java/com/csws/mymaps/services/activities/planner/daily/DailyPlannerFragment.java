package com.csws.mymaps.services.activities.planner.daily;

import android.graphics.Color;
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
import com.csws.mymaps.core.models.plans.PlannedStage;
import com.csws.mymaps.core.ui.timeline.TimelineEntry;
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

        List<TimelineEntry> entries = buildEntries(cachedPlannedTasks, cachedTasks);
        timelineView.render(entries);
    }
    private List<TimelineEntry> buildEntries(
            List<PlannedTask> plannedTasks,
            List<TaskItem> tasks
    ) {

        List<TimelineEntry> entries = new ArrayList<>();

        Map<String, TaskItem> taskMap = new HashMap<>();
        for (TaskItem t : tasks) {
            taskMap.put(t.id, t);
        }

        for (PlannedTask pt : plannedTasks) {

            TaskItem task = taskMap.get(pt.taskId);
            if (task == null) continue;

            if (pt.targetStartTimeMillis != null && pt.targetEndTimeMillis != null) {

                TimelineEntry entry = new TimelineEntry();
                entry.id = pt.id;
                entry.title = task.title;
                entry.startMillis = pt.targetStartTimeMillis;
                entry.endMillis = pt.targetEndTimeMillis;
                entry.level = 0;
                entry.color = Color.parseColor("#4CAF50");

                entries.add(entry);
            }

            if (pt.stages != null) {
                for (PlannedStage stage : pt.stages) {

                    if (stage.scheduledStartMillis == null ||
                            stage.scheduledEndMillis == null) continue;

                    TimelineEntry entry = new TimelineEntry();
                    entry.id = stage.id;
                    entry.title = stage.title;
                    entry.startMillis = stage.scheduledStartMillis;
                    entry.endMillis = stage.scheduledEndMillis;
                    entry.level = 1;
                    entry.color = getStageColor(stage);

                    entries.add(entry);
                }
            }
        }

        return entries;
    }
    //TODO: Move to Utils
    private int getStageColor(PlannedStage stage) {

        switch (stage.type) {
            case PACK_BAG:
                return Color.parseColor("#FF9800");
            case LEAVE:
                return Color.parseColor("#F44336");
            case NAVIGATION:
                return Color.parseColor("#2196F3");
            case REMINDER:
                return Color.parseColor("#9C27B0");
            case NOTES:
                return Color.parseColor("#607D8B");
            default:
                return Color.GRAY;
        }
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
