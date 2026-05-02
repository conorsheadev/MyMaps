package com.csws.mymaps.features.map.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.csws.mymaps.domain.flows.CreateLocationState;

public class CreateLocationViewModel extends ViewModel {

    private final MutableLiveData<CreateLocationState> state =
            new MutableLiveData<>(new CreateLocationState());

    public LiveData<CreateLocationState> getState() {
        return state;
    }

    public CreateLocationState getCurrent() {
        return state.getValue();
    }

    public void update(CreateLocationState newState) {
        state.setValue(newState);
    }
}
