package com.csws.mymaps.domain.planner;

public class PlannedTask {
    public String taskId;

    public long plannedStartTime;
    public long plannedEndTime;

    public int orderIndex;
    public boolean locked;

    public PlannedTask(String taskId, long plannedStartTime, long plannedEndTime, int orderIndex) {

        this.taskId = taskId;
        this.plannedStartTime = plannedStartTime;
        this.plannedEndTime = plannedEndTime;
        this.orderIndex = orderIndex;

        this.locked = false;
    }

    public long getDurationMillis() {
        return plannedEndTime - plannedStartTime;
    }

    public boolean hasScheduledTime() {
        return plannedStartTime > 0
                && plannedEndTime > 0;
    }
}

