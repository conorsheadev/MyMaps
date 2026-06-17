package com.csws.mymaps.coordinators.map.controllers.map;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.csws.mymaps.R;
import com.csws.mymaps.core.models.locations.LocationItem;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.core.models.tasks.TaskItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapController_InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final Context context;
    private Map<String, List<PlannedTask>> plannedTasksByLocation = new HashMap<>();
    private Map<String, TaskItem> taskLookup = new HashMap<>();


    public MapController_InfoWindowAdapter(Context context) {
        this.context = context;
    }

    public void setTasks(Map<String, List<PlannedTask>> plannedTasksByLocation, Map<String, TaskItem> taskLookup) {
        this.plannedTasksByLocation = plannedTasksByLocation;
        this.taskLookup = taskLookup;
    }

    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(@NonNull Marker marker) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.info_window_location, null);

        TextView title = view.findViewById(R.id.locationTitle);
        TextView tasksView = view.findViewById(R.id.taskList);

        LocationItem location = (LocationItem) marker.getTag();

        if (location == null) {
            title.setText(marker.getTitle());
            tasksView.setText("No tasks");
            return view;
        }

        title.setText(location.name);

        List<PlannedTask> plannedTasks = plannedTasksByLocation.get(location.id);

        if (plannedTasks == null || plannedTasks.isEmpty()) {
            tasksView.setText("No tasks yet");
        } else {
            //TODO: convert from stringbuilder to custom xml inflation or smthn
            StringBuilder builder = new StringBuilder();

            for (PlannedTask plannedTask : plannedTasks) {
                TaskItem task = taskLookup.get(plannedTask.taskId);
                builder.append("• ").append(task.title);

                if (plannedTask.startTimeMillis != null && plannedTask.startTimeMillis > 0) {
                    builder.append(" (scheduled)");
                }

                builder.append("\n");
            }

            tasksView.setText(builder.toString().trim());
        }

        return view;
    }
}