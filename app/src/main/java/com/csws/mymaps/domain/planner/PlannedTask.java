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

    // References
    public String taskId;
    public String locationId;

    // Planning
    public String date;

    public Long startTimeMillis;
    public Long endTimeMillis;

    public Integer estimatedTravelMinutes;
    public String travelMode;

    // State
    public Status status;

    public PlannedTask(
            String id,
            String taskId,
            String locationId
    ) {
        this.id = id;
        this.taskId = taskId;
        this.locationId = locationId;

        this.status = Status.PLANNED;
    }

    public PlannedTask(
            String id,
            String taskId,
            String locationId,
            String date
    ) {
        this.id = id;
        this.taskId = taskId;
        this.locationId = locationId;
        this.date = date;

        this.status = Status.PLANNED;
    }

    // ----------------------------------------------------
    // Parcelable
    // ----------------------------------------------------

    protected PlannedTask(Parcel in) {

        id = in.readString();

        taskId = in.readString();
        locationId = in.readString();

        date = in.readString();

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

        travelMode = in.readString();

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
        dest.writeString(locationId);

        dest.writeString(date);

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

        dest.writeString(travelMode);

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