package com.csws.mymaps.coordinators.map.fragments.bottom_sheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.csws.mymaps.R;
import com.csws.mymaps.core.ui.timeline.TimelineEntry;
import com.csws.mymaps.core.ui.timeline.TimelineRenderer;
import com.csws.mymaps.core.ui.timeline.TimelineView;
import com.csws.mymaps.core.models.locations.LocationItem;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.core.models.tasks.TaskItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationPlanFragment extends Fragment {

    private static final String ARG_LOCATION = "location";
    private static final String ARG_TASKS = "tasks";
    private static final String ARG_PLANNED_TASKS = "planned_tasks";

    private LocationItem location;
    private List<TaskItem> tasks;
    private List<PlannedTask> plannedTasks;


    private TimelineRenderer timelineRenderer;

    public static LocationPlanFragment newInstance(LocationItem location, List<TaskItem> tasks, List<PlannedTask> plannedTasks) {
        LocationPlanFragment fragment = new LocationPlanFragment();

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
        List<TimelineEntry> entries = new ArrayList<>();

        for (PlannedTask planned : plannedTasks) {

            // find matching task
            TaskItem task = null;
            for (TaskItem t : tasks) {
                if (t.id.equals(planned.taskId)) {
                    task = t;
                    break;
                }
            }

            if (task == null) continue;

            TimelineEntry entry = new TimelineEntry();
            entry.id = planned.taskId;
            entry.title = task.title;
            entry.startMillis = planned.targetStartTimeMillis;
            entry.endMillis = planned.targetEndTimeMillis;
            entry.level = 0; // task level
            entry.color = 0xFF00FF00; // you define this

            entries.add(entry);
        }

        timelineView.render(entries);
    }
}
