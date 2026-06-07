package com.csws.mymaps.domain.planner.engine.prompts;

public class PlannerPrompt {

    public enum Type {
        PREPARE_TO_LEAVE,
        PACK_BAGS,
        LEAVE_NOW,
        TRAFFIC_DELAY,
        TASK_STARTING
    }

    public String taskId;

    public Type type;

    public String title;
    public String message;
}
