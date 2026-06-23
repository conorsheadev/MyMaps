package com.csws.mymaps.core.ui.timeline;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.core.models.tasks.TaskItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class TimelineRenderer {

    private final Context context;
    private final RelativeLayout container;

    private List<TimelineEntry> currentEntries = new ArrayList<>();
    private final TimelineConfig config;

    public TimelineRenderer(Context context, RelativeLayout container, TimelineConfig config) {
        this.context = context;
        this.container = container;
        this.config = config;
    }

    public void render(List<TimelineEntry> entries) {

        currentEntries = new ArrayList<>(entries);

        container.removeAllViews();

        ensureContainerHeight();
        drawTimeline();

        for (TimelineEntry entry : entries) {
            if (shouldRender(entry)) {
                drawEntry(entry);
            }
        }
    }
    private boolean shouldRender(TimelineEntry entry) {
        return entry.startMillis > 0 && entry.endMillis > 0;
    }
    public void setHourHeight(int hourHeight) {
        config.hourHeight = hourHeight;
    }

    // --- TIMELINE CREATION ---
    private void ensureContainerHeight() {
        int totalHours = config.endHour - config.startHour;
        int height = totalHours * config.hourHeight;
        ViewGroup.LayoutParams params = container.getLayoutParams();
        params.height = height;
        container.setLayoutParams(params);
    }

    private void drawTimeline() {

        for (int hour = config.startHour; hour <= config.endHour; hour++) {

            int top = (hour - config.startHour) * config.hourHeight;

            TextView label = new TextView(context);
            label.setText(String.format("%02d:00", hour));

            RelativeLayout.LayoutParams labelParams =
                    new RelativeLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);

            labelParams.topMargin = top;

            container.addView(label, labelParams);

            View line = new View(context);
            line.setBackgroundColor(Color.LTGRAY);

            RelativeLayout.LayoutParams lineParams =
                    new RelativeLayout.LayoutParams(MATCH_PARENT, 2);

            lineParams.topMargin = top + 40;
            lineParams.leftMargin = config.leftPadding;

            container.addView(line, lineParams);
        }
    }

    // --- ENTRY RENDERING ---

    private void drawEntry(TimelineEntry entry) {

        int startMinutes = getMinutesFromStartOfTimeline(entry.startMillis);
        int endMinutes = getMinutesFromStartOfTimeline(entry.endMillis);

        int top = (startMinutes * config.hourHeight) / 60;
        int height = ((endMinutes - startMinutes) * config.hourHeight) / 60;

        View view = createEntryView(entry);

        RelativeLayout.LayoutParams params =
                new RelativeLayout.LayoutParams(MATCH_PARENT, height);

        params.topMargin = top;
        params.leftMargin = 100;

        container.addView(view, params);
    }

    private int getMinutesFromStartOfTimeline(long millis) {
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(millis);

        int minutes = cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
        return minutes - (config.startHour * 60);
    }

    // --- UI ---

    private View createEntryView(TimelineEntry entry) {

        MaterialCardView card = new MaterialCardView(context);
        card.setRadius(16f);
        card.setCardElevation(4f);

        card.setCardBackgroundColor(entry.color);

        TextView text = new TextView(context);
        text.setText(entry.title);
        text.setPadding(16, 16, 16, 16);

        card.addView(text);

        return card;
    }
}