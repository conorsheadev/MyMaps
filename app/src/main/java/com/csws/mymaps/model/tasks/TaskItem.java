package com.csws.mymaps.model.tasks;

import java.util.List;

public class TaskItem {
    public enum TaskType {BASIC, SCHEDULED, LOCATION_BASED}
    public enum TaskState {WAITING,SCHEDULED,STARTED,IN_PROGRESS,COMPLETED}


    // Basic info
    public String id;// UUID
    public String title;
    public String description;
    // Extended info
    public String locationId;
    public TaskType type;

    // State
    public TaskState state;

    // Scheduling
    public Long startTimeMillis;
    public Long endTimeMillis;
    public Integer travelTimeMinutes;
    public String travelMode;

    // ExtraData
    public List<String> prerequisites;

    public TaskItem(String id, String title, String description, String locationId, TaskType type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.locationId = locationId;
        this.type = type;
        this.state = TaskState.WAITING;
    }
}
