package com.csws.mymaps.coordinators;

import com.csws.mymaps.core.contracts.Planner;
import com.csws.mymaps.core.contracts.WorkflowManager;
import com.csws.mymaps.core.contracts.services.EntityCreationService;
import com.csws.mymaps.core.contracts.services.NotificationService;
import com.csws.mymaps.core.contracts.ui_coordinator.UiCoordinator;
import com.csws.mymaps.core.contracts.services.RouteService;
import com.csws.mymaps.coordinators.session.SessionManager;

public class CoordinatorContext {



    // --- Coordinators ---
    public WorkflowManager workflowManager; //Workflow Coordinator
    public UiCoordinator uiCoordinator; //MapView Coordinator
    public Planner promptResultListener; //Scheduling Coordinator
    public SessionManager sessionManager; //SessionManager

    // --- Services ---
    public EntityCreationService entityCreationService;
    public RouteService routeService;
    public NotificationService notificationService;


    public CoordinatorContext(
            WorkflowManager workflowManager,
            EntityCreationService entityCreationService,
            UiCoordinator uiCoordinator,
            Planner promptResultListener,
            RouteService routeService,
            NotificationService notificationService
    ) {

        this.workflowManager = workflowManager;
        this.entityCreationService = entityCreationService;
        this.uiCoordinator = uiCoordinator;
        this.promptResultListener = promptResultListener;
        this.routeService = routeService;
        this.notificationService = notificationService;
    }

    public void bindCoordinators(WorkflowManager workflowManager, UiCoordinator uiCoordinator, Planner promptResultListener, SessionManager sessionManager){
        this.workflowManager = workflowManager;
        this.uiCoordinator = uiCoordinator;
        this.promptResultListener = promptResultListener;
        this.sessionManager = sessionManager;
    }
    public void bindWorkflowServices(EntityCreationService entityCreationService, RouteService routeService, NotificationService notificationService){
        this.entityCreationService = entityCreationService;
        this.routeService = routeService;
        this.notificationService = notificationService;
    }
}
