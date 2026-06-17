package com.csws.mymaps.coordinators.scheduling.executors;

import com.csws.mymaps.core.models.plans.PlannedStage;

import java.util.HashMap;
import java.util.Map;

public class ExecutorRegistry {

    private final Map<PlannedStage.StageType, StageExecutor> executors = new HashMap<>();

    public ExecutorRegistry() {

        register(PlannedStage.StageType.PACK_BAG, new PackBagExecutor());
        register(PlannedStage.StageType.LEAVE, new LeaveExecutor());
        register(PlannedStage.StageType.NAVIGATION, new NavigationExecutor());
        /*
        register(PlannedStage.StageType.REMINDER, new ReminderExecutor());
        register(PlannedStage.StageType.NOTES, new NotesExecutor());
        */
    }

    public void register(PlannedStage.StageType type, StageExecutor executor) {

        executors.put(type, executor);
    }

    public StageExecutor getExecutor(PlannedStage.StageType type) {

        return executors.get(type);
    }
}
