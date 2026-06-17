package com.csws.mymaps.services.activities.planner.tasks;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.csws.mymaps.services.activities.planner.tasks.adapters.TaskSearchable;
import com.csws.mymaps.services.activities.planner.tasks.adapters.date.DateTasksFragment;
import com.csws.mymaps.services.activities.planner.tasks.adapters.location.LocationTasksFragment;
import com.csws.mymaps.services.activities.planner.tasks.adapters.type.TypeTasksFragment;

public class TasksViewController {
    public enum TaskViewMode {
        LOCATION,
        TYPE,
        DATE
    }
    private final FragmentManager fragmentManager;
    private final int containerId;

    private TaskViewMode currentMode = TaskViewMode.LOCATION;

    public TasksViewController(
            FragmentManager fragmentManager,
            int containerId
    ) {

        this.fragmentManager = fragmentManager;
        this.containerId = containerId;
    }

    // --- MODE SWITCHING ---

    public void setViewMode(TaskViewMode mode) {
        currentMode = mode;
        showCurrentView();
    }

    public TaskViewMode getCurrentMode() {
        return currentMode;
    }

    // --- SEARCH ---

    public void notifySearchChanged(String query) {
        Fragment fragment = fragmentManager.findFragmentById(containerId);
        if (fragment instanceof TaskSearchable) {
            ((TaskSearchable) fragment).onSearchQueryChanged(query);
        }
    }

    // --- INTERNAL ---

    private void showCurrentView() {

        Fragment fragment;

        switch (currentMode) {

            case TYPE:
                fragment = new TypeTasksFragment();
                break;

            case DATE:
                fragment = new DateTasksFragment();
                break;

            case LOCATION:
            default:
                fragment = new LocationTasksFragment();
                break;
        }

        fragmentManager.beginTransaction().replace(containerId, fragment).commit();
    }
}
