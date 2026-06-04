package com.csws.mymaps.features.planner.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csws.mymaps.data.repositories.ConfigRepository;
import com.csws.mymaps.domain.planner.PlannerConfig;

import java.time.DayOfWeek;
import java.time.LocalTime;

public class PlannerConfigViewModel extends AndroidViewModel {

    private final ConfigRepository repository;
    private final MutableLiveData<PlannerConfig> config = new MutableLiveData<>(new PlannerConfig());

    public PlannerConfigViewModel(@NonNull Application application)
    {
        super(application);

        repository = new ConfigRepository(application);
        loadConfig();
    }
    private void loadConfig(){
        PlannerConfig loaded = repository.loadConfig();
        config.setValue(loaded);
    }
    public void saveConfig(){
        PlannerConfig current = config.getValue();
        repository.saveConfig(current);
    }

    public LiveData<PlannerConfig> getConfig() {
        return config;
    }
    public void setWakeUpTime(DayOfWeek day, LocalTime time) {

        PlannerConfig current = config.getValue();

        if (current == null) {
            current = new PlannerConfig();
        }

        current.setWakeUpTime(day, time);

        config.setValue(current);
        saveConfig();
    }
}
