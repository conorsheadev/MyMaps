package com.csws.mymaps.domain.planner;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.EnumMap;
import java.util.Map;

public class PlannerConfig {

    public Map<DayOfWeek, LocalTime> wakeUpTimes;

    public PlannerConfig() {
        wakeUpTimes = new EnumMap<>(DayOfWeek.class);
        // Defaults
        for (DayOfWeek day : DayOfWeek.values()) {
            wakeUpTimes.put(day, LocalTime.of(9, 0));
        }
    }

    public LocalTime getWakeUpTime(DayOfWeek day) {
        return wakeUpTimes.get(day);
    }

    public void setWakeUpTime(DayOfWeek day, LocalTime time) {
        wakeUpTimes.put(day, time);
    }
}
