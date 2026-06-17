package com.csws.mymaps.core.models.tasks;

import android.os.Parcel;
import android.os.Parcelable;

public class TaskCollection implements Parcelable {

    public String id;

    public String title;
    public String description;

    public int color;

    public String iconName;

    public TaskCollection() {}

    public TaskCollection(String id, String title) {
        this.id = id;
        this.title = title;
    }

    // --- Parcelable ---
    protected TaskCollection(Parcel in) {

        id = in.readString();

        title = in.readString();
        description = in.readString();

        color = in.readInt();

        iconName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);

        dest.writeString(title);
        dest.writeString(description);

        dest.writeInt(color);

        dest.writeString(iconName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TaskCollection> CREATOR = new Creator<TaskCollection>() {

                @Override
                public TaskCollection createFromParcel(Parcel in) {
                    return new TaskCollection(in);
                }

                @Override
                public TaskCollection[] newArray(int size) {
                    return new TaskCollection[size];
                }
            };
}
