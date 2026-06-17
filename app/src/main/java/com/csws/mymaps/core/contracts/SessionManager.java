package com.csws.mymaps.core.contracts;

import androidx.lifecycle.LiveData;

import com.csws.mymaps.core.models.locations.LocationItem;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.core.models.tasks.TaskCollection;
import com.csws.mymaps.core.models.tasks.TaskItem;
import com.csws.mymaps.core.models.sessions.DailySession;
import com.csws.mymaps.core.models.sessions.SessionStartType;

import java.util.List;

public interface SessionManager {
    //Retrieval
    public DailySession getCurrentSession();
    public List<TaskItem> getAllTasks();
    public List<PlannedTask> getAllPlans();
    public List<TaskCollection> getAllCollections();
    public List<LocationItem> getAllLocations();

    //Queries
    public List<TaskItem> getTasksForLocation(String locationId);
    public List<PlannedTask> getPlansForLocation(String locationId);
    public LocationItem getLocationById(String locationId);

    //Session Management
    boolean hasSessionToday();
    void createSession(SessionStartType type);
    void loadTodaySession();
    void loadLatestSession();

    //LiveData
    LiveData<List<LocationItem>> getLocationsLiveData();
    LiveData<List<TaskItem>> getTasksLiveData();
    LiveData<List<PlannedTask>> getPlansLiveData();

}
