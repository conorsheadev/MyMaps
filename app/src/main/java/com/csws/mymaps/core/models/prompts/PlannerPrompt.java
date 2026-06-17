package com.csws.mymaps.core.models.prompts;

import java.util.HashMap;
import java.util.Map;

public class PlannerPrompt {

    public enum Type {
        PREPARE_TO_LEAVE,
        PACK_BAGS,
        LEAVE_NOW,
        START_NAVIGATION
    }

    public String id;
    public String planId;

    public Type type;

    public String title;
    public String message;

    public Map<String, String> data = new HashMap<>();
}
