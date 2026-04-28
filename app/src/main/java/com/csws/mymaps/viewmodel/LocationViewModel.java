package com.csws.mymaps.viewmodel;

import android.util.Log;
import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.AndroidViewModel;

import com.csws.mymaps.model.locations.LocationItem;
import com.csws.mymaps.data.LocationRepository;
import com.csws.mymaps.model.locations.MarkerConfig;
import com.csws.mymaps.model.locations.PolygonConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LocationViewModel extends AndroidViewModel {
    private final LocationRepository repository;
    private final MutableLiveData<List<LocationItem>> locations = new MutableLiveData<>();

    public LocationViewModel(@NonNull Application application) {
        super(application);
        repository = new LocationRepository(application);
        loadLocations();
    }
    private void loadLocations() {
        List<LocationItem> loaded = repository.loadLocations();
        locations.setValue(loaded);
    }
    public LiveData<List<LocationItem>> getLocations() {
        return locations;
    }

    public LocationItem addLocation(String name, String type, double lat, double lng, PolygonConfig polygonConfig, MarkerConfig markerConfig) {

        LocationItem item = new LocationItem(
                UUID.randomUUID().toString(),
                name,
                type,
                lat,
                lng,
                polygonConfig,
                markerConfig
        );
        return addLocation(item);
    }
    public LocationItem addLocation(LocationItem item) {
        List<LocationItem> current = locations.getValue();
        if (current == null) current = new ArrayList<>();

        current.add(item);
        repository.saveLocations(current);
        locations.setValue(current);
        return item;
    }
    public void removeLocation(String id) {
        List<LocationItem> current = locations.getValue();
        if (current == null) return;

        current.removeIf(loc -> loc.id.equals(id));
        repository.saveLocations(current);
        locations.setValue(current);
    }
    public LocationItem getLocationById(String id) {
        List<LocationItem> current = locations.getValue();
        if (current == null) return null;

        for (LocationItem loc : current) {
            if (loc.id.equals(id)) return loc;
        }
        return null;
    }
}
