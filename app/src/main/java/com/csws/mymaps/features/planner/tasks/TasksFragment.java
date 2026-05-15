package com.csws.mymaps.features.planner.tasks;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.planner.LocationTasks;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.core.viewmodel.LocationViewModel;
import com.csws.mymaps.core.viewmodel.TaskViewModel;
import com.csws.mymaps.core.viewmodel.PlannedTaskViewModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TasksFragment extends Fragment {

    private LocationViewModel locationViewModel;
    private TaskViewModel taskViewModel;
    private PlannedTaskViewModel plannedTaskViewModel;

    private RecyclerView recyclerView;

    private TasksViewAdapter adapter;

    //Cached Data
    private List<LocationItem> cachedLocations = new ArrayList<>();
    private List<TaskItem> cachedTasks = new ArrayList<>();
    private List<PlannedTask> cachedPlannedTasks = new ArrayList<>();

    public TasksFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pagefragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.tasksRecycler);
        adapter = new TasksViewAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);

        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        plannedTaskViewModel = new ViewModelProvider(requireActivity()).get(PlannedTaskViewModel.class);

        observeData();
    }

    private void updateLocations(List<LocationItem> newLocations){this.cachedLocations = newLocations; updateUI();}
    private void updateTasks(List<TaskItem> newTasks){this.cachedTasks = newTasks; updateUI();}
    private void updatePlannedTasks(List<PlannedTask> newPlannedTasks){this.cachedPlannedTasks = newPlannedTasks; updateUI();}

    private void observeData() {
        locationViewModel.getLocations().observe(getViewLifecycleOwner(), this::updateLocations);
        taskViewModel.getTasks().observe(getViewLifecycleOwner(), this::updateTasks);
        plannedTaskViewModel.getPlannedTasks().observe(getViewLifecycleOwner(), this::updatePlannedTasks);
    }

    private void updateUI() {
        Log.d("TasksFragment", "Updating UI");
        List<LocationTasks> grouped = buildSections(cachedLocations, cachedTasks, cachedPlannedTasks);
        adapter.submitList(grouped);
    }

    private List<LocationTasks> buildSections(List<LocationItem> locations, List<TaskItem> tasks, List<PlannedTask> plannedTasks) {

        List<LocationTasks> result = new ArrayList<>();
        Map<String, TaskItem> taskMap = new HashMap<>();

        for (TaskItem task : tasks) {
            taskMap.put(task.id, task);
        }

        for (LocationItem location : locations) {

            List<PlannedTask> plansForLocation = new ArrayList<>();
            Map<String, TaskItem> resolvedTasks = new HashMap<>();

            for (PlannedTask plannedTask : plannedTasks) {

                TaskItem task = taskMap.get(plannedTask.taskId);
                if (task == null || !location.id.equals(task.locationId)) {continue;}

                plansForLocation.add(plannedTask);
                resolvedTasks.put(task.id, task);
            }

            if (!plansForLocation.isEmpty()) {
                result.add(new LocationTasks(location,plansForLocation,resolvedTasks));
            }
        }

        return result;
    }
}
