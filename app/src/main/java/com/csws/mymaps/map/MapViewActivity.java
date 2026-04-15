package com.csws.mymaps.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.csws.mymaps.R;
import com.csws.mymaps.data.LocationItem;
import com.csws.mymaps.data.LocationRepository;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps3d.Map3DView;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class MapViewActivity extends AppCompatActivity implements UIController.ToolbarListener, PlacesController.PlacesResultListener {

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private MapController mapController;
    private UIController uiController;
    private LocationManager locationManager;
    private PlacesController placesController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);

        //UI Components
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        //Controllers
        uiController = new UIController(this, drawerLayout, toolbar);
        uiController.init();
        mapController = new MapController(this);
        mapFragment.getMapAsync(mapController);
        LocationRepository repo = new LocationRepository(this);
        locationManager = new LocationManager(repo);
        placesController = new PlacesController(this);

        // Load and display
        mapController.displayLocations(locationManager.getLocations());

        //Activity Permissions
        checkLocationPermissions();
        Log.d("MapViewActivity", "onCreate() called");
    }


    // --- UI Listeners ---
    @Override
    public void onAddLocationClicked() {
        placesController.launchPlacePicker();
    }
    @Override
    public void onAddTaskClicked() {}

    // --- Places Search ---
    @Override
    public void onPlaceSelected(Place place, LatLng latLng) {
        mapController.displayTemporaryLocation(latLng);

        PlaceConfirmBottomSheet sheet = new PlaceConfirmBottomSheet(this);
        sheet.show(place.getName(), (name, type) -> {
            mapController.removeTemporaryLocation();

            LocationItem item = new LocationItem(
                    UUID.randomUUID().toString(),
                    name,
                    latLng.latitude,
                    latLng.longitude,
                    type
            );

            locationManager.addLocation(item);
            mapController.displayLocations(locationManager.getLocations());
        });


    }
    @Override
    public void onPlacesError(String message) {
        Log.e("Places", message);
    }
    // --- Permissions ---
    private void checkLocationPermissions() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCL0FPqe4IgRy-QQO42y1P5xCg09LwHLuc");
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //If permissions already granted
            mapController.enableUserLocation();
        } else {
            //If permissions not granted, request them
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST
            );
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                mapController.enableUserLocation();
                mapController.moveToUserLocation();
            }
        }
    }
}
