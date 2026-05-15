package com.csws.mymaps.core.ui;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.google.android.material.card.MaterialCardView;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class TimelineRenderer {

    private final Context context;
    private final RelativeLayout container;
    private final TimelineConfig config;

    public TimelineRenderer(Context context, RelativeLayout container, TimelineConfig config) {
        this.context = context;
        this.container = container;
        this.config = config;
    }

    public void render(List<PlannedTask> plannedTasks, Map<String, TaskItem> tasks) {
        container.removeAllViews();
        ensureContainerHeight();
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
    private void ensureContainerHeight() {
        int totalHours = config.endHour - config.startHour;
        int height = totalHours * config.hourHeight;
        ViewGroup.LayoutParams params = container.getLayoutParams();
        params.height = height;
        container.setLayoutParams(params);
    }

    private void drawTimeline(){
        for (int hour = config.startHour; hour <= config.endHour; hour++) {

            int top = (hour - config.startHour) * config.hourHeight;

            //Label
            TextView label = new TextView(context);
            label.setText(String.format("%02d:00", hour));//TODO: Setup DateTime Utils for Formatting

            RelativeLayout.LayoutParams labelParams = new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            labelParams.topMargin = (hour - config.startHour) * config.hourHeight;

            container.addView(label, labelParams);

            //Line
            View line = new View(context);
            line.setBackgroundColor(Color.LTGRAY);

            RelativeLayout.LayoutParams lineParams = new RelativeLayout.LayoutParams(MATCH_PARENT, 2);
            lineParams.topMargin = top + 40;
            lineParams.leftMargin = config.leftPadding;

            container.addView(line, lineParams);
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
