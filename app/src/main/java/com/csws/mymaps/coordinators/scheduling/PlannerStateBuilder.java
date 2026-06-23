package com.csws.mymaps.coordinators.scheduling;

import com.csws.mymaps.coordinators.scheduling.models.PlannerState;
import com.csws.mymaps.core.models.plans.PlannedTask;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlannerStateBuilder {

    public PlannerState build(
            List<PlannedTask> plans
    ) {

        PlannerState state =
                new PlannerState();

        if (plans == null || plans.isEmpty()) {
            return state;
        }

        long now =
                System.currentTimeMillis();

        state.activePlans =
                findActivePlans(plans, now);

        state.nextPlan =
                findNextPlan(plans, now);

        state.upcomingPlans =
                findUpcomingPlans(plans, now);

        state.millisUntilNextPlan =
                calculateCountdown(
                        state.nextPlan,
                        now
                );

        return state;
    }

    private List<PlannedTask> findActivePlans(
            List<PlannedTask> plans,
            long now
    ) {

        List<PlannedTask> active =
                new ArrayList<>();

        for (PlannedTask plan : plans) {

            if (plan.targetStartTimeMillis == null
                    || plan.targetEndTimeMillis == null) {
                continue;
            }

            if (plan.targetStartTimeMillis <= now
                    && now <= plan.targetEndTimeMillis) {

                active.add(plan);
            }
        }

        return active;
    }

    private PlannedTask findNextPlan(
            List<PlannedTask> plans,
            long now
    ) {

        PlannedTask nextPlan = null;

        long smallestDelta =
                Long.MAX_VALUE;

        for (PlannedTask plan : plans) {

            if (plan.targetStartTimeMillis == null) {
                continue;
            }

            long delta =
                    plan.targetStartTimeMillis - now;

            if (delta > 0
                    && delta < smallestDelta) {

                smallestDelta = delta;
                nextPlan = plan;
            }
        }

        return nextPlan;
    }

    private List<PlannedTask> findUpcomingPlans(
            List<PlannedTask> plans,
            long now
    ) {

        List<PlannedTask> upcoming =
                new ArrayList<>();

        for (PlannedTask plan : plans) {

            if (plan.targetStartTimeMillis == null) {
                continue;
            }

            if (plan.targetStartTimeMillis > now) {

                upcoming.add(plan);
            }
        }

        upcoming.sort(
                Comparator.comparingLong(
                        task -> task.targetStartTimeMillis
                )
        );

        return upcoming;
    }

    private long calculateCountdown(PlannedTask nextPlan, long now) {

        if (nextPlan == null) {
            return 0;
        }

        return Math.max(
                0,
                nextPlan.targetStartTimeMillis - now
        );
    }
}
