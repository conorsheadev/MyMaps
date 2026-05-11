package com.csws.mymaps.core.ui;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.google.android.material.card.MaterialCardView;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class TimelineRenderer {

    public static class Config {
        public int startHour = 6;
        public int endHour = 22;
        public int hourHeight = 120;
        public int leftPadding = 100;
    }

    private final Context context;
    private final RelativeLayout container;
    private final Config config;

    public TimelineRenderer(Context context, RelativeLayout container, Config config) {
        this.context = context;
        this.container = container;
        this.config = config;
    }

    public void render(List<PlannedTask> plannedTasks, Map<String, TaskItem> tasks) {
        container.removeAllViews();

        drawTimeline();

        for (PlannedTask plannedTask : plannedTasks) {
            if (shouldRender(plannedTask)) {
                TaskItem task = tasks.get(plannedTask.taskId);
                if (task != null) {
                    drawTask(plannedTask, task);
                }
            }
        }
    }
    private boolean shouldRender(PlannedTask plannedTask) {
        return plannedTask.startTimeMillis > 0 && plannedTask.endTimeMillis > 0;
    }

    // --- TIMELINE CREATION ---
    private void drawTimeline(){
        for (int hour = config.startHour; hour <= config.endHour; hour++) {

            TextView label = new TextView(context);

            //TODO: Setup DateTime Utils for Formatting
            label.setText(String.format("%02d:00", hour));

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    WRAP_CONTENT, WRAP_CONTENT
            );

            params.topMargin = (hour - config.startHour) * config.hourHeight;

            container.addView(label, params);
        }
    }
    private void drawTask(PlannedTask plannedTask, TaskItem task) {
        int startMinutes = getMinutesFromStartOfTimeline(plannedTask.startTimeMillis);
        int endMinutes = getMinutesFromStartOfTimeline(plannedTask.endTimeMillis);
        Log.d("TimelineRenderer", "Task start: " + plannedTask.startTimeMillis + ", end: " + plannedTask.endTimeMillis);
        Log.d("TimelineRenderer", "Task start: " + startMinutes + ", end: " + endMinutes);
        int top = (startMinutes * config.hourHeight) / 60;
        int height = ((endMinutes - startMinutes) * config.hourHeight) / 60;

        View taskView = createTaskBlock(task);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MATCH_PARENT,height);

        params.topMargin = top;
        params.leftMargin = 100;

        container.addView(taskView, params);
    }
    private int getMinutesFromStartOfTimeline(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);

        int minutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
        return minutes - (config.startHour * 60);
    }

    //UI CREATION
    private View createTaskBlock(TaskItem task) {

        MaterialCardView card = new MaterialCardView(context);
        card.setRadius(16f);
        card.setCardElevation(4f);

        card.setCardBackgroundColor(context.getColor(android.R.color.darker_gray));

        TextView text = new TextView(context);
        text.setText(task.title);
        text.setPadding(16, 16, 16, 16);

        card.addView(text);

        return card;
    }
}
