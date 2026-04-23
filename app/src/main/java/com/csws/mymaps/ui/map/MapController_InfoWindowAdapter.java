package com.csws.mymaps.ui.map;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.csws.mymaps.R;
import com.csws.mymaps.model.locations.LocationItem;
import com.csws.mymaps.model.tasks.TaskItem;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapController_InfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

    private final Context context;
    private Map<String, List<TaskItem>> tasksByLocation = new HashMap<>();

    public MapController_InfoWindowAdapter(Context context) {
        this.context = context;
    }

    public void setTasks(Map<String, List<TaskItem>> tasksByLocation) {
        this.tasksByLocation = tasksByLocation;
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

        List<TaskItem> tasks = tasksByLocation.get(location.id);

        if (tasks == null || tasks.isEmpty()) {
            tasksView.setText("No tasks yet");
        } else {
            StringBuilder builder = new StringBuilder();

            for (TaskItem task : tasks) {
                builder.append("• ").append(task.title);

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