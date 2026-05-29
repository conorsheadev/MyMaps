package com.csws.mymaps.features.map.ui.bottom_sheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.csws.mymaps.R;
import com.csws.mymaps.core.ui.TimelineRenderer;
import com.csws.mymaps.core.ui.TimelineView;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationDetailFragment extends Fragment {

    private static final String ARG_LOCATION = "location";
    private static final String ARG_TASKS = "tasks";
    private static final String ARG_PLANNED_TASKS = "planned_tasks";

    private LocationItem location;
    private List<TaskItem> tasks;
    private List<PlannedTask> plannedTasks;


    private TimelineRenderer timelineRenderer;

    public static LocationDetailFragment newInstance(LocationItem location, List<TaskItem> tasks, List<PlannedTask> plannedTasks) {
        LocationDetailFragment fragment = new LocationDetailFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_LOCATION, location);
        args.putParcelableArrayList(ARG_TASKS, new ArrayList<>(tasks));
        args.putParcelableArrayList(ARG_PLANNED_TASKS, new ArrayList<>(plannedTasks));

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_location_detail,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Args
        Bundle args = getArguments();
        if (args != null) {
            location = args.getParcelable(ARG_LOCATION);
            tasks = args.getParcelableArrayList(ARG_TASKS);
            plannedTasks = args.getParcelableArrayList(ARG_PLANNED_TASKS);
        }

        if (tasks == null) {
            tasks = new ArrayList<>();
        }

        if (plannedTasks == null) {
            plannedTasks = new ArrayList<>();
        }

        //Views
        TextView title = view.findViewById(R.id.locationTitle);
        TimelineView timelineView = view.findViewById(R.id.timeline_view);

        //TODO: Clean Up

        //Location
        if (location != null) {
            title.setText(location.name);
        }
        //Timeline
        Map<String, TaskItem> taskLookup = new HashMap<>();
        for (TaskItem task : tasks) {
            taskLookup.put(task.id, task);
        }

        timelineView.render(plannedTasks,taskLookup);
    }
}
