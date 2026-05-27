package com.csws.mymaps.features.map.coordinators;

import com.csws.mymaps.core.flow.interfaces.ActivityActions;
import com.csws.mymaps.core.flow.interfaces.FlowNavigator;
import com.csws.mymaps.core.flow.interfaces.MapActions;
import com.csws.mymaps.core.flow.interfaces.SessionActions;
import com.csws.mymaps.core.viewmodel.LocationViewModel;
import com.csws.mymaps.core.viewmodel.PlannedTaskViewModel;
import com.csws.mymaps.core.viewmodel.TaskViewModel;
import com.csws.mymaps.features.map.controllers.BottomSheetController;
import com.csws.mymaps.features.map.controllers.MapFabController;
import com.csws.mymaps.features.map.controllers.MapToolbarController;
import com.csws.mymaps.features.map.controllers.ui.TopSheetController;
import com.csws.mymaps.features.map.viewmodels.SessionViewModel;

public class FlowContext {

    // --- ViewModels ---
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


    // --- Navigation ---
    public FlowNavigator flowNavigator;
    // --- Data Actions ---
    public SessionActions sessionActions;


    public FlowContext(
            TaskViewModel taskViewModel,
            PlannedTaskViewModel plannedTaskViewModel,
            LocationViewModel locationViewModel,
            SessionViewModel sessionViewModel,

            MapToolbarController toolbarController,
            TopSheetController topSheetController,
            MapActions mapActions,
            MapFabController fabController,
            BottomSheetController bottomSheetController,

            FlowNavigator flowNavigator,
            SessionActions sessionActions
    ) {

        this.taskViewModel = taskViewModel;
        this.plannedTaskViewModel = plannedTaskViewModel;
        this.locationViewModel = locationViewModel;
        this.sessionViewModel = sessionViewModel;

        this.toolbarController = toolbarController;
        this.topSheetController = topSheetController;
        this.mapActions = mapActions;
        this.fabController = fabController;
        this.bottomSheetController = bottomSheetController;

        this.flowNavigator = flowNavigator;
        this.sessionActions = sessionActions;
    }

    public void bootstrap(FlowNavigator flowNavigator, SessionActions sessionActions){
        this.flowNavigator = flowNavigator;
        this.sessionActions = sessionActions;
    }
}
