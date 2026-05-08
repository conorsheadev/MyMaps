package com.csws.mymaps.domain.session;

import java.util.ArrayList;
import java.util.List;

public class DailySession {

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
}
