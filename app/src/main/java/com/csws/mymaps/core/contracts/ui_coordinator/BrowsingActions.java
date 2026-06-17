package com.csws.mymaps.core.contracts.ui_coordinator;


import com.csws.mymaps.core.models.locations.LocationItem;

public interface BrowsingActions {

    void startCreateLocation();

    void startCreateTask();

    void startCreatePlan();

    void startCreateCollection();

    void startCreateTaskFromLocation(LocationItem location);
}