package com.csws.mymaps.map.mapcontroller;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.csws.mymaps.R;
import com.csws.mymaps.data.locations.LocationItem;
import com.csws.mymaps.data.tasks.TaskItem;
import com.csws.mymaps.map.tasks_and_locations.TaskManager;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.List;

public class MapController_InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final TaskManager taskManager;
    private final Context context;

    public MapController_InfoWindowAdapter(Context context, TaskManager taskManager) {
        this.context = context;
        this.taskManager = taskManager;
    }

    @Override
    public View getInfoWindow(@NonNull Marker marker) {
        return null; // use default frame
    }

    @Override
    public View getInfoContents(@NonNull Marker marker) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.info_window_location, null);

        TextView title = view.findViewById(R.id.locationTitle);
        TextView tasksView = view.findViewById(R.id.taskList);

        // --- Get location ---
        LocationItem location = (LocationItem) marker.getTag();

        if (location == null) {
            title.setText(marker.getTitle());
            tasksView.setText("No tasks");
            return view;
        }

        title.setText(location.name);

        // --- Get tasks ---
        List<TaskItem> tasks = taskManager.getTasksForLocation(location.id);

        if (tasks == null || tasks.isEmpty()) {
            tasksView.setText("No tasks yet");
        } else {

            StringBuilder builder = new StringBuilder();

            for (TaskItem task : tasks) {

                builder.append("• ")
                        .append(task.title);

                if (task.startTimeMillis != null && task.startTimeMillis > 0) {
                    builder.append(" (scheduled)");
                }

                builder.append("\n");
            }

            tasksView.setText(builder.toString().trim());
        }

        return view;
    }
}
