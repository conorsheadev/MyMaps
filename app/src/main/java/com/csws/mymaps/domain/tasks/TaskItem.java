package com.csws.mymaps.domain.tasks;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class TaskItem implements Parcelable {

    public enum TaskType {
        BASIC,
        LOCATION_BASED
    }

    public String id;
    public String collectionId;

    // Core
    public String title;
    public String description;

    // Linking
    public String locationId;

    // Classification
    public TaskType type;

    // Metadata
    public List<String> prerequisites;

    public TaskItem(String id, String collectionId, String title, String description, String locationId, TaskType type) {

        this.id = id;

        this.title = title;
        this.description = description;

        this.locationId = locationId;
        this.collectionId = collectionId;

        this.type = type;

        this.prerequisites = new ArrayList<>();
    }

    // --- Helpers ---
    public boolean hasCollection() {
        return collectionId != null && !collectionId.isEmpty();
    }

    // --- Parcelable ---
    protected TaskItem(Parcel in) {

        id = in.readString();
        collectionId = in.readString();

        title = in.readString();
        description = in.readString();

        locationId = in.readString();

        String typeString = in.readString();

        if (typeString != null) {
            type = TaskType.valueOf(typeString);
        }

        prerequisites = new ArrayList<>();
        in.readStringList(prerequisites);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(collectionId);

        dest.writeString(title);
        dest.writeString(description);

        dest.writeString(locationId);

        dest.writeString(
                type != null
                        ? type.name()
                        : null
        );

        dest.writeStringList(prerequisites);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TaskItem> CREATOR =
            new Creator<TaskItem>() {

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