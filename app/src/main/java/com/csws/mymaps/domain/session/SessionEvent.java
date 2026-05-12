package com.csws.mymaps.domain.session;

import android.os.Parcel;
import android.os.Parcelable;

public class SessionEvent implements Parcelable {

    public SessionEventType type;

    public long timestamp;

    public String description;

    public String relatedTaskId;

    public String relatedLocationId;

    public SessionEvent() {}

    protected SessionEvent(Parcel in) {

        String typeName = in.readString();

        if (typeName != null) {
            type = SessionEventType.valueOf(typeName);
        }

        timestamp = in.readLong();
        description = in.readString();
        relatedTaskId = in.readString();
        relatedLocationId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(type != null ? type.name() : null);
        dest.writeLong(timestamp);
        dest.writeString(description);
        dest.writeString(relatedTaskId);
        dest.writeString(relatedLocationId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SessionEvent> CREATOR =
            new Creator<SessionEvent>() {

                @Override
                public SessionEvent createFromParcel(Parcel in) {
                    return new SessionEvent(in);
                }

                @Override
                public SessionEvent[] newArray(int size) {
                    return new SessionEvent[size];
                }
            };
}