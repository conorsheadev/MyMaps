package com.csws.mymaps.coordinators.map.fragments.bottom_sheets;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.csws.mymaps.R;
import com.csws.mymaps.coordinators.scheduling.models.PlannerState;
import com.csws.mymaps.core.models.plans.PlannedStage;
import com.csws.mymaps.core.ui.plannerstate.PlannerStateView;
import com.csws.mymaps.core.ui.timeline.TimelineEntry;
import com.csws.mymaps.core.ui.timeline.TimelineRenderer;
import com.csws.mymaps.core.ui.timeline.TimelineView;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.core.models.sessions.DailySession;
import com.csws.mymaps.core.models.tasks.TaskItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DayPlanFragment extends Fragment {

    private static final String ARG_SESSION = "session";
    private static final String ARG_TASKS = "tasks";
    private static final String ARG_PLANNED_TASKS = "planned_tasks";

    private DailySession session;
    private List<TaskItem> tasks;
    private List<PlannedTask> plannedTasks;
    private PlannerState plannerState;

    public void setPlannerState(PlannerState plannerState) {
        this.plannerState = plannerState;
    }

    private TimelineRenderer timelineRenderer;

    public static DayPlanFragment newInstance(DailySession session, List<TaskItem> tasks, List<PlannedTask> plannedTasks) {
        DayPlanFragment fragment = new DayPlanFragment();
        Bundle args = new Bundle();

        args.putParcelable(ARG_SESSION, session);
        args.putParcelableArrayList(ARG_TASKS, new ArrayList<>(tasks));
        args.putParcelableArrayList(ARG_PLANNED_TASKS, new ArrayList<>(plannedTasks));

        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_day_plan, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        loadArguments();

        TextView title = view.findViewById(R.id.dayPlanTitle);
        TimelineView timelineView = view.findViewById(R.id.timeline_view);
        PlannerStateView plannerStateView = view.findViewById(R.id.plannerStateView);

        if (session != null) {
            title.setText("Today's Plan");
        }

        //Timeline
        Map<String, TaskItem> taskLookup = new HashMap<>();

        for (TaskItem task : tasks) {
            taskLookup.put(task.id, task);
        }

        List<TimelineEntry> entries = buildTimelineEntries(plannedTasks, tasks);
        timelineView.render(entries);

        //PlannerState
        plannerStateView.render(plannerState, taskLookup);
    }

    private List<TimelineEntry> buildTimelineEntries(
            List<PlannedTask> plannedTasks,
            List<TaskItem> tasks
    ) {

        List<TimelineEntry> entries = new ArrayList<>();

        // Lookup tasks
        Map<String, TaskItem> taskMap = new HashMap<>();
        for (TaskItem t : tasks) {
            taskMap.put(t.id, t);
        }

        for (PlannedTask pt : plannedTasks) {

            TaskItem task = taskMap.get(pt.taskId);
            if (task == null) continue;

            // -------------------------
            // 1. TASK LEVEL ENTRY
            // -------------------------
            if (pt.targetStartTimeMillis != null && pt.targetEndTimeMillis != null) {

                TimelineEntry taskEntry = new TimelineEntry();
                taskEntry.id = pt.id;
                taskEntry.title = task.title;
                taskEntry.startMillis = pt.targetStartTimeMillis;
                taskEntry.endMillis = pt.targetEndTimeMillis;
                taskEntry.level = 0;
                taskEntry.color = Color.parseColor("#4CAF50");

                entries.add(taskEntry);
            }

            // -------------------------
            // 2. STAGE LEVEL ENTRIES
            // -------------------------
            if (pt.stages != null) {

                for (PlannedStage stage : pt.stages) {

                    if (stage.scheduledStartMillis == null ||
                            stage.scheduledEndMillis == null) {
                        continue;
                    }

                    TimelineEntry stageEntry = new TimelineEntry();

                    stageEntry.id = stage.id;
                    stageEntry.title = stage.title;
                    stageEntry.startMillis = stage.scheduledStartMillis;
                    stageEntry.endMillis = stage.scheduledEndMillis;

                    stageEntry.level = 1;

                    stageEntry.color = getStageColor(stage);

                    entries.add(stageEntry);
                }
            }
        }

        return entries;
    }

    //TODO: Move to Utils
    private int getStageColor(PlannedStage stage) {

        switch (stage.type) {
            case PACK_BAG:
                return Color.parseColor("#FF9800");
            case LEAVE:
                return Color.parseColor("#F44336");
            case NAVIGATION:
                return Color.parseColor("#2196F3");
            case REMINDER:
                return Color.parseColor("#9C27B0");
            case NOTES:
                return Color.parseColor("#607D8B");
            default:
                return Color.GRAY;
        }
    }

    private void loadArguments() {

        Bundle args = getArguments();

        if (args == null) return;

        session = args.getParcelable(ARG_SESSION);
        tasks = args.getParcelableArrayList(ARG_TASKS);
        plannedTasks = args.getParcelableArrayList(ARG_PLANNED_TASKS);

        if (tasks == null) {
            tasks = new ArrayList<>();
        }

        if (plannedTasks == null) {
            plannedTasks = new ArrayList<>();
        }
    }
}
