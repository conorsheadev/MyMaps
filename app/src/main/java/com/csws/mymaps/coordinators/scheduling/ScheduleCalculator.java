package com.csws.mymaps.coordinators.scheduling;

import com.csws.mymaps.core.models.plans.PlannedStage;
import com.csws.mymaps.core.models.plans.PlannedTask;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScheduleCalculator {

    public void calculateSchedules(List<PlannedTask> plans) {

        if (plans == null) {
            return;
        }

        for (PlannedTask plan : plans) {

            calculateSchedule(plan);
        }
    }

    public void calculateSchedule(PlannedTask plan) {

        if (plan == null) {
            return;
        }

        if (plan.targetStartTimeMillis == null) {
            return;
        }

        long cursor = plan.targetStartTimeMillis;

        List<PlannedStage> stages = new ArrayList<>(plan.stages);
        stages.sort(Comparator.comparingInt(s -> s.order));
        Collections.reverse(stages);

        for (PlannedStage stage : stages) {

            long duration = getStageDurationMillis(stage, plan);

            stage.scheduledEndMillis = cursor;
            stage.scheduledDurationMillis = duration;
            stage.scheduledStartMillis = cursor - duration;
            cursor = stage.scheduledStartMillis;
        }

        plan.estimatedScheduleMinutes = (int) ((plan.targetStartTimeMillis - cursor) / 60000);
        plan.scheduleLastCalculatedMillis = System.currentTimeMillis();
        plan.scheduleDirty = false;
    }

    private long getStageDurationMillis(PlannedStage stage, PlannedTask plan) {

        switch (stage.type) {

            case NAVIGATION:

                return getNavigationDurationMillis(stage);

            case PACK_BAG:

                return 10 * 60 * 1000L;

            case LEAVE:

                return 5 * 60 * 1000L;

            case REMINDER:

                return 1 * 60 * 1000L;

            default:
                return 0;
        }
    }

    private long getNavigationDurationMillis(PlannedStage stage) {

        if (stage.scheduledDurationMillis == null) {

            return 15 * 60 * 1000L;
        }

        return stage.scheduledDurationMillis;
    }
}