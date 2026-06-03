package com.csws.mymaps.features.planner.tasks.adapters.location;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.planner.taskviews.LocationTasks;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.features.planner.tasks.adapters.BaseTasksFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationTasksFragment extends BaseTasksFragment<LocationTasks> {

    private LocationTasksAdapter adapter;

    public LocationTasksFragment() {
    }

    @Override
    protected void setupRecyclerView() {

        adapter = new LocationTasksAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void submitSections(List<LocationTasks> sections) {
        adapter.submitList(sections);
    }

    @Override
    protected List<LocationTasks> buildSections(List<LocationItem> locations, List<TaskItem> tasks, List<PlannedTask> plannedTasks) {

        List<LocationTasks> result = new ArrayList<>();
        Map<String, TaskItem> taskMap = new HashMap<>();

        for (TaskItem task : tasks) {
            if (!matchesSearch(task)) {continue;}
            taskMap.put(task.id, task);
        }

        for (LocationItem location : locations) {

            List<PlannedTask> plansForLocation = new ArrayList<>();
            Map<String, TaskItem> resolvedTasks = new HashMap<>();

            for (PlannedTask plannedTask : plannedTasks) {

                if (!location.id.equals(plannedTask.locationId)) {continue;}

                TaskItem task = taskMap.get(plannedTask.taskId);

                if (task == null) {continue;}

                plansForLocation.add(plannedTask);

                resolvedTasks.put(task.id, task);
            }

            if (!plansForLocation.isEmpty()) {

                result.add(new LocationTasks(location, plansForLocation, resolvedTasks));
            }
        }

        return result;
    }
}