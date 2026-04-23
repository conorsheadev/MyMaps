package com.csws.mymaps.ui.map.deprecated;

import com.csws.mymaps.model.locations.LocationItem;
import com.csws.mymaps.model.tasks.TaskItem;

public interface ActivityActions {
    void createNewLocation(LocationItem locationItem);
    void createNewTask(TaskItem taskItem);
    void cancelCurrentFlow();
}
