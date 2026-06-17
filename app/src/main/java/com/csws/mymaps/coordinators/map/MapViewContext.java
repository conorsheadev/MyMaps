package com.csws.mymaps.coordinators.map;

import com.csws.mymaps.core.contracts.PromptResultListener;
import com.csws.mymaps.core.contracts.WorkflowManager;
import com.csws.mymaps.core.contracts.services.EntityCreationService;
import com.csws.mymaps.core.contracts.ui_coordinator.UiCoordinator;
import com.csws.mymaps.core.contracts.services.RouteService;
import com.csws.mymaps.coordinators.session.SessionManager;

public class MapViewContext {



    // --- Coordinators ---
    public WorkflowManager workflowManager; //Workflow Coordinator
    public UiCoordinator uiCoordinator; //MapView Coordinator
    public PromptResultListener promptResultListener; //Scheduling Coordinator
    public SessionManager sessionManager; //SessionManager

    // --- Services ---
    public EntityCreationService entityCreationService;
    public RouteService routeService;


    public MapViewContext(
            WorkflowManager workflowManager,
            EntityCreationService entityCreationService,
            UiCoordinator uiCoordinator,
            PromptResultListener promptResultListener,
            RouteService routeService
    ) {

        this.workflowManager = workflowManager;
        this.entityCreationService = entityCreationService;
        this.uiCoordinator = uiCoordinator;
        this.promptResultListener = promptResultListener;
        this.routeService = routeService;
    }

    public void bindCoordinators(WorkflowManager workflowManager, UiCoordinator uiCoordinator, PromptResultListener promptResultListener, SessionManager sessionManager){
        this.workflowManager = workflowManager;
        this.uiCoordinator = uiCoordinator;
        this.promptResultListener = promptResultListener;
        this.sessionManager = sessionManager;
    }
    public void bindWorkflowServices(EntityCreationService entityCreationService, RouteService routeService){
        this.entityCreationService = entityCreationService;
        this.routeService = routeService;
    }
}
