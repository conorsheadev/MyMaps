package com.csws.mymaps.coordinators.session;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.csws.mymaps.core.models.locations.LocationItem;
import com.csws.mymaps.core.viewmodels.locations.LocationViewModel;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.core.viewmodels.plans.PlannedTaskViewModel;
import com.csws.mymaps.core.models.tasks.TaskCollection;
import com.csws.mymaps.core.models.tasks.TaskItem;
import com.csws.mymaps.core.viewmodels.collections.TaskCollectionViewModel;
import com.csws.mymaps.core.viewmodels.sessions.SessionViewModel;
import com.csws.mymaps.core.viewmodels.tasks.TaskViewModel;
import com.csws.mymaps.core.models.sessions.DailySession;
import com.csws.mymaps.core.models.sessions.SessionStartType;
import com.csws.mymaps.coordinators.CoordinatorContext;
import com.csws.mymaps.coordinators.session.workflows.InitialiseSessionWorkflow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class SessionManager implements com.csws.mymaps.core.contracts.SessionManager {


    private final AppCompatActivity activity;
    private final CoordinatorContext context;

    private TaskCollectionViewModel taskCollectionViewModel;
    private TaskViewModel taskViewModel;
    private PlannedTaskViewModel plannedTaskViewModel;
    private LocationViewModel locationViewModel;
    private SessionViewModel sessionViewModel;

    public SessionManager(AppCompatActivity activity, CoordinatorContext context) {
        this.activity = activity;
        this.context = context;

        ViewModelProvider vmProvider = new ViewModelProvider(activity);

        taskCollectionViewModel = vmProvider.get(TaskCollectionViewModel.class);
        taskViewModel = vmProvider.get(TaskViewModel.class);
        plannedTaskViewModel = vmProvider.get(PlannedTaskViewModel.class);
        locationViewModel = vmProvider.get(LocationViewModel.class);
        sessionViewModel = vmProvider.get(SessionViewModel.class);
    }

    public void start() {



        context.workflowManager.finishWorkflow();

        boolean hasSession = hasSessionToday();

        if (hasSession) {
            loadTodaySession();

        } else {
            context.workflowManager.startWorkflow(new InitialiseSessionWorkflow(context));
        }
    }

    // --- Retrival ---
    @Override
    public DailySession getCurrentSession() {

        DailySession session = sessionViewModel.getCurrentSession().getValue();
        return session != null ? session : new DailySession();
    }
    @Override
    public List<TaskItem> getAllTasks() {

        List<TaskItem> tasks = taskViewModel.getTasks().getValue();
        return tasks != null ? tasks : new ArrayList<>();
    }

    @Override
    public List<PlannedTask> getAllPlans() {

        List<PlannedTask> plans = plannedTaskViewModel.getPlannedTasks().getValue();
        return plans != null ? plans : new ArrayList<>();
    }
    @Override
    public List<TaskCollection> getAllCollections() {

        List<TaskCollection> collections = taskCollectionViewModel.getCollections().getValue();
        return collections != null ? collections : new ArrayList<>();
    }
    @Override
    public List<LocationItem> getAllLocations() {

        List<LocationItem> locations = locationViewModel.getLocations().getValue();
        return locations != null ? locations : new ArrayList<>();
    }

    // --- Queries ---
    @Override
    public List<TaskItem> getTasksForLocation(String locationId) {

        List<PlannedTask> plans = getPlansForLocation(locationId);
        Set<String> taskIds = new HashSet<>();
        for (PlannedTask plan : plans) {

            if (plan.taskId != null) {
                taskIds.add(plan.taskId);
            }
        }

        return taskViewModel.getTasksByIds(taskIds);
    }
    @Override
    public List<PlannedTask> getPlansForLocation(String locationId) {
        return plannedTaskViewModel.getPlansForLocation(locationId);
    }
    @Override
    public LocationItem getLocationById(String id) {

        return locationViewModel.getLocationById(id);
    }

    // --- LiveData ---
    @Override
    public LiveData<List<PlannedTask>> getPlansLiveData() {
        return plannedTaskViewModel.getPlannedTasks();
    }
    @Override
    public LiveData<List<TaskItem>> getTasksLiveData() {
        return taskViewModel.getTasks();
    }
    @Override
    public LiveData<List<LocationItem>> getLocationsLiveData() {
        return locationViewModel.getLocations();
    }

    // --- Session Management ---
    @Override
    public boolean hasSessionToday() {
        return sessionViewModel.hasSessionToday();
    }
    @Override
    public void createSession(SessionStartType type) {
        sessionViewModel.createSession(type);
    }
    @Override
    public void loadTodaySession() {
        sessionViewModel.loadTodaySession();
    }
    @Override
    public void loadLatestSession() {
        sessionViewModel.loadLatestSession();
    }

}
