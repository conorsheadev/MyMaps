package com.csws.mymaps.map.bottomsheets;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.csws.mymaps.data.tasks.TaskItem;
import com.google.android.material.card.MaterialCardView;

import java.util.List;

public class TimelineRenderer {
    private static final int HOUR_HEIGHT = 120; // px per hour
    private static final int START_HOUR = 6;
    private static final int END_HOUR = 22;

    private final Context context;
    private final RelativeLayout timelineContainer;
    public TimelineRenderer(Context context, RelativeLayout timeLineContainer)
    {
        this.context = context;
        this.timelineContainer = timeLineContainer;
    }

    public void renderTasks(List<TaskItem> tasks) {
        timelineContainer.removeAllViews();
        drawTimeline();
        for(TaskItem task : tasks){
            drawTask(task);
        }
    }
    // --- TIMELINE CREATION ---
    private void drawTimeline(){
        for (int hour = START_HOUR; hour <= END_HOUR; hour++) {

            TextView label = new TextView(context);
            label.setText(String.format("%02d:00", hour));

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    WRAP_CONTENT, WRAP_CONTENT
            );

            params.topMargin = (hour - START_HOUR) * HOUR_HEIGHT;

            timelineContainer.addView(label, params);
        }
    }
    private void drawTask(TaskItem task) {
        int startMinutes = getStart(task.startTimeMillis);
        int endMinutes = getStart(task.endTimeMillis);
        Log.d("TimelineRenderer", "Task start: " + task.startTimeMillis + ", end: " + task.endTimeMillis);
        Log.d("TimelineRenderer", "Task start: " + startMinutes + ", end: " + endMinutes);
        int top = (startMinutes * HOUR_HEIGHT) / 60;
        int height = ((endMinutes - startMinutes) * HOUR_HEIGHT) / 60;

        View taskView = createTaskBlock(task);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(MATCH_PARENT,height);

        params.topMargin = top;
        params.leftMargin = 100;

        timelineContainer.addView(taskView, params);
    }
    private int getStart(Long time) {return (int) ((time)/1);}
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
