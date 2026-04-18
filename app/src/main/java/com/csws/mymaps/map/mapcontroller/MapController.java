package com.csws.mymaps.map.mapcontroller;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.csws.mymaps.R;
import com.csws.mymaps.data.locations.LocationItem;
import com.csws.mymaps.map.tasks_and_locations.TaskManager;
import com.csws.mymaps.utils.Utilities;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;

public class MapController implements OnMapReadyCallback {
    public interface MarkerClickListener {
        void onLocationSelected(LocationItem location);
        void onMapClicked(); // optional (clear selection)
    }

    private final Context context;
    private MarkerClickListener listener;
    private MapController_InfoWindowAdapter infoWindowAdapter;
    private List<Marker> activeMarkers = new ArrayList<>();
    private List<Polygon> activePolygons = new ArrayList<>();

    private boolean locationPermissionGranted = false;

    private GoogleMap map;
    private final FusedLocationProviderClient fusedLocationClient;

    public MapController(Context context, MarkerClickListener listener, TaskManager taskManager) {
        this.context = context;
        this.listener = listener;
        this.infoWindowAdapter = new MapController_InfoWindowAdapter(context, taskManager);
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
        if(cachedLocations != null){displayLocations(cachedLocations); cachedLocations = null;}
        map.setOnMarkerClickListener(this::onMarkerClicked);
        map.setOnMapClickListener(this::onMapClicked);
        try {
            this.map.setMapStyle(MapStyleOptions.loadRawResourceStyle(context,R.raw.style_json));
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

    // --- Draw Polygon Mode ---
    private boolean drawMode = false;
    private Polygon tempPolygon;
    private List<LatLng> tempPoints = new ArrayList<>();
    private List<Marker> tempPolygonMarkers = new ArrayList<>();
    public void enableDrawMode() {
        drawMode = true;
        tempPoints.clear();
        tempPolygonMarkers.clear();
        showZoomControls(true);
    }
    public void displayTemporaryPolygon(List<LatLng> points) {
        if (map == null || points == null) return;
        for(Marker marker : tempPolygonMarkers){ marker.remove(); }
        for(LatLng point : points){
            addTempMarker(point);
        }
        if (points.size() < 3) return;

        if (tempPolygon != null) {
            tempPolygon.remove();
        }

        tempPolygon = map.addPolygon(new PolygonOptions()
                .addAll(points)
                .strokeWidth(4)
                .strokeColor(Color.BLUE)
                .fillColor(0x220000FF));
    }
    private void addTempMarker(LatLng point) {
        Marker marker = map.addMarker(new MarkerOptions()
                .position(point)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
        );

        marker.setTag("TEMP_POLYGON_POINT");
        tempPolygonMarkers.add(marker);
    }
    public void clearTemporaryPolygon() {
        if (tempPolygon != null) {
            tempPolygon.remove();
            tempPolygon = null;
        }
        for(Marker marker : tempPolygonMarkers){ marker.remove(); }
    }
    public void undoLastPoint() {
        if (!tempPoints.isEmpty()) {
            tempPoints.remove(tempPoints.size() - 1);
            displayTemporaryPolygon(tempPoints);
        }
    }
    public List<LatLng> finishPolygon() {
        drawMode = false;

        List<LatLng> result = new ArrayList<>(tempPoints);
        tempPoints.clear();

        showZoomControls(false);
        return result;
    }
    // --- Map Interaction ---
    private Marker focusedMarker;
    private boolean onMarkerClicked(Marker marker)
    {
        Object tag = marker.getTag();
        if ("TEMP_LOCATION".equals(tag)) {
            return true;
        }
        if ("TEMP_POLYGON_POINT".equals(tag)) {
            return true;
        }

        focusedMarker = marker;
        if (listener != null) {
            LocationItem loc = (LocationItem) marker.getTag();
            if (loc != null) {
                listener.onLocationSelected(loc);
            }
        }
        return false;
    }
    private void onPolygonClicked(Polygon polygon) {
        LocationItem loc = (LocationItem) polygon.getTag();

        if (loc != null && listener != null) {
            listener.onLocationSelected(loc);
        }
    }
    private boolean onMapClicked(LatLng latLng)
    {
        focusedMarker = null;

        if(drawMode){
            tempPoints.add(latLng);
            displayTemporaryPolygon(tempPoints);
            return false;
        }
        if(listener != null) {
            listener.onMapClicked();
        }


        return false;
    }
    public void setMarkerClickListener(MarkerClickListener listener) {
        this.listener = listener;
    }

    // --- Temporary Location ---
    private Marker tempMarker;
    public void displayTemporaryLocation(LatLng latLng) {
        Marker marker = map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Temporary Location"));

        marker.setTag("TEMP_LOCATION");
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17));
    }
    public void removeTemporaryLocation(){
        map.clear();
        moveToUserLocation();
    }

    // --- User Location ---
    public void enableUserLocation() {locationPermissionGranted = true;}
    public void moveToUserLocation() {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
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
