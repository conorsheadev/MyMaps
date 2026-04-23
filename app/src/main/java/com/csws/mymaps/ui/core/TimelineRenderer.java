package com.csws.mymaps.ui.core;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.csws.mymaps.model.tasks.TaskItem;
import com.google.android.material.card.MaterialCardView;

import java.util.Calendar;
import java.util.List;

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

    public void render(List<TaskItem> tasks) {
        container.removeAllViews();

        drawTimeline();

        for (TaskItem task : tasks) {
            if (shouldRender(task)) {
                drawTask(task);
            }
        }
    }
    private boolean shouldRender(TaskItem task) {
        return task.startTimeMillis > 0 && task.endTimeMillis > 0;
    }

    // --- TIMELINE CREATION ---
    private void drawTimeline(){
        for (int hour = config.startHour; hour <= config.endHour; hour++) {

            TextView label = new TextView(context);
            label.setText(String.format("%02d:00", hour));

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    WRAP_CONTENT, WRAP_CONTENT
            );

            params.topMargin = (hour - config.startHour) * config.hourHeight;

            container.addView(label, params);
        }
    }
    private void drawTask(TaskItem task) {
        int startMinutes = getMinutesFromStartOfTimeline(task.startTimeMillis);
        int endMinutes = getMinutesFromStartOfTimeline(task.endTimeMillis);
        Log.d("TimelineRenderer", "Task start: " + task.startTimeMillis + ", end: " + task.endTimeMillis);
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
