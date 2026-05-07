package com.csws.mymaps.domain.tasks;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class TaskItem implements Parcelable {
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

    public LocalDate toLocalDate(){return toLocalDate(startTimeMillis);}
    private LocalDate toLocalDate(long millis){
        return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).toLocalDate();
    }

    // --- Parcelable Constructor ---
    protected TaskItem(Parcel in) {
        id = in.readString();
        title = in.readString();
        description = in.readString();

        locationId = in.readString();

        type = TaskType.valueOf(in.readString());
        state = TaskState.valueOf(in.readString());

        if (in.readByte() == 0) {
            startTimeMillis = null;
        } else {
            startTimeMillis = in.readLong();
        }

        if (in.readByte() == 0) {
            endTimeMillis = null;
        } else {
            endTimeMillis = in.readLong();
        }

        if (in.readByte() == 0) {
            travelTimeMinutes = null;
        } else {
            travelTimeMinutes = in.readInt();
        }

        travelMode = in.readString();

        prerequisites = new ArrayList<>();
        in.readStringList(prerequisites);
    }

    // --- Parcelable Writer ---
    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(description);

        dest.writeString(locationId);

        dest.writeString(type.name());
        dest.writeString(state.name());

        if (startTimeMillis == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(startTimeMillis);
        }

        if (endTimeMillis == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(endTimeMillis);
        }

        if (travelTimeMinutes == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(travelTimeMinutes);
        }

        dest.writeString(travelMode);

        dest.writeStringList(prerequisites);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // --- Parcelable Creator ---
    public static final Parcelable.Creator<TaskItem> CREATOR =
            new Parcelable.Creator<TaskItem>() {
                @Override
                public TaskItem createFromParcel(Parcel in) {
                    return new TaskItem(in);
                }

                @Override
                public TaskItem[] newArray(int size) {
                    return new TaskItem[size];
                }
            };
}
