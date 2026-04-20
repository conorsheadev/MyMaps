package com.csws.mymaps.map;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.csws.mymaps.R;
import com.csws.mymaps.data.locations.LocationItem;
import com.csws.mymaps.data.locations.LocationRepository;
import com.csws.mymaps.data.locations.PolygonConfig;
import com.csws.mymaps.data.tasks.TaskItem;
import com.csws.mymaps.data.tasks.TaskRepository;
import com.csws.mymaps.map.bottomsheets.LocationCreatorBottomSheet;
import com.csws.mymaps.map.bottomsheets.TaskCreatorBottomSheet;
import com.csws.mymaps.map.googleplaces.PlacesController;
import com.csws.mymaps.map.mapcontroller.MapController;
import com.csws.mymaps.map.tasks_and_locations.LocationManager;
import com.csws.mymaps.map.tasks_and_locations.TaskManager;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.UUID;

public class MapViewActivity extends AppCompatActivity implements UIController.ToolbarListener, UIController.LocationActionsListener, UIController.PolygonActionsListener, PlacesController.PlacesResultListener, MapController.MarkerClickListener {

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private MapController mapController;
    private UIController uiController;
    private LocationDetailSheetController sheetController;
    private LocationManager locationManager;
    private TaskManager taskManager;
    private PlacesController placesController;
    private DayPlannerController dayPlannerController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);

        //UI Components
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        FloatingActionButton fab = findViewById(R.id.mapFab);
        FrameLayout fabContainer = findViewById(R.id.fabContainer);
        View sheetView = findViewById(R.id.locationSheet);
        //RelativeLayout plannerContainer = findViewById(R.id.plannerTimelineContainer);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        //Controllers
        uiController = new UIController(this, drawerLayout, toolbar, fab, fabContainer);
        uiController.init();
        sheetController = new LocationDetailSheetController(this,sheetView);
        LocationRepository repo = new LocationRepository(this);
        locationManager = new LocationManager(repo);
        TaskRepository taskRepo = new TaskRepository(this);
        taskManager = new TaskManager(taskRepo);
        //dayPlannerController = new DayPlannerController(this, plannerContainer);
        //dayPlannerController.showDay(taskManager.getAllTasks());
        placesController = new PlacesController(this);


        mapController = new MapController(this, this,taskManager);
        mapFragment.getMapAsync(mapController);
        //Activity Permissions
        checkLocationPermissions();
        Log.d("MapViewActivity", "onCreate() called");

        // Load and display
        mapController.displayLocations(locationManager.getLocations());
    }

    // --- Map Interaction Listeners ---
    LocationItem lastLocationClicked = null;
    @Override
    public void onLocationSelected(LocationItem location) {
        uiController.showLocationActions(location);

        if(lastLocationClicked != null && lastLocationClicked.equals(location)){
            List<TaskItem> tasks = taskManager.getTasksForLocation(location.id);
            sheetController.show(location, tasks);
        }
        else{lastLocationClicked = location;}
    }
    @Override
    public void onMapClicked() {
        sheetController.hide();
        lastLocationClicked = null;
        uiController.showDefaultFab();
    }

    // --- Location Action Listeners ---
    @Override
    public void onAddTaskToLocation(LocationItem location) {
        this.runOnUiThread(() -> {
            if (!this.isFinishing() && !this.isDestroyed()) {
                //TaskCreatorBottomSheet sheet = new TaskCreatorBottomSheet(this);
                //sheet.show(location.id, task -> {Log.d("MapViewActivity", "call backrecieved");});
            }
        });
    }

    // --- UI Listeners ---
    @Override
    public void onAddLocationClicked() {
        this.runOnUiThread(()-> {
            placesController.launchPlacePicker();
        });
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

            locationManager.addLocation(item);

            resetDrawingState();

            mapController.displayLocations(locationManager.getLocations());
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
}
