package com.csws.mymaps.core.contracts.ui_coordinator;

import androidx.fragment.app.Fragment;

import com.csws.mymaps.core.models.locations.LocationItem;
import com.csws.mymaps.core.models.navigation.NavigationSession;
import com.csws.mymaps.core.models.prompts.PlannerPrompt;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

public interface UiCoordinator {

    //Toolbar
    void showToolbarCountdown(Long millis);
    void hideToolbarCountdown();

    //FAB
    void showDefaultFabMenu();
    void showLocationFabMenu();
    void showFabMenu(int menuRes);

    // Top Sheet
    void showTopSheet(Fragment fragment);
    void hideTopSheet();

    // Bottom Sheet
    void showBottomSheet(Fragment fragment);
    void hideBottomSheet();

    // Map
    void previewLocation(LocationItem location);
    void focusLocation(LocationItem location);
    void moveToUserLocation();
    boolean isCenteredOnUser();
    void renderTempLocation(LatLng latLng);
    void renderTempPolygon(List<LatLng> points);
    void clearTempMapObjects();
    void setMapGesturesEnabled(boolean enabled);


    // Helpers
    void showLocationDetails(LocationItem location);
    void showDayPlan();

    //PromptHandler
    //TODO: Workout how to set this up and move these funcs back to PromptHandler Interface
    void setNavigationSession(NavigationSession session);
    void setPlannerPrompts(List<PlannerPrompt> prompts);

    boolean canDisplayPlannerPrompts();
}
