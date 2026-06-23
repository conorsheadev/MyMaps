package com.csws.mymaps.core.models.plans;

import java.util.HashMap;
import java.util.Map;

public class PlannedStage {

    public enum StageCategory {
        PREPARATION,
        ACTIVE,
        COMPLETION
    }

    public enum StageType {
        PACK_BAG,
        LEAVE,
        NAVIGATION,
        REMINDER,
        NOTES
    }

    public enum StageStatus {
        PENDING,
        ACTIVE,
        COMPLETED,
        SKIPPED
    }

    public String id;

    public String title;

    public StageType type;

    public int order;

    public StageCategory category;

    public StageStatus status;

    // Calculated schedule
    public Long scheduledStartMillis;
    public Long scheduledDurationMillis;
    public Long scheduledEndMillis;
    // Scheduling metadata
    public Long lastCalculatedMillis;
    public boolean requiresScheduleCalculation;
    // Actual execution
    public Long actualStartMillis;
    public Long actualEndMillis;



    // Arbitrary configuration
    public Map<String, String> config = new HashMap<>();

    public PlannedStage() {

        status = StageStatus.PENDING;

        requiresScheduleCalculation = false;
    }
}
