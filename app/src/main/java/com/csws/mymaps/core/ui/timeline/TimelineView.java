package com.csws.mymaps.core.ui.timeline;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import com.csws.mymaps.R;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.core.models.tasks.TaskItem;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TimelineView extends RelativeLayout {

    private final TimelineRenderer renderer;

    private RelativeLayout timelineContainer;
    private ScrollView scrollView;

    private MaterialButton zoomInButton;
    private MaterialButton zoomOutButton;

    private List<TimelineEntry> currentEntries = new ArrayList<>();

    private int hourHeight = 120;

    private static final int MIN_HOUR_HEIGHT = 60;
    private static final int MAX_HOUR_HEIGHT = 400;
    private static final int ZOOM_STEP = 30;

    public TimelineView(Context context) {
        this(context, null);
    }

    public TimelineView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimelineView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context)
                .inflate(R.layout.view_timeline, this, true);

        initializeViews();

        renderer = new TimelineRenderer(
                context,
                timelineContainer,
                new TimelineConfig()
        );

        setupZoomListeners();
    }

    private void initializeViews() {

        timelineContainer = findViewById(R.id.timelineContainer);

        scrollView = findViewById(R.id.timelineScrollView);

        zoomInButton = findViewById(R.id.buttonZoomIn);

        zoomOutButton = findViewById(R.id.buttonZoomOut);
    }

    private void setupZoomListeners() {

        zoomInButton.setOnClickListener(v -> {

            hourHeight = Math.min(
                    hourHeight + ZOOM_STEP,
                    MAX_HOUR_HEIGHT
            );

            refreshRenderer();
        });

        zoomOutButton.setOnClickListener(v -> {

            hourHeight = Math.max(
                    hourHeight - ZOOM_STEP,
                    MIN_HOUR_HEIGHT
            );

            refreshRenderer();
        });
    }

    private void refreshRenderer() {

        renderer.setHourHeight(hourHeight);

        renderer.render(currentEntries);
    }

    public void render(List<TimelineEntry> entries) {

        currentEntries = new ArrayList<>(entries);

        renderer.setHourHeight(hourHeight);

        renderer.render(entries);
    }

    public TimelineRenderer getRenderer() {
        return renderer;
    }
}
