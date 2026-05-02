package com.csws.mymaps.features.map.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.csws.mymaps.domain.locations.LocationItem;

public class CreateTaskViewModel extends ViewModel {

    public enum Stage {
        SELECT_LOCATION,
        CONFIGURE_TASK
    }

    private final MutableLiveData<Stage> stage = new MutableLiveData<>(Stage.SELECT_LOCATION);

    private final MutableLiveData<LocationItem> selectedLocation = new MutableLiveData<>();
    private final MutableLiveData<String> taskName = new MutableLiveData<>("");
    private final MutableLiveData<String> taskDescription = new MutableLiveData<>("");

    // --- Stage ---
    public LiveData<Stage> getStage() {
        return stage;
    }

    public void setStage(Stage newStage) {
        stage.setValue(newStage);
    }

    // --- Location ---
    public void setLocation(LocationItem location) {
        selectedLocation.setValue(location);
    }

    public LiveData<LocationItem> getLocation() {
        return selectedLocation;
    }

    public LocationItem getCurrentLocation() {
        return selectedLocation.getValue();
    }

    // --- Task Data ---
    public void setTaskName(String name) {
        taskName.setValue(name);
    }

    public String getTaskName() {
        return taskName.getValue();
    }

    public void setTaskDescription(String desc) {
        taskDescription.setValue(desc);
    }

    public String getTaskDescription() {
        return taskDescription.getValue();
    }

    // --- Reset ---
    public void reset() {
        stage.setValue(Stage.SELECT_LOCATION);
        selectedLocation.setValue(null);
        taskName.setValue("");
        taskDescription.setValue("");
    }
}
