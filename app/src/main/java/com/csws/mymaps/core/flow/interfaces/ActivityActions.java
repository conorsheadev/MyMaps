package com.csws.mymaps.core.flow.interfaces;

import androidx.annotation.MenuRes;
import androidx.fragment.app.Fragment;

import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.features.map.ui.placesearch.PlaceSearchFragment;

public interface ActivityActions {

    //NEW FLOW ACTIONS
    void startCreateLocationFlow();
    void startCreateTaskFlow();

    //UI ACTIONS
    void openPlaceSearch(PlaceSearchFragment.PlaceSelectionListener listener);
    void closePlaceSearch();
    void setFabMenu(@MenuRes int menuRes);
    void showBottomSheet(Fragment fragment);
    void hideBottomSheet();

    //REAL ACTIONS
    void createNewLocation(LocationItem locationItem);
    void createNewTask(TaskItem taskItem);
    void cancelCurrentFlow();
}
