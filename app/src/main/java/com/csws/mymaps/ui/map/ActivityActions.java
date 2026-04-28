package com.csws.mymaps.ui.map;

import androidx.annotation.MenuRes;
import androidx.fragment.app.Fragment;

import com.csws.mymaps.model.locations.LocationItem;
import com.csws.mymaps.model.tasks.TaskItem;
import com.csws.mymaps.ui.places.PlaceSearchFragment;

import java.util.ResourceBundle;

public interface ActivityActions {

    //NEW FLOW ACTIONS
    void startCreateLocationFlow();

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
