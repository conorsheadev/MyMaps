package com.csws.mymaps.map.tasks_and_locations;

import android.util.Log;

import com.csws.mymaps.data.locations.LocationItem;
import com.csws.mymaps.data.locations.LocationRepository;
import com.csws.mymaps.data.locations.MarkerConfig;
import com.csws.mymaps.data.locations.PolygonConfig;

import java.util.List;
import java.util.UUID;

public class LocationManager {
    private final LocationRepository repository;
    private List<LocationItem> locations;

    public LocationManager(LocationRepository repository) {
        this.repository = repository;
        this.locations = repository.loadLocations();
    }
    public List<LocationItem> getLocations() {
        Log.d("LocationManager","LocationRepository returned:"+locations.stream().count());
        return locations;
    }

    public LocationItem addLocation(String name, double lat, double lng, String type, PolygonConfig polygonConfig, MarkerConfig markerConfig) {

        LocationItem item = new LocationItem(
                UUID.randomUUID().toString(),
                name,
                lat,
                lng,
                polygonConfig,
                markerConfig
        );
        return addLocation(item);
    }
    public LocationItem addLocation(LocationItem item) {
        locations.add(item);
        repository.saveLocations(locations);
        return item;
    }
    public void removeLocation(String id) {
        locations.removeIf(loc -> loc.id.equals(id));
        repository.saveLocations(locations);
    }
    public LocationItem getLocationById(String id) {
        for (LocationItem loc : locations) {
            if (loc.id.equals(id)) return loc;
        }
        return null;
    }
}
