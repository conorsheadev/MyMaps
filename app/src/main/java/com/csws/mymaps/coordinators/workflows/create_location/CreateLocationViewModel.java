package com.csws.mymaps.coordinators.workflows.create_location;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.csws.mymaps.core.models.locations.LocationItem;

public class CreateLocationViewModel extends ViewModel {

    private final MutableLiveData<LocationItem> draftLocation = new MutableLiveData<>();

    public void reset() {
        draftLocation.setValue(null);
    }

    // -------------------------
    // Draft Location
    // -------------------------

    public void setDraftLocation(LocationItem location) {
        draftLocation.setValue(location);
    }

    public LiveData<LocationItem> getDraftLocation() {
        return draftLocation;
    }

    public LocationItem getCurrentLocation() {
        return draftLocation.getValue();
    }
}