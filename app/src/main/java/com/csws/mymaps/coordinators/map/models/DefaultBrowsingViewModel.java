package com.csws.mymaps.coordinators.map.models;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.csws.mymaps.core.models.locations.LocationItem;

public class DefaultBrowsingViewModel extends ViewModel {

    private final MutableLiveData<LocationItem> selectedLocation = new MutableLiveData<>();

    public void setSelectedLocation(LocationItem location) {
        selectedLocation.setValue(location);
    }

    public LiveData<LocationItem> getSelectedLocation() {
        return selectedLocation;
    }

    public LocationItem getCurrentLocation() {
        return selectedLocation.getValue();
    }

    public void clearSelection() {
        selectedLocation.setValue(null);
    }
}
