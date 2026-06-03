package com.csws.mymaps.features.map.coordinators;

import com.csws.mymaps.core.flow.interfaces.coordinator_interfaces.PromptHandler;
import com.csws.mymaps.core.flow.interfaces.coordinator_interfaces.WorkflowNavigator;
import com.csws.mymaps.core.flow.interfaces.MapActions;
import com.csws.mymaps.core.flow.interfaces.coordinator_interfaces.SessionActions;
import com.csws.mymaps.core.viewmodel.LocationViewModel;
import com.csws.mymaps.core.viewmodel.PlannedTaskViewModel;
import com.csws.mymaps.core.viewmodel.TaskCollectionViewModel;
import com.csws.mymaps.core.viewmodel.TaskViewModel;
import com.csws.mymaps.features.map.controllers.BottomSheetController;
import com.csws.mymaps.features.map.controllers.MapFabController;
import com.csws.mymaps.features.map.controllers.MapToolbarController;
import com.csws.mymaps.features.map.controllers.TopSheetController;
import com.csws.mymaps.features.map.viewmodels.SessionViewModel;

public class MapViewContext {

    // --- ViewModels ---
    public final TaskCollectionViewModel taskCollectionViewModel;
    public final TaskViewModel taskViewModel;
    public final PlannedTaskViewModel plannedTaskViewModel;
    public final LocationViewModel locationViewModel;
    public final SessionViewModel sessionViewModel;

    // --- UI ---
    public final MapToolbarController toolbarController;
    public final TopSheetController topSheetController;
    public final MapActions mapActions;
    public final MapFabController fabController;
    public final BottomSheetController bottomSheetController;


    // --- Workflow Navigation ---
    public WorkflowNavigator workflowNavigator;
    // --- Data Actions ---
    public SessionActions sessionActions;
    // --- Prompt Handler ---
    public PromptHandler promptHandler;

    public MapViewContext(
            TaskCollectionViewModel taskCollectionViewModel,
            TaskViewModel taskViewModel,
            PlannedTaskViewModel plannedTaskViewModel,
            LocationViewModel locationViewModel,
            SessionViewModel sessionViewModel,

            MapToolbarController toolbarController,
            TopSheetController topSheetController,
            MapActions mapActions,
            MapFabController fabController,
            BottomSheetController bottomSheetController,

            WorkflowNavigator workflowNavigator,
            SessionActions sessionActions,
            PromptHandler promptHandler
    ) {

        this.taskCollectionViewModel = taskCollectionViewModel;
        this.taskViewModel = taskViewModel;
        this.plannedTaskViewModel = plannedTaskViewModel;
        this.locationViewModel = locationViewModel;
        this.sessionViewModel = sessionViewModel;

        this.toolbarController = toolbarController;
        this.topSheetController = topSheetController;
        this.mapActions = mapActions;
        this.fabController = fabController;
        this.bottomSheetController = bottomSheetController;

        this.workflowNavigator = workflowNavigator;
        this.sessionActions = sessionActions;
        this.promptHandler = promptHandler;
    }

    public void bindWorkflowServices(WorkflowNavigator workflowNavigator, SessionActions sessionActions, PromptHandler promptHandler){
        this.workflowNavigator = workflowNavigator;
        this.sessionActions = sessionActions;
        this.promptHandler = promptHandler;
    }
}
