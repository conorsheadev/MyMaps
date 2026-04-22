package com.csws.mymaps.ui.planner.taskview_fragment;

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
import com.csws.mymaps.model.locations.LocationItem;
import com.csws.mymaps.model.planner.LocationTasks;
import com.csws.mymaps.model.tasks.TaskItem;
import com.csws.mymaps.viewmodel.LocationViewModel;
import com.csws.mymaps.viewmodel.TaskViewModel;

import java.util.ArrayList;
import java.util.List;

public class TasksFragment extends Fragment {

    private LocationViewModel locationViewModel;
    private TaskViewModel taskViewModel;

    private RecyclerView recyclerView;

    private TasksViewAdapter adapter;

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

        observeData();
    }

    private List<LocationItem> currentLocations = new ArrayList<>(); private void updateLocations(List<LocationItem> newLocations){this.currentLocations = newLocations; updateUI();}
    private List<TaskItem> currentTasks = new ArrayList<>(); private void updateTasks(List<TaskItem> newTasks){this.currentTasks = newTasks; updateUI();}

    private void observeData() {
        locationViewModel.getLocations().observe(getViewLifecycleOwner(), this::updateLocations);
        taskViewModel.getTasks().observe(getViewLifecycleOwner(), this::updateTasks);
    }

    private void updateUI() {
        Log.d("TasksFragment", "Updating UI");
        onDataChanged(currentLocations, currentTasks);
    }

    private void onDataChanged(List<LocationItem> locations, List<TaskItem> tasks) {

        List<LocationTasks> grouped = new ArrayList<>();

        for (LocationItem location : locations) {

            List<TaskItem> tasksForLocation = new ArrayList<>();

            for (TaskItem task : tasks) {
                if (task.locationId.equals(location.id)) {
                    tasksForLocation.add(task);
                }
            }

            if (!tasksForLocation.isEmpty()) {
                grouped.add(new LocationTasks(location, tasksForLocation));
            }
        }

        adapter.submitList(grouped);
    }
}
