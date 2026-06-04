package com.csws.mymaps.core.flow.interfaces;

import androidx.annotation.MenuRes;
import androidx.fragment.app.Fragment;

import com.csws.mymaps.core.ui.pickers.PlaceSearchFragment;

public interface ActivityActions {
    //UI ACTIONS
    void openPlaceSearch(PlaceSearchFragment.PlaceSelectionListener listener);
    void closePlaceSearch();
    void setFabMenu(@MenuRes int menuRes);
    void showBottomSheet(Fragment fragment);
    void hideBottomSheet();
}
