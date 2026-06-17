package com.csws.mymaps.core.models.sessions;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class DailySession implements Parcelable{

    public String id;
    public String date;
    public long createdTimestamp;
    public SessionState state;
    public SessionStartType startType;

    public List<SessionEvent> events;

    public DailySession() {
        events = new ArrayList<>();
    }

    public DailySession(String date, long createdTimestamp, SessionStartType startType) {
        this.date = date;
        this.createdTimestamp = createdTimestamp;
        this.startType = startType;
        this.events = new ArrayList<>();
    }

    // --- Parcelable ---
    protected DailySession(Parcel in) {
        id = in.readString();
        date = in.readString();
        createdTimestamp = in.readLong();

        String stateName = in.readString();
        if (stateName != null) {
            state = SessionState.valueOf(stateName);
        }

        String startTypeName = in.readString();
        if (startTypeName != null) {
            startType = SessionStartType.valueOf(startTypeName);
        }

        events = in.createTypedArrayList(SessionEvent.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(date);
        dest.writeLong(createdTimestamp);

        dest.writeString(state != null ? state.name() : null);
        dest.writeString(startType != null ? startType.name() : null);

        dest.writeTypedList(events);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<DailySession> CREATOR = new Parcelable.Creator<DailySession>() {
        @Override
        public DailySession createFromParcel(Parcel in) {
            return new DailySession(in);
        }

        @Override
        public DailySession[] newArray(int size) {
            return new DailySession[size];
        }
    };
}
