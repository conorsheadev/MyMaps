package com.csws.mymaps.features.map.controllers.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.csws.mymaps.R;
import com.csws.mymaps.core.utils.Utilities;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.core.flow.interfaces.MapActions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapFragment extends Fragment implements OnMapReadyCallback, MapActions {
    //Debugging
    private static final String TAG = "MapFragment";

    public interface OnMapLoadedListener {
        void onMapLoaded();
    }
    private OnMapLoadedListener onMapLoadedListener; public void setOnMapLoadedListener(OnMapLoadedListener onMapLoadedListener) {this.onMapLoadedListener = onMapLoadedListener;}

    public interface OnMapPreparedListener {
        void onMapPrepared();
    }
    private OnMapPreparedListener onMapPreparedListener; public void setOnMapPreparedListener(OnMapPreparedListener listener) {this.onMapPreparedListener = listener;}
    private boolean mapPreparedDispatched = false;

    public interface MapCallbacks {
        void onMapClicked(LatLng latLng);
        void onLocationSelected(LocationItem location);
        void onRecenterClicked();
    }
    private MapCallbacks listener; public void setListener(MapCallbacks listener){this.listener = listener;}
    private MapController_InfoWindowAdapter infoWindowAdapter; public void setInfoWindowAdapter(MapController_InfoWindowAdapter adapter) {this.infoWindowAdapter = adapter;}
    private FusedLocationProviderClient fusedLocationClient;

    private GoogleMap map;
    private boolean userLocationEnabled = false;

    private LatLng lastUserLatLng;
    private List<Marker> activeMarkers = new ArrayList<>();
    private List<Polygon> activePolygons = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        /*Debug*/Log.d(TAG, "onCreateView");
        return inflater.inflate(R.layout.mapfragment_map, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Debug*/Log.d(TAG, "onViewCreated START");

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

        /*Debug*/Log.d(TAG, "Existing child map fragment = " + (mapFragment != null));

        if (mapFragment == null) {

            /*Debug*/Log.d(TAG, "Creating new SupportMapFragment");

            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map, mapFragment)
                    .commit();

            /*Debug*/Log.d(TAG, "SupportMapFragment transaction committed");
        }

        /*Debug*/Log.d(TAG, "Calling getMapAsync");

        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());

        /*Debug*/Log.d(TAG, "FusedLocationProviderClient initialised");

        /*Debug*/Log.d(TAG, "onViewCreated END");
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady START");

        this.map = googleMap;

        Log.d(TAG, "GoogleMap assigned");

        if(cachedLocations != null){

            Log.d(TAG, "Rendering cached locations: " + cachedLocations.size());
            displayLocations(cachedLocations); cachedLocations = null;
        }

        Log.d(TAG, "Setting map listeners");

        map.setOnMarkerClickListener(this::onMarkerClicked);
        map.setOnMapClickListener(this::onMapClicked);
        try {

            Log.d(TAG, "Applying map style");

            this.map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(),R.raw.style_json));

            Log.d(TAG, "Map style applied");
        }
        catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style", e);
        }

        Log.d(TAG, "Applying user location state");

        applyUserLocationState();

        map.setOnCameraIdleListener(() -> {

            Log.d(TAG, "Camera idle");

            if (userLocationEnabled && lastUserLatLng != null) {
                dispatchMapPrepared();
            }
        });

        map.setInfoWindowAdapter(infoWindowAdapter);

        Log.d(TAG, "InfoWindowAdapter attached");

        map.setOnMapLoadedCallback(() -> {

            Log.d(TAG, "MAP TILES FULLY LOADED");

            if(onMapLoadedListener != null){

                Log.d(TAG, "Dispatching onMapLoadedListener");

                onMapLoadedListener.onMapLoaded();
                //onMapLoadedListener = null;
            }
        });

        Log.d(TAG, "onMapReady END");
    }

    private void dispatchMapPrepared() {

        if (mapPreparedDispatched) {
            return;
        }

        mapPreparedDispatched = true;

        Log.d(TAG, "MAP PREPARED");

        if (onMapPreparedListener != null) {

            Log.d(TAG, "Dispatching onMapPreparedListener");

            onMapPreparedListener.onMapPrepared();
        }
    }

    public void showZoomControls(boolean show){
        if(show){map.setPadding(0,100,0,150);}
        else{map.setPadding(0,0,0,0);}
        map.getUiSettings().setZoomControlsEnabled(show);
    }

    // --- MAP DATA ---
    private List<LocationItem> cachedLocations;
    public void displayLocations(List<LocationItem> locations) {
        Log.d("MapFragment", "displayLocations: " + locations.size());
        //return;

        if (map == null) {cachedLocations = locations; return;}

        map.clear();
        activeMarkers.clear();
        activePolygons.clear();

        for (LocationItem loc : locations) {
            // --- DRAW POLYGON ---
            PolygonOptions polygonOptions = new PolygonOptions()
                    .addAll(loc.polygonConfig.points)
                    .strokeWidth(4);
                    //.strokeColor(getStrokeColor(loc))
                    //.fillColor(getFillColor(loc));

            Polygon polygon = map.addPolygon(polygonOptions);
            polygon.setTag(loc);
            activePolygons.add(polygon);

            // --- DRAW MARKER ---
            LatLng position = new LatLng(loc.lat, loc.lng);

            MarkerOptions options = new MarkerOptions();
            options.position(position);
            options.title(loc.name);

            if(loc.markerConfig.iconType.equals("default")){options.icon(BitmapDescriptorFactory.defaultMarker(loc.markerConfig.colorHue));}
            else{
                options.icon(BitmapDescriptorFactory.fromResource(Utilities.getIconResource24px(loc.markerConfig.iconType)));
            }

            Marker marker = map.addMarker(options);
            marker.setTag(loc);

            activeMarkers.add(marker);
        }
    }
    public void setTasks(List<TaskItem> tasks, List<PlannedTask> plannedTasks) {
        //TaskItem Lookup
        Map<String, TaskItem> taskLookup = new HashMap<>();
        for (TaskItem task : tasks) {
            taskLookup.put(task.id, task);
        }
        //plannedTasks grouped by location
        Map<String, List<PlannedTask>> grouped = new HashMap<>();
        for (PlannedTask plannedTask : plannedTasks) {
            TaskItem task = taskLookup.get(plannedTask.taskId);
            if (task == null || task.locationId == null) { continue;}
            if (!grouped.containsKey(task.locationId)) {
                grouped.put(task.locationId, new ArrayList<>());
            }
            grouped.get(task.locationId).add(plannedTask);
        }

        //Adapter refresh
        infoWindowAdapter.setTasks(grouped, taskLookup);
        refreshInfoWindows();
    }
    public void refreshInfoWindows() {
        if (map == null) return;

        Marker focused = null;

        for (Marker marker : activeMarkers) {
            if (marker.isInfoWindowShown()) {
                focused = marker;
                break;
            }
        }

        if (focused != null) {
            focused.hideInfoWindow();
            focused.showInfoWindow();
        }
    }

    // --- Map Actions ---
    public void previewLocation(LocationItem location){
        if (map == null) return;

        for (Marker marker : activeMarkers) {
            Object tag = marker.getTag();
            if (tag instanceof LocationItem) {
                LocationItem loc = (LocationItem) tag;

                if (loc.id.equals(location.id)) {
                    marker.showInfoWindow();
                    return;
                }
            }
        }
    }

    public void focusLocation(LocationItem location) {
        if (map == null) return;

        for (Marker marker : activeMarkers) {
            Object tag = marker.getTag();
            if (tag instanceof LocationItem) {
                LocationItem loc = (LocationItem) tag;

                if (loc.id.equals(location.id)) {
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), 17));
                    return;
                }
            }
        }
    }

    @Override
    public void setMapGesturesEnabled(boolean enabled) {
        if (map == null) return;

        UiSettings ui = map.getUiSettings();
        ui.setScrollGesturesEnabled(enabled);
        ui.setZoomGesturesEnabled(enabled);
        ui.setTiltGesturesEnabled(enabled);
        ui.setRotateGesturesEnabled(enabled);
    }

    private Marker tempMarker;
    @Override
    public void renderTempLocation(LatLng latLng) {
        if (tempMarker != null) { tempMarker.remove(); }

        tempMarker = map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Temporary Location"));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
    }

    private Polygon tempPolygon;
    private List<Marker> tempPolygonMarkers = new ArrayList<>();
    @Override
    public void renderTempPolygon(List<LatLng> points) {
        //Remove old polygon and markers if they exist
        if (tempPolygon != null) {
            tempPolygon.remove();
        }
        for(Marker marker : tempPolygonMarkers){ marker.remove(); }

        //Add temporary markers for each point
        for(LatLng point : points){
            Marker marker = map.addMarker(new MarkerOptions()
                    .position(point)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
            );
            marker.setTag("TEMP_POLYGON_POINT");
            tempPolygonMarkers.add(marker);
        }

        //If +2 points provided -> draw polygon
        if (points.size() < 2) return;
        tempPolygon = map.addPolygon(new PolygonOptions()
                .addAll(points)
                .strokeWidth(4)
                .strokeColor(Color.BLUE)
                .fillColor(0x220000FF));
    }

    @Override
    public void clearTemp() {
        //Clear Marker
        if (tempMarker != null) {
            tempMarker.remove();
            tempMarker = null;
        }

        //Clear Polygon
        if (tempPolygon != null) {
            tempPolygon.remove();
            tempPolygon = null;
        }
        for(Marker marker : tempPolygonMarkers){ marker.remove(); }
    }

    @Override
    public boolean isCenteredOnUser() {

        if (map == null || lastUserLatLng == null) {
            return false;
        }

        LatLng target = map.getCameraPosition().target;

        float[] results = new float[1];

        Location.distanceBetween(
                target.latitude,
                target.longitude,
                lastUserLatLng.latitude,
                lastUserLatLng.longitude,
                results
        );

        return results[0] < 50;
    }

    // --- Map Callbacks ---
    private boolean mapClicksEnabled = true;
    public void setMapClicksEnabled(boolean enabled) {mapClicksEnabled = enabled;}
    public boolean isMapClicksEnabled() {return mapClicksEnabled;}

    private boolean onMarkerClicked(Marker marker) {
        if (!mapClicksEnabled) {
            return true;
        }

        Object tag = marker.getTag();
        if (tag instanceof LocationItem && listener != null) {
            listener.onLocationSelected((LocationItem) tag);
        }

        return true;
    }
    private boolean onMapClicked(LatLng latLng) {
        if (!mapClicksEnabled) {
            return true;
        }

        if (listener != null) {
            listener.onMapClicked(latLng);
        }

        return true;
    }

    // --- User Location ---
    public void enableUserLocation() {

        userLocationEnabled = true;

        applyUserLocationState();

        moveToUserLocation();
    }
    private void applyUserLocationState() {

        if (map == null) {return;}
        if (!userLocationEnabled) {return;}
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {return;}

        try {

            map.setMyLocationEnabled(true);

            map.setOnMyLocationButtonClickListener(() -> {

                if (listener != null) {
                    listener.onRecenterClicked();
                }

                return false;
            });

        } catch (SecurityException e) {

            Log.e("MapFragment", "Failed to enable user location", e);
        }
    }
    public void moveToUserLocation() {
        Log.d(TAG, "moveToUserLocation START");
        if (map == null) { Log.d(TAG, "moveToUserLocation ABORT map == null"); return; }
        if (!userLocationEnabled) { Log.d(TAG, "moveToUserLocation ABORT userLocationEnabled == false"); return; }
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { Log.d(TAG, "moveToUserLocation ABORT permission denied"); return; }

        Log.d(TAG, "Requesting last location");

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(location -> {

                    Log.d(TAG, "getLastLocation SUCCESS");

                    if (location != null && map != null) {

                        Log.d(TAG, "Location received: " + location.getLatitude() + ", " + location.getLongitude());

                        LatLng center = new LatLng(
                                location.getLatitude(),
                                location.getLongitude()
                        );
                        lastUserLatLng = center;

                        Log.d(TAG, "Moving camera to user");

                        map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 17));

                        Log.d(TAG, "Camera move complete");
                    }
                })
                .addOnFailureListener(e -> {

                    Log.e(TAG, "getLastLocation FAILED", e);
                });

    }


}
