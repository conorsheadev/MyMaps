package com.csws.mymaps.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.util.Log;

import androidx.core.content.ContextCompat;

import com.csws.mymaps.R;
import com.csws.mymaps.data.LocationItem;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps3d.GoogleMap3D;
import com.google.android.gms.maps3d.OnMap3DViewReadyCallback;
import com.google.android.gms.maps3d.model.Camera;
import com.google.android.gms.maps3d.model.LatLngAltitude;
import com.google.android.gms.maps3d.model.Map3DMode;

import java.util.List;

public class MapController implements OnMapReadyCallback {
    private final Context context;
    private boolean locationPermissionGranted = false;

    private GoogleMap map;
    private final FusedLocationProviderClient fusedLocationClient;

    public MapController(Context context) {
        this.context = context;
        this.fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.map = googleMap;
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

    }

    public void displayLocations(List<LocationItem> locations) {
        if (map == null) return;

        map.clear();

        for (LocationItem loc : locations) {
            LatLng position = new LatLng(loc.lat, loc.lng);

            map.addMarker(new MarkerOptions()
                    .position(position)
                    .title(loc.name));
        }
    }

    // --- Temporary Location ---
    private Marker tempMarker;
    public void displayTemporaryLocation(LatLng latLng) {
        map.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Temporary Location"));

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
