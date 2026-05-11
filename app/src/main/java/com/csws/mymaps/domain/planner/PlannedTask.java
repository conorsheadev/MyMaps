package com.csws.mymaps.domain.planner;

import android.os.Parcel;
import android.os.Parcelable;

public class PlannedTask implements Parcelable {

    public enum Status {
        PLANNED,
        ACTIVE,
        COMPLETED,
        SKIPPED
    }

    public String id;

    // Reference to reusable task
    public String taskId;

    // Planned date
    public String date;

    // Schedule
    public Long startTimeMillis;
    public Long endTimeMillis;

    // Planning metadata
    public Integer estimatedTravelMinutes;
    public String travelMode;

    // State
    public Status status;

    public PlannedTask(String id, String taskId) {
        this.id = id;
        this.taskId = taskId;
    }

    public PlannedTask(String id, String taskId, String date) {
        this.id = id;
        this.taskId = taskId;
        this.date = date;

        this.status = Status.PLANNED;
    }

    // --- Parcelable ---
    protected PlannedTask(Parcel in) {
        id = in.readString();
        taskId = in.readString();
        date = in.readString();

        //TODO:Clean Up

        // startTimeMillis
        if (in.readByte() == 0) {
            startTimeMillis = null;
        } else {
            startTimeMillis = in.readLong();
        }
        // endTimeMillis
        if (in.readByte() == 0) {
            endTimeMillis = null;
        } else {
            endTimeMillis = in.readLong();
        }
        // estimatedTravelMinutes
        if (in.readByte() == 0) {
            estimatedTravelMinutes = null;
        } else {
            estimatedTravelMinutes = in.readInt();
        }
        //TravelMode
        travelMode = in.readString();
        //Status
        String statusName = in.readString();
        if (statusName != null) {
            status = Status.valueOf(statusName);
        } else {
            status = Status.PLANNED;
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(taskId);
        dest.writeString(date);

        //TODO:Clean Up

        // startTimeMillis
        if (startTimeMillis == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(startTimeMillis);
        }
        // endTimeMillis
        if (endTimeMillis == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeLong(endTimeMillis);
        }
        // estimatedTravelMinutes
        if (estimatedTravelMinutes == null) {
            dest.writeByte((byte) 0);
        } else {
            dest.writeByte((byte) 1);
            dest.writeInt(estimatedTravelMinutes);
        }
        //TravelMode
        dest.writeString(travelMode);
        //Status
        dest.writeString(
                status != null
                        ? status.name()
                        : Status.PLANNED.name()
        );
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PlannedTask> CREATOR =
            new Creator<PlannedTask>() {

                @Override
                public PlannedTask createFromParcel(Parcel in) {
                    return new PlannedTask(in);
                }

                @Override
                public PlannedTask[] newArray(int size) {
                    return new PlannedTask[size];
                }
            };
}

