package com.csws.mymaps.core.models.tasks;

import com.csws.mymaps.core.models.plans.PlannedStage;

import java.util.HashMap;
import java.util.Map;

public class TaskStageTemplate {

    public String id;

    public String title;

    public int order;

    public PlannedStage.StageType type;

    public PlannedStage.StageCategory category;

    public Map<String, String> config = new HashMap<>();
}
