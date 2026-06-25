package com.csws.mymaps.core.ui.plannerstate;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.csws.mymaps.R;
import com.csws.mymaps.coordinators.scheduling.models.PlannerState;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.core.models.tasks.TaskItem;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.Map;

public class PlannerStateView extends FrameLayout {

    private TextView nextPlanTitle;
    private TextView nextPlanCountdown;

    private ChipGroup activePlansContainer;
    private ChipGroup upcomingPlansContainer;

    public PlannerStateView(Context context) {
        this(context, null);
    }

    public PlannerStateView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PlannerStateView(Context context, AttributeSet attrs, int defStyleAttr) {

        super(context, attrs, defStyleAttr);

        LayoutInflater.from(context)
                .inflate(R.layout.view_planner_state, this, true);

        initializeViews();
    }

    private void initializeViews() {

        nextPlanTitle =
                findViewById(R.id.nextPlanTitle);

        nextPlanCountdown =
                findViewById(R.id.nextPlanCountdown);

        activePlansContainer =
                findViewById(R.id.activePlansContainer);

        upcomingPlansContainer =
                findViewById(R.id.upcomingPlansContainer);
    }

    public void render(PlannerState plannerState, Map<String, TaskItem> taskLookup) {

        renderNextPlan(plannerState, taskLookup);
        renderActivePlans(plannerState, taskLookup);
        renderUpcomingPlans(plannerState, taskLookup);
    }





    private void renderNextPlan(PlannerState plannerState, Map<String, TaskItem> taskLookup) {

        if (plannerState.nextPlan == null) {

            nextPlanTitle.setText("No upcoming plan");
            nextPlanCountdown.setText("");

            return;
        }

        TaskItem task =
                taskLookup.get(plannerState.nextPlan.taskId);

        nextPlanTitle.setText(
                task != null
                        ? task.title
                        : plannerState.nextPlan.id
        );

        long minutes =
                plannerState.millisUntilNextPlan / 60000L;

        nextPlanCountdown.setText(
                minutes + " min"
        );
    }

    private void renderUpcomingPlans(
            PlannerState plannerState,
            Map<String, TaskItem> taskLookup
    ) {

        upcomingPlansContainer.removeAllViews();

        if (plannerState.upcomingPlans == null ||
                plannerState.upcomingPlans.isEmpty()) {

            TextView empty = new TextView(getContext());
            empty.setText("No upcoming plans");

            upcomingPlansContainer.addView(empty);

            return;
        }

        for (PlannedTask plannedTask : plannerState.upcomingPlans) {

            Chip chip = createPlanChip(
                    plannedTask,
                    taskLookup
            );

            upcomingPlansContainer.addView(chip);
        }
    }

    private void renderActivePlans(
            PlannerState plannerState,
            Map<String, TaskItem> taskLookup
    ) {

        activePlansContainer.removeAllViews();

        if (plannerState.activePlans == null ||
                plannerState.activePlans.isEmpty()) {

            TextView empty = new TextView(getContext());
            empty.setText("No active plans");

            activePlansContainer.addView(empty);

            return;
        }

        for (PlannedTask plannedTask : plannerState.activePlans) {

            Chip chip = createPlanChip(
                    plannedTask,
                    taskLookup
            );

            activePlansContainer.addView(chip);
        }
    }

    private Chip createPlanChip(
            PlannedTask plannedTask,
            Map<String, TaskItem> taskLookup
    ) {

        TaskItem task = taskLookup.get(plannedTask.taskId);

        Chip chip = new Chip(getContext());

        chip.setClickable(false);
        chip.setCheckable(false);

        chip.setText(task != null ? task.title : plannedTask.id);

        return chip;
    }
}
