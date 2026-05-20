package com.csws.mymaps.features.planner.tasks;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.planner.taskviews.LocationTasks;
import com.csws.mymaps.features.planner.tasks.adapters.TaskSearchable;
import com.csws.mymaps.features.planner.tasks.adapters.TaskTileAdapter;
import com.csws.mymaps.features.planner.tasks.adapters.date.DateTasksFragment;
import com.csws.mymaps.features.planner.tasks.adapters.location.LocationTasksFragment;
import com.csws.mymaps.features.planner.tasks.adapters.type.TypeTasksFragment;

import java.util.ArrayList;
import java.util.List;

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
