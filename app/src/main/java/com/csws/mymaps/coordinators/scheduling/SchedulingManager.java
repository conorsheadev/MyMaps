package com.csws.mymaps.coordinators.scheduling;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.LifecycleOwner;

import com.csws.mymaps.core.contracts.Planner;
import com.csws.mymaps.core.contracts.services.RouteService;
import com.csws.mymaps.core.models.locations.LocationItem;
import com.csws.mymaps.core.models.navigation.NavigationEstimate;
import com.csws.mymaps.core.models.plans.PlannedStage;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.coordinators.scheduling.models.PlannerState;
import com.csws.mymaps.core.models.prompts.PlannerPrompt;
import com.csws.mymaps.core.models.prompts.PlannerPromptResult;
import com.csws.mymaps.core.models.navigation.NavigationSession;
import com.csws.mymaps.coordinators.CoordinatorContext;
import com.google.android.gms.maps.model.LatLng;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Coordinates scheduling operations for the application.
 *
 * Responsibilities:
 *
 * - Observes planned task changes.
 * - Builds planner state using PlannerEngine.
 * - Evaluates schedules at regular intervals.
 * - Dispatches prompts and navigation updates.
 * - Receives user responses to planner prompts.
 *
 * Acts as the bridge between PlannerEngine and the UI layer.
 */
public class SchedulingManager implements Planner, SchedulingTicker.Listener{

    private final CoordinatorContext context;

    private final SchedulingTicker ticker;

    private final ScheduleCalculator scheduleCalculator;
    private final PlannerEngine plannerEngine;
    private final PlannerStateBuilder plannerStateBuilder;

    private PlannerState currentState; @Override public PlannerState getCurrentState(){return currentState;}

    public SchedulingManager(CoordinatorContext context) {

        this.context = context;

        plannerEngine = new PlannerEngine();
        plannerStateBuilder = new PlannerStateBuilder();
        scheduleCalculator = new ScheduleCalculator();
        ticker = new SchedulingTicker(this);
    }

    public void observe(LifecycleOwner owner) {

        context.sessionManager.getPlansLiveData().observe(owner, this::updatePlans);
    }

    public void start() {
        ticker.start();
    }
    @Override
    public void onPlannerTick() {
        evaluatePlanner();
    }

    @Override
    public void onCountdownTick() {
        updateCountdown();
    }

    @Override
    public void onTravelEstimateTick(){ refreshTravelEstimates(); }

    private void updatePlans(List<PlannedTask> updatedPlans){
        Log.d("PlannerCoordinator", "calling updatePlans()");
        long start = System.currentTimeMillis();
        Log.d(
                "ScheduleCalculator",
                "calculateSchedules() starting"
        );

        for (PlannedTask plan : updatedPlans) {

            Log.d(
                    "ScheduleCalculator",
                    "Plan "
                            + plan.id
                            + " dirty="
                            + plan.scheduleDirty
            );

            for (PlannedStage stage : plan.stages) {

                Log.d(
                        "ScheduleCalculator",
                        "Stage "
                                + stage.title
                                + " duration="
                                + stage.scheduledDurationMillis
                );
            }
        }

        scheduleCalculator.calculateSchedules(updatedPlans);

        for (PlannedTask plan : updatedPlans) {

            Log.d(
                    "ScheduleCalculator",
                    "Plan "
                            + plan.id
                            + " estimatedScheduleMinutes="
                            + plan.estimatedScheduleMinutes
            );

            for (PlannedStage stage : plan.stages) {

                Log.d(
                        "ScheduleCalculator",
                        stage.title
                                + " start="
                                + stage.scheduledStartMillis
                                + " end="
                                + stage.scheduledEndMillis
                                + " duration="
                                + stage.scheduledDurationMillis
                );
            }
        }

        currentState = plannerStateBuilder.build(updatedPlans);

        plannerEngine.evaluate(updatedPlans, currentState);

        dispatchPrompts(currentState);
        dispatchNavigation(plannerEngine.getActiveNavigation());
    }
    private void evaluatePlanner() {

        List<PlannedTask> plans = context.sessionManager.getAllPlans();

        Log.d(
                "PersistenceTest",
                "Plans instance="
                        + System.identityHashCode(plans)
        );

        for (PlannedTask plan : plans) {

            Log.d(
                    "PersistenceTest",
                    "Plan="
                            + plan.id
                            + " instance="
                            + System.identityHashCode(plan)
            );

            for (PlannedStage stage : plan.stages) {

                Log.d(
                        "PersistenceTest",
                        "Stage="
                                + stage.id
                                + " instance="
                                + System.identityHashCode(stage)
                                + " duration="
                                + stage.scheduledDurationMillis
                );
            }
        }

        updatePlans(plans);
    }
    private void updateCountdown() {

        if (currentState.nextPlan != null) {

            long millisUntilNextPlan = currentState.nextPlan.targetStartTimeMillis - System.currentTimeMillis();
            context.uiCoordinator.showToolbarCountdown(millisUntilNextPlan);

        } else {

            context.uiCoordinator.hideToolbarCountdown();
        }
    }
    private void refreshTravelEstimates() {

        List<PlannedTask> plans =
                context.sessionManager.getAllPlans();

        for (PlannedTask plan : plans) {

            updateNavigationStages(plan);
        }
    }

    private void updateNavigationStages(PlannedTask plan) {

        for (PlannedStage stage : plan.stages) {

            if (stage.type != PlannedStage.StageType.NAVIGATION) {

                continue;
            }

            requestTravelEstimate(plan, stage);
        }
    }
    private boolean needsEstimate(PlannedStage stage) {

        if (stage.scheduledDurationMillis == null) {
            return true;
        }

        if (stage.requiresScheduleCalculation) {
            return true;
        }

        if (stage.lastCalculatedMillis == null) {
            return true;
        }

        long age =
                System.currentTimeMillis()
                        - stage.lastCalculatedMillis;

        return age > (5 * 60 * 1000);
    }

    private void requestTravelEstimate(PlannedTask plan, PlannedStage stage) {



        if (!needsEstimate(stage)) {
            return;
        }

        LatLng origin = context.uiCoordinator.getUserLocation();
        LocationItem destination = context.sessionManager.getLocationById(plan.locationId);

        if (origin == null || destination == null) {

            return;
        }

        Log.d("TravelEstimate",
                "Requesting estimate for plan="
                        + plan.id
                        + " stage="
                        + stage.id
                        + " currentDuration="
                        + stage.scheduledDurationMillis
        );

        context.routeService.estimateRoute(
                origin,
                new LatLng(destination.lat, destination.lng),
                new RouteService.TravelTimeCallback() {

                    @Override
                    public void onEstimateReady(NavigationEstimate estimate) {
                        Log.d(
                                "TravelEstimate",
                                "Estimate received for plan="
                                        + plan.id
                                        + " stage="
                                        + stage.id
                                        + " durationSeconds="
                                        + estimate.durationSeconds
                        );
                        stage.scheduledDurationMillis = estimate.durationSeconds * 1000L;
                        stage.lastCalculatedMillis = System.currentTimeMillis();
                        stage.requiresScheduleCalculation = false;
                        plan.scheduleDirty = true;
                        Log.d(
                                "TravelEstimate",
                                "Updated stage duration="
                                        + stage.scheduledDurationMillis
                                        + " scheduleDirty="
                                        + plan.scheduleDirty
                        );
                        new Handler(Looper.getMainLooper()).post(() -> {
                            evaluatePlanner();
                        });
                    }

                    @Override
                    public void onEstimateError(
                            Exception exception
                    ) {
                        Log.e("RouteService", "Route calculation failed", exception);
                    }
                }
        );
    }

    private final Set<String> notifiedPromptIds = new HashSet<>();
    private void dispatchPrompts(PlannerState state) {

        notifyPrompts();
        cleanupPromptNotifications();

        if (!context.uiCoordinator.canDisplayPlannerPrompts()) {

            return;
        }

        context.uiCoordinator.setPlannerPrompts(plannerEngine.getActivePrompts());
    }
    private void notifyPrompts() {

        List<PlannerPrompt> prompts = plannerEngine.getActivePrompts();

        for (PlannerPrompt prompt : prompts) {

            if (prompt == null) {
                continue;
            }

            if (notifiedPromptIds.contains(prompt.id)) {
                continue;
            }

            context.notificationService.showPrompt(
                    prompt.title,
                    prompt.message
            );

            notifiedPromptIds.add(prompt.id);
        }
    }
    private void cleanupPromptNotifications() {

        Set<String> activePromptIds =
                new HashSet<>();

        for (PlannerPrompt prompt :
                plannerEngine.getActivePrompts()) {

            activePromptIds.add(prompt.id);
        }

        notifiedPromptIds.retainAll(activePromptIds);
    }
    private void dispatchNavigation(NavigationSession session){

        context.uiCoordinator.setNavigationSession(session);
    }



    @Override
    public void submitPromptResult(PlannerPromptResult result) {

        Log.d("PlannerCoordinator", "Prompt Result: " + result.promptId + " -> " + result.type);
        plannerEngine.submitPromptResult(result);

        evaluatePlanner();
    }

}
