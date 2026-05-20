package com.csws.mymaps.features.planner.tasks.adapters.date;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.planner.taskviews.DateTasks;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.features.planner.tasks.adapters.BaseTasksFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class DateTasksFragment extends BaseTasksFragment<DateTasks> {

    private DateTasksAdapter adapter;

    @Override
    protected void setupRecyclerView() {

        adapter = new DateTasksAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void submitSections(List<DateTasks> sections) {

        adapter.submitList(sections);
    }

    @Override
    protected List<DateTasks> buildSections(List<LocationItem> locations, List<TaskItem> tasks, List<PlannedTask> plannedTasks) {

        Map<String, DateTasks> grouped = new LinkedHashMap<>();
        Map<String, TaskItem> taskMap = new HashMap<>();

        for (TaskItem task : tasks) {

            if (!matchesSearch(task)) {continue;}

            taskMap.put(task.id, task);
        }

        for (PlannedTask plannedTask : plannedTasks) {

            TaskItem task = taskMap.get(plannedTask.taskId);
            if (task == null) {continue;}

            String date = plannedTask.date;

            if (!grouped.containsKey(date)) {
                grouped.put(date, new DateTasks(date, new ArrayList<>(), new HashMap<>()));
            }

            DateTasks section = grouped.get(date);

            section.plannedTasks.add(plannedTask);

            section.tasks.put(task.id, task);
        }

        return new ArrayList<>(grouped.values());
    }
}
