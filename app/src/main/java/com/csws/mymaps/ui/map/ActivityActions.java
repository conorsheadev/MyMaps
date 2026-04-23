package com.csws.mymaps.ui.map;

import com.csws.mymaps.model.locations.LocationItem;
import com.csws.mymaps.model.tasks.TaskItem;

public interface ActivityActions {

    void openPlaceSearch();

    void createNewLocation(LocationItem locationItem);
    void createNewTask(TaskItem taskItem);
    void cancelCurrentFlow();
}
