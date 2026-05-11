package com.csws.mymaps.core.flow.interfaces;

import com.csws.mymaps.domain.locations.LocationItem;

public interface FlowActions {
    void startCreateLocationFlow();
    void startCreateTaskFlow();
    void startCreateTaskFromLocationFlow(LocationItem location);
    void cancelCurrentFlow();
}
