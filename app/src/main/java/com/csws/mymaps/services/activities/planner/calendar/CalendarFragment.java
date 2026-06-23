package com.csws.mymaps.services.activities.planner.calendar;

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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.core.models.plans.PlannedStage;
import com.csws.mymaps.core.ui.timeline.TimelineEntry;
import com.csws.mymaps.core.ui.timeline.TimelineView;
import com.csws.mymaps.core.viewmodels.plans.PlannedTaskViewModel;
import com.csws.mymaps.core.viewmodels.tasks.TaskViewModel;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.core.models.tasks.TaskItem;
import com.google.android.material.button.MaterialButton;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CalendarFragment extends Fragment {

    private RecyclerView recyclerView;
    private TimelineView timelineView;
    private TextView monthLabel;

    private CalendarAdapter adapter;

    private LocalDate selectedDate = LocalDate.now();
    private YearMonth currentMonth = YearMonth.now();

    private TaskViewModel taskViewModel;
    private PlannedTaskViewModel plannedTaskViewModel;

    private List<TaskItem> cachedTasks = new ArrayList<>();
    private List<PlannedTask> cachedPlannedTasks = new ArrayList<>();

    public CalendarFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pagesubfragment_planner_calendar, container, false);
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
        monthLabel = view.findViewById(R.id.monthLabel);
        recyclerView = view.findViewById(R.id.calendarRecyclerView);
        timelineView = view.findViewById(R.id.timelineView);

        setupCalendar(view);
    }

    private void setupCalendar(View view) {
        MaterialButton previous = view.findViewById(R.id.buttonPreviousMonth);
        MaterialButton next = view.findViewById(R.id.buttonNextMonth);

        adapter = new CalendarAdapter(this::onDateSelected);

        recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 7));

        recyclerView.setAdapter(adapter);

        previous.setOnClickListener(v -> {
            currentMonth = currentMonth.minusMonths(1);
            refreshCalendar();
        });

        next.setOnClickListener(v -> {
            currentMonth = currentMonth.plusMonths(1);
            refreshCalendar();
        });

        refreshCalendar();
    }

    // --- Data Observers ---
    private void onTasksChanged(List<TaskItem> tasks) {
        cachedTasks = tasks;
        refreshTimeline();
    }
    private void onPlannedTasksChanged(List<PlannedTask> plannedTasks) {
        cachedPlannedTasks = plannedTasks;
        refreshTimeline();
    }

    private void onDateSelected(LocalDate date) {
        selectedDate = date;
        refreshCalendar();
        refreshTimeline();
    }
    private void refreshCalendar() {
        monthLabel.setText(currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
        adapter.submitList(buildCalendarDays());
        refreshTimeline();
    }
    private void refreshTimeline() {

        String selected = selectedDate.toString();

        List<PlannedTask> filtered = new ArrayList<>();

        for (PlannedTask task : cachedPlannedTasks) {

            if (selected.equals(task.date)) {
                filtered.add(task);
            }
        }

        Map<String, TaskItem> taskLookup = new HashMap<>();

        for (TaskItem task : cachedTasks) {
            taskLookup.put(task.id, task);
        }

        List<TimelineEntry> entries = buildEntries(filtered, cachedTasks);
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

            // ---------------- TASK ----------------
            if (pt.targetStartTimeMillis != null && pt.targetEndTimeMillis != null) {

                TimelineEntry taskEntry = new TimelineEntry();
                taskEntry.id = pt.id;
                taskEntry.title = task.title;
                taskEntry.startMillis = pt.targetStartTimeMillis;
                taskEntry.endMillis = pt.targetEndTimeMillis;
                taskEntry.level = 0;
                taskEntry.color = Color.parseColor("#4CAF50");

                entries.add(taskEntry);
            }

            // ---------------- STAGES ----------------
            if (pt.stages != null) {
                for (PlannedStage stage : pt.stages) {

                    if (stage.scheduledStartMillis == null ||
                            stage.scheduledEndMillis == null) continue;

                    TimelineEntry stageEntry = new TimelineEntry();
                    stageEntry.id = stage.id;
                    stageEntry.title = stage.title;
                    stageEntry.startMillis = stage.scheduledStartMillis;
                    stageEntry.endMillis = stage.scheduledEndMillis;
                    stageEntry.level = 1;
                    stageEntry.color = getStageColor(stage);

                    entries.add(stageEntry);
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
    private List<CalendarDay> buildCalendarDays() {

        List<CalendarDay> days = new ArrayList<>();

        LocalDate firstDay = currentMonth.atDay(1);

        int firstDayOffset = firstDay.getDayOfWeek().getValue() - 1;

        int length = currentMonth.lengthOfMonth();

        for (int i = 0; i < firstDayOffset; i++) {
            days.add(new CalendarDay());
        }

        for (int day = 1; day <= length; day++) {

            LocalDate date = currentMonth.atDay(day);

            CalendarDay item = new CalendarDay();
            item.date = date;
            item.isToday = date.equals(LocalDate.now());
            item.isSelected = date.equals(selectedDate);
            item.isCurrentMonth = true;

            days.add(item);
        }

        return days;
    }

}
