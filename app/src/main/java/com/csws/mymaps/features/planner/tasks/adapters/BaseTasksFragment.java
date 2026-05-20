package com.csws.mymaps.features.planner.tasks.adapters;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.core.viewmodel.LocationViewModel;
import com.csws.mymaps.core.viewmodel.PlannedTaskViewModel;
import com.csws.mymaps.core.viewmodel.TaskViewModel;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseTasksFragment<T> extends Fragment implements TaskSearchable {
    protected LocationViewModel locationViewModel;
    protected TaskViewModel taskViewModel;
    protected PlannedTaskViewModel plannedTaskViewModel;

    protected RecyclerView recyclerView;

    // Cached Data
    protected List<LocationItem> cachedLocations = new ArrayList<>();
    protected List<TaskItem> cachedTasks = new ArrayList<>();
    protected List<PlannedTask> cachedPlannedTasks = new ArrayList<>();

    // Search
    protected String currentQuery = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pagesubfragment_tasks_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.tasksRecycler);

        setupRecyclerView();

        setupViewModels();

        observeData();
    }

    // --- ABSTRACT ---
    protected abstract void setupRecyclerView();
    protected abstract void submitSections(List<T> sections);
    protected abstract List<T> buildSections(List<LocationItem> locations, List<TaskItem> tasks, List<PlannedTask> plannedTasks);

    // --- SETUP ---
    private void setupViewModels() {
        locationViewModel = new ViewModelProvider(requireActivity()).get(LocationViewModel.class);
        taskViewModel = new ViewModelProvider(requireActivity()).get(TaskViewModel.class);
        plannedTaskViewModel = new ViewModelProvider(requireActivity()).get(PlannedTaskViewModel.class);
    }
    private void observeData() {
        locationViewModel.getLocations().observe(getViewLifecycleOwner(), this::onLocationsChanged);
        taskViewModel.getTasks().observe(getViewLifecycleOwner(), this::onTasksChanged);
        plannedTaskViewModel.getPlannedTasks().observe(getViewLifecycleOwner(), this::onPlannedTasksChanged);
    }
    private void onLocationsChanged(List<LocationItem> locations) {
        cachedLocations = locations;
        refreshUI();
    }
    private void onTasksChanged(List<TaskItem> tasks) {
        cachedTasks = tasks;
        refreshUI();
    }
    private void onPlannedTasksChanged(List<PlannedTask> plannedTasks) {
        cachedPlannedTasks = plannedTasks;
        refreshUI();
    }

    // --- SEARCH ---
    @Override
    public void onSearchQueryChanged(String query) {
        currentQuery = query;
        refreshUI();
    }
    protected boolean matchesSearch(TaskItem task) {
        if (currentQuery == null || currentQuery.trim().isEmpty()) {
            return true;
        }

        return task.title.toLowerCase().contains(currentQuery.toLowerCase());
    }
    protected void refreshUI() {
        List<T> sections = buildSections(cachedLocations, cachedTasks, cachedPlannedTasks);
        submitSections(sections);
    }
}
