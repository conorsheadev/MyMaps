package com.csws.mymaps.map.bottomsheets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.csws.mymaps.R;
import com.csws.mymaps.data.locations.LocationItem;
import com.csws.mymaps.data.tasks.TaskItem;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

public class LocationDetailSheetController {
    private BottomSheetBehavior<View> behavior;
    private TextView title;
    private RelativeLayout timeLineContainer;
    private View sheetView;

    TimelineRenderer timelineRenderer;

    public LocationDetailSheetController(Context context, View sheetView) {

        this.sheetView = sheetView;

        behavior = BottomSheetBehavior.from(sheetView);
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        title = sheetView.findViewById(R.id.locationTitle);
        timeLineContainer = sheetView.findViewById(R.id.timelineContainer);

        timelineRenderer = new TimelineRenderer(context, timeLineContainer);
    }
    public void show(LocationItem location, List<TaskItem> tasks) {

        title.setText(location.name);

        renderTasks(tasks);

        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    public void hide() {
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }
    private void renderTasks(List<TaskItem> tasks) {
        timelineRenderer.renderTasks(tasks);
    }
}
