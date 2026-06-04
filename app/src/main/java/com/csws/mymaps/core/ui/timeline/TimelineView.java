package com.csws.mymaps.core.ui.timeline;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;

import java.util.List;
import java.util.Map;

public class TimelineView extends RelativeLayout {

    private final TimelineRenderer renderer;

    public TimelineView(Context context) {
        this(context, null);
    }

    public TimelineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimelineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        renderer = new TimelineRenderer(context, this, new TimelineConfig());
    }

    public void render(List<PlannedTask> plannedTasks, Map<String, TaskItem> tasks) {
        renderer.render(plannedTasks, tasks);
    }

    public TimelineRenderer getRenderer() {
        return renderer;
    }
}
