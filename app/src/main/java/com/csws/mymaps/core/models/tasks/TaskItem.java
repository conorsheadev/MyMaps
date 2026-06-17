package com.csws.mymaps.core.models.tasks;

import android.os.Parcel;
import android.os.Parcelable;

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

    // Presentation
    public String iconName;

    // Behaviour
    public TaskType type;

    public List<TaskStageTemplate> stageTemplates = new ArrayList<>();

    public TaskItem(
            String id,
            String collectionId,
            String title,
            String description,
            String iconName,
            TaskType type
    ) {
        this.id = id;
        this.collectionId = collectionId;

        this.title = title;
        this.description = description;

        this.iconName = iconName;

        this.type = type;
    }

    public boolean hasCollection() {
        return collectionId != null
                && !collectionId.isEmpty();
    }

    public boolean hasCustomIcon() {
        return iconName != null
                && !iconName.isEmpty();
    }

    // --------------------------------------------------
    // Parcelable
    // --------------------------------------------------

    protected TaskItem(Parcel in) {

        id = in.readString();
        collectionId = in.readString();

        title = in.readString();
        description = in.readString();

        iconName = in.readString();

        String typeName = in.readString();

        if (typeName != null) {
            type = TaskType.valueOf(typeName);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(collectionId);

        dest.writeString(title);
        dest.writeString(description);

        dest.writeString(iconName);

        dest.writeString(
                type != null
                        ? type.name()
                        : null
        );
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