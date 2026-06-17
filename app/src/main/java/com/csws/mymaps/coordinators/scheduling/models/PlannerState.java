package com.csws.mymaps.coordinators.scheduling.models;

import com.csws.mymaps.core.models.plans.PlannedTask;

import java.util.ArrayList;
import java.util.List;

public class PlannerState {

    public List<PlannedTask> activePlans;

    public PlannedTask nextPlan;

    public long millisUntilNextPlan;

    public List<PlannedTask> upcomingPlans = new ArrayList<>();

}
