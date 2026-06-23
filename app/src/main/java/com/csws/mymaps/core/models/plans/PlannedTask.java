package com.csws.mymaps.core.models.plans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class PlannedTask implements Parcelable {

    public enum Status {
        PLANNED, // The plan exists and is scheduled, but readiness requirements have not yet been satisfied.
        PREPARED, // The user has completed all required preparation actions and is ready to begin the plan.
        ACTIVE, // The plan is currently in progress.
        COMPLETED, // The user successfully finished the plan.
        SKIPPED // The plan can no longer be completed and was abandoned.
    }

    public String id;

    // References
    public String taskId;
    public String locationId;

    // Planning
    public String date;

    public Long targetStartTimeMillis;
    public Long targetEndTimeMillis;

    // Scheduling
    public Long scheduleLastCalculatedMillis;
    public boolean scheduleDirty = true;
    // Scheduling Estimate
    public Integer estimatedScheduleMinutes;
    public String travelMode;

    // Stages
    public List<PlannedStage> stages = new ArrayList<>();
    public int currentStageIndex = 0;
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
            targetStartTimeMillis = null;
        } else {
            targetStartTimeMillis = in.readLong();
        }

        // endTimeMillis
        if (in.readByte() == 0) {
            targetEndTimeMillis = null;
        } else {
            targetEndTimeMillis = in.readLong();
        }

        // estimatedTravelMinutes
        if (in.readByte() == 0) {
            estimatedScheduleMinutes = null;
        } else {
            estimatedScheduleMinutes = in.readInt();
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
        if (targetStartTimeMillis == null) {

            dest.writeByte((byte) 0);

        } else {

            dest.writeByte((byte) 1);
            dest.writeLong(targetStartTimeMillis);
        }

        // endTimeMillis
        if (targetEndTimeMillis == null) {

            dest.writeByte((byte) 0);

        } else {

            dest.writeByte((byte) 1);
            dest.writeLong(targetEndTimeMillis);
        }

        // estimatedTravelMinutes
        if (estimatedScheduleMinutes == null) {

            dest.writeByte((byte) 0);

        } else {

            dest.writeByte((byte) 1);
            dest.writeInt(estimatedScheduleMinutes);
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