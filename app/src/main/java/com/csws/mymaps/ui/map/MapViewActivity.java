package com.csws.mymaps.ui.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;

import com.csws.mymaps.R;
import com.csws.mymaps.model.locations.LocationItem;
import com.csws.mymaps.model.locations.PolygonConfig;
import com.csws.mymaps.model.tasks.TaskItem;
import com.csws.mymaps.data.TaskRepository;
import com.csws.mymaps.ui.map.bottomsheets.LocationCreatorBottomSheet;
import com.csws.mymaps.ui.map.bottomsheets.TaskCreatorBottomSheet;
import com.csws.mymaps.ui.map.googleplaces.PlacesDialogFragment;
import com.csws.mymaps.ui.map.mapcontroller.MapController;
import com.csws.mymaps.viewmodel.LocationViewModel;
import com.csws.mymaps.viewmodel.TaskViewModel;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.UUID;

public class MapViewActivity extends AppCompatActivity implements UIController.ToolbarListener, UIController.LocationActionsListener, UIController.PolygonActionsListener, PlacesDialogFragment.PlacesResultListener, MapController.MarkerClickListener {

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private MapController mapController;
    private UIController uiController;
    private LocationDetailSheetController sheetController;
    private LocationViewModel locationViewModel;
    private TaskViewModel taskViewModel;

    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);
        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST, null);

        //UI Components
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        FloatingActionButton fab = findViewById(R.id.mapFab);
        FrameLayout fabContainer = findViewById(R.id.fabContainer);
        //View sheetView = findViewById(R.id.locationSheet);
        //SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map_container, mapFragment)
                .commit();

        //Controllers
        uiController = new UIController(this, drawerLayout, toolbar, fab, fabContainer);
        uiController.init();
        //sheetController = new LocationDetailSheetController(this,sheetView);

        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        mapController = new MapController(this, this, taskViewModel);
        mapFragment.getMapAsync(mapController);

        //ViewModel
        locationViewModel = new LocationViewModel(this.getApplication());
        locationViewModel.getLocations().observe(this, locations -> {
            mapController.displayLocations(locations);
        });

        //Activity Permissions
        checkLocationPermissions();
        Log.d("MapViewActivity", "onCreate() called");
        Log.d("DEBUG", "IS TASK ROOT: " + isTaskRoot());
        Log.d("CTX_CHECK", "this = " + this);
        Log.d("CTX_CHECK", "appContext = " + getApplicationContext());

        // Load and display
        Toast.makeText(MapViewActivity.this, "ACTIVITY CONTEXT", Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "APP CONTEXT", Toast.LENGTH_LONG).show();

    }

    // --- Map Interaction Listeners ---
    LocationItem lastLocationClicked = null;
    @Override
    public void onLocationSelected(LocationItem location) {
        List<TaskItem> tasks = taskViewModel.getTasksForLocation(location.id);
        uiController.showLocationActions(location);

        if(lastLocationClicked != null && lastLocationClicked.equals(location)){
            sheetController.show(location, tasks);
        }

        else{lastLocationClicked = location;}
    }

    @Override
    public void onMapClicked() {
        Log.d("MapClicked", "onMapClicked() called");
        sheetController.hide();
        lastLocationClicked = null;
        uiController.showDefaultFab();
    }

    // --- Location Action Listeners ---
    @Override
    public void onAddTaskToLocation(LocationItem location) {
        TaskCreatorBottomSheet sheet = new TaskCreatorBottomSheet(this);
        sheet.show(location.id, (task) -> {
            taskViewModel.addTask(task);
            mapController.refreshInfoWindows();
        });
    }

    // --- UI Listeners ---
    @Override
    public void onAddLocationClicked() {
        Log.d("DialogTest", "StateSaved: " + getSupportFragmentManager().isStateSaved());
        Log.d("CTX_CHECK", "this (click) = " + this);
        //PlacesDialogFragment dialog = PlacesDialogFragment.newInstance();
        //dialog.setListener(this);
        //dialog.show(getSupportFragmentManager(), "PlacesDialog");
        //sheetController.hide();
        Toast.makeText(MapViewActivity.this, "ACTIVITY CONTEXT", Toast.LENGTH_LONG).show();
        Toast.makeText(getApplicationContext(), "APP CONTEXT", Toast.LENGTH_LONG).show();


    }
    @Override
    public void onAddTaskClicked() {
        if(lastLocationClicked != null){
            onAddTaskToLocation(lastLocationClicked);
        }
    }

    // --- Location Creation ---
    private boolean isDrawingLocation = false;
    private LatLng pendingLatLng;
    private Place pendingPlace;
    @Override
    public void onPlaceSelected(Place place, LatLng latLng) {

        // Store pending data
        pendingPlace = place;
        pendingLatLng = latLng;

        // Show temp marker
        mapController.displayTemporaryLocation(latLng);

        // Enable draw mode
        mapController.enableDrawMode();
        isDrawingLocation = true;

        // Update UI (FAB + controls)
        uiController.showPolygonActions();
    }
    @Override
    public void onPlacesError(String message) {
        Log.e("Places", message);
    }
    @Override
    public void onUndoPolygonPoint() {
        mapController.undoLastPoint();
    }
    @Override
    public void onConfirmPolygon() {

        List<LatLng> points = mapController.finishPolygon();

        if (points == null || points.size() < 3) {
            Toast.makeText(this, "Polygon needs at least 3 points", Toast.LENGTH_SHORT).show();
            return;
        }
        LocationCreatorBottomSheet sheet = new LocationCreatorBottomSheet(this);

        sheet.show(pendingPlace.getName(), (name, type, markerConfig) -> {

            PolygonConfig polygonConfig = new PolygonConfig(
                    210f, // default color (blue)
                    points
            );

            LocationItem item = new LocationItem(
                    UUID.randomUUID().toString(),
                    name,
                    pendingLatLng.latitude,
                    pendingLatLng.longitude,
                    polygonConfig,
                    markerConfig
            );

            locationViewModel.addLocation(item);

            resetDrawingState();
        });
    }
    @Override
    public void onCancelPolygon() {
        resetDrawingState();
    }
    private void resetDrawingState() {

        isDrawingLocation = false;

        pendingLatLng = null;
        pendingPlace = null;

        mapController.clearTemporaryPolygon();
        mapController.removeTemporaryLocation();

        uiController.showDefaultFab();
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

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("LIFECYCLE", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("LIFECYCLE", "onPause");
    }
}
