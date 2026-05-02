package com.csws.mymaps.features.map.ui.bottom_sheets.deprecated;

import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.core.ui.TimelineRenderer;
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
        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        behavior.setHideable(true);
        title = sheetView.findViewById(R.id.locationTitle);
        //timeLineContainer = sheetView.findViewById(R.id.timelineContainer);

        timelineRenderer = new TimelineRenderer(context, timeLineContainer,new TimelineRenderer.Config());
    }
    public void show(LocationItem location, List<TaskItem> tasks) {
        sheetView.setVisibility(View.VISIBLE);
        title.setText(location.name);

        renderTasks(tasks);

        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }
    public void hide() {
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        sheetView.setVisibility(View.GONE);
    }
    private void renderTasks(List<TaskItem> tasks) {
        timelineRenderer.render(tasks);
    }
}
