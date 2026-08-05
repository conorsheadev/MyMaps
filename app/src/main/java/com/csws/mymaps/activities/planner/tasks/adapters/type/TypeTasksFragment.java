package com.csws.mymaps.activities.planner.tasks.adapters.type;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.csws.mymaps.core.models.locations.LocationItem;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.activities.planner.models.TypeTasks;
import com.csws.mymaps.core.models.tasks.TaskItem;
import com.csws.mymaps.activities.planner.tasks.adapters.BaseTasksFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TypeTasksFragment extends BaseTasksFragment<TypeTasks> {

    private TypeTasksAdapter adapter;

    @Override
    protected void setupRecyclerView() {

        adapter = new TypeTasksAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void submitSections(List<TypeTasks> sections) {
        adapter.submitList(sections);
    }

    @Override
    protected List<TypeTasks> buildSections(List<LocationItem> locations, List<TaskItem> tasks, List<PlannedTask> plannedTasks) {

        Map<String, TypeTasks> grouped = new LinkedHashMap<>();
        Map<String, TaskItem> taskMap = new HashMap<>();

        for (TaskItem task : tasks) {

            if (!matchesSearch(task)) {continue;}

            taskMap.put(task.id, task);

            String type = task.type != null ? task.type.toString() : "Other";

            if (!grouped.containsKey(type)) {

                grouped.put(type, new TypeTasks(type, new ArrayList<>(), new HashMap<>()));
            }
        }

        for (PlannedTask plannedTask : plannedTasks) {

            TaskItem task = taskMap.get(plannedTask.taskId);

            if (task == null) {continue;}

            String type = task.type != null ? task.type.toString() : "Other";

            TypeTasks section = grouped.get(type);

            section.plannedTasks.add(plannedTask);

            section.tasks.put(task.id, task);
        }

        return new ArrayList<>(grouped.values());
    }
}
