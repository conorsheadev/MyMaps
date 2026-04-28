package com.csws.mymaps.ui.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
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
import com.csws.mymaps.model.locations.LocationItem;
import com.csws.mymaps.model.tasks.TaskItem;
import com.csws.mymaps.utils.Utilities;
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

    public interface MapCallbacks {
        void onMapClicked(LatLng latLng);
        void onLocationSelected(LocationItem location);
    }

    private MapCallbacks listener; public void setListener(MapCallbacks listener){this.listener = listener;}
    private MapController_InfoWindowAdapter infoWindowAdapter; public void setInfoWindowAdapter(MapController_InfoWindowAdapter adapter) {this.infoWindowAdapter = adapter;}
    private FusedLocationProviderClient fusedLocationClient;

    private GoogleMap map;
    private boolean locationPermissionGranted = false;

    private List<Marker> activeMarkers = new ArrayList<>();
    private List<Polygon> activePolygons = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mapfragment_map, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager()
                        .findFragmentById(R.id.map);

        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.map, mapFragment)
                    .commit();
        }

        mapFragment.getMapAsync(this);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext());
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        if(cachedLocations != null){displayLocations(cachedLocations); cachedLocations = null;}
        map.setOnMarkerClickListener(this::onMarkerClicked);
        map.setOnMapClickListener(this::onMapClicked);
        try {
            this.map.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(),R.raw.style_json));
        }
        catch (Resources.NotFoundException e) {
            Log.e("MapController", "Can't find style. Error: ", e);
        }
        if(locationPermissionGranted){
            try{map.setMyLocationEnabled(true);} catch (SecurityException e){Log.e("MapViewActivity", "Error enabling map location", e);}
            moveToUserLocation();
        }

        map.setInfoWindowAdapter(infoWindowAdapter);
    }
    public void showZoomControls(boolean show){
        if(show){map.setPadding(0,100,0,150);}
        else{map.setPadding(0,0,0,0);}
        map.getUiSettings().setZoomControlsEnabled(show);
    }

    // --- MAP DATA ---
    private List<LocationItem> cachedLocations;
    public void displayLocations(List<LocationItem> locations) {
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
    public void setTasks(List<TaskItem> tasks) {
        Map<String, List<TaskItem>> grouped = new HashMap<>();

        for (TaskItem task : tasks) {
            if (!grouped.containsKey(task.locationId)) {
                grouped.put(task.locationId, new ArrayList<>());
            }
            grouped.get(task.locationId).add(task);
        }

        infoWindowAdapter.setTasks(grouped);
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
    @Override
    public void setCallbacksListener(MapCallbacks listener) {this.setListener(listener);}

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
    public void enableUserLocation() {locationPermissionGranted = true;}
    public void moveToUserLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        if (location != null && map != null) {
                            LatLng center = new LatLng(
                                    location.getLatitude(),
                                    location.getLongitude()
                            );

                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(center, 17));
                        }
                    });
        }
    }


}
