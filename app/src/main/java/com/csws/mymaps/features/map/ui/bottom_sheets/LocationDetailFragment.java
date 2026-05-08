package com.csws.mymaps.features.map.ui.bottom_sheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.csws.mymaps.R;
import com.csws.mymaps.core.ui.TimelineRenderer;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.tasks.TaskItem;

import java.util.ArrayList;
import java.util.List;

public class LocationDetailFragment extends Fragment {

    private static final String ARG_LOCATION = "location";
    private static final String ARG_TASKS = "tasks";

    private LocationItem location;
    private List<TaskItem> tasks;

    private TimelineRenderer timelineRenderer;

    public static LocationDetailFragment newInstance(LocationItem location, List<TaskItem> tasks) {
        LocationDetailFragment fragment = new LocationDetailFragment();

        Bundle args = new Bundle();
        args.putParcelable(ARG_LOCATION, location);
        args.putParcelableArrayList(ARG_TASKS, new ArrayList<>(tasks));

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

        // --- Load args ---
        Bundle args = getArguments();

        if (args != null) {
            location = args.getParcelable(ARG_LOCATION);
            tasks = args.getParcelableArrayList(ARG_TASKS);
        }

        // --- Views ---
        TextView title =
                view.findViewById(R.id.locationTitle);

        RelativeLayout timelineContainer =
                view.findViewById(R.id.timelineContainer);

        // --- Render location ---
        if (location != null) {
            title.setText(location.name);
        }

        // --- Timeline ---
        timelineRenderer = new TimelineRenderer(requireContext(), timelineContainer, new TimelineRenderer.Config());

        if (tasks != null) {
            timelineRenderer.render(tasks);
        }
    }
}
