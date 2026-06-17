package com.csws.mymaps.core.models.prompts;

import java.util.HashMap;
import java.util.Map;

public class PlannerPromptResult {

    public String promptId;

    public String planId;

    public ResultType type;

    public Map<String, String> data = new HashMap<>();

    public enum ResultType {

        DISMISSED,

        COMPLETED,

        SNOOZED,

        CUSTOM
    }

}
