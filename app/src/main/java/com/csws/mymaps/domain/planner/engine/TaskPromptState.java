package com.csws.mymaps.domain.planner.engine;

import java.util.HashSet;
import java.util.Set;

public class TaskPromptState {

    public String taskId;

    // Prompt tracking
    public final Set<String> shownPrompts = new HashSet<>();

    // User responses
    public boolean bagsPacked;
    public boolean leaveConfirmed;

    // Future
    public boolean routeViewed;
    public boolean navigationStarted;

}
