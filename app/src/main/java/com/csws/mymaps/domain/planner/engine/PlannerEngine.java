package com.csws.mymaps.domain.planner.engine;

import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.planner.rules.LeaveNowRule;
import com.csws.mymaps.domain.planner.rules.PrepareToLeaveRule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlannerEngine {

    private final Map<String, PlanPromptState> planStates = new HashMap<>();
    private final List<PlannerRule> rules =
            Arrays.asList(
                    new PrepareToLeaveRule(),
                    new LeaveNowRule()
            );

    public PlannerState buildState(List<PlannedTask> plans) {

        PlannerState state = new PlannerState();

        if (plans == null || plans.isEmpty()) {
            return state;
        }

        long now = System.currentTimeMillis();

        state.activePlans = findActivePlans(plans, now);

        state.nextPlan = findNextPlan(plans, now);

        state.upcomingPlans = findUpcomingPlans(plans, now);

        state.millisUntilNextPlan = calculateCountdown(state.nextPlan, now);

        evaluateRules(state);

        return state;
    }

    private List<PlannedTask> findActivePlans(List<PlannedTask> plans, long now) {

        List<PlannedTask> active = new ArrayList<>();

        for (PlannedTask plan : plans) {

            if (plan.startTimeMillis == null || plan.endTimeMillis == null) {continue;}
            if (plan.startTimeMillis <= now && now <= plan.endTimeMillis) {
                active.add(plan);
            }
        }

        return active;
    }

    private PlannedTask findNextPlan(List<PlannedTask> plans, long now) {

        PlannedTask nextPlan = null;

        long smallestDelta = Long.MAX_VALUE;

        for (PlannedTask plan : plans) {

            if (plan.startTimeMillis == null) { continue; }

            long delta = plan.startTimeMillis - now;

            if (delta > 0 && delta < smallestDelta) {

                smallestDelta = delta;
                nextPlan = plan;
            }
        }

        return nextPlan;
    }

    private List<PlannedTask> findUpcomingPlans(List<PlannedTask> plans, long now) {

        List<PlannedTask> upcoming = new ArrayList<>();

        for (PlannedTask plan : plans) {

            if (plan.startTimeMillis == null) {
                continue;
            }

            if (plan.startTimeMillis > now) {

                upcoming.add(plan);
            }
        }

        upcoming.sort(
                Comparator.comparingLong(
                        t -> t.startTimeMillis
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
                nextPlan.startTimeMillis - now
        );
    }
    private void evaluateRules(PlannerState state) {

        if (state.nextPlan == null) {
            return;
        }

        PlanPromptState taskState = getState(state.nextPlan);

        for (PlannerRule rule : rules) {

            rule.evaluate(
                    state.nextPlan,
                    taskState,
                    state
            );
        }
    }
    private PlanPromptState getState(PlannedTask plan) {

        PlanPromptState state = planStates.get(plan.id);

        if (state == null) {

            state = new PlanPromptState();
            state.planId = plan.id;

            planStates.put(plan.id, state);
        }

        return state;
    }
}
