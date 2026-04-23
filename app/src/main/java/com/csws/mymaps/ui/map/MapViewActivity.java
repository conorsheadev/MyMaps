package com.csws.mymaps.ui.map;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.csws.mymaps.R;
import com.csws.mymaps.model.locations.LocationItem;
import com.csws.mymaps.model.tasks.TaskItem;
import com.csws.mymaps.ui.core.actions.ActionFlowController;
import com.csws.mymaps.ui.core.actions.flows.CreateLocationFlow;
import com.csws.mymaps.ui.places.PlaceSearchActivity;
import com.csws.mymaps.viewmodel.LocationViewModel;
import com.csws.mymaps.viewmodel.TaskViewModel;
import com.csws.mymaps.viewmodel.flows.CreateLocationViewModel;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MapViewActivity extends AppCompatActivity implements ActivityActions, MapFabController.DefaultActionsListener, MapFabController.LocationActionsListener, MapController.MapCallbacks {

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private MapController mapController;
    MapFabController fabController;
    private ActionFlowController flowController;

    private LocationViewModel locationViewModel;
    private TaskViewModel taskViewModel;

    MapView mapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);
        flowController = new ActionFlowController();

        //Setup ViewModels
        setupViewModels();
        //Setup Components
        setupToolbar();
        setupMap();
        setupFab();
        setupBottomSheet();
        //Setup LiveData
        observeData();

        //Activity Permissions
        checkLocationPermissions();
    }

    // --- SETUP ---
    private void setupViewModels(){
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
    }
    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        toolbar.setNavigationOnClickListener(v -> {
            finish(); // back to Planner
        });

        toolbar.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.action_add_location) {
                onAddLocation();
                return true;
            }
            return false;
        });
    }
    private void setupMap() {
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map_container, mapFragment)
                .commit();

        MapController_InfoWindowAdapter adapter = new MapController_InfoWindowAdapter(this);
        mapController = new MapController(this, this, adapter);
        mapFragment.getMapAsync(mapController);
    }
    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.mapFab);
        FrameLayout fabContainer = findViewById(R.id.fabContainer);

        fabController = new MapFabController(this, fab, fabContainer);
        fabController.setListeners(this, this);
        fabController.showDefault();
    }
    private void setupBottomSheet() {
        //TODO: ReImplement BottomSheet Setup
    }
    private void observeData(){
        locationViewModel.getLocations().observe(this, this.mapController::displayLocations);
        taskViewModel.getTasks().observe(this, mapController::setTasks);
    }

    // --- Activity Actions ---
    @Override
    public void openPlaceSearch() {
        Intent intent = new Intent(this, PlaceSearchActivity.class);
        startActivity(intent);
    }
    @Override
    public void createNewLocation(LocationItem locationItem) {
        locationViewModel.addLocation(locationItem);
    }
    @Override
    public void createNewTask(TaskItem taskItem) {
        taskViewModel.addTask(taskItem);
    }
    @Override
    public void cancelCurrentFlow() {}

    // --- FAB Controller Callbacks ---
    @Override
    public void onAddLocation() {
        //TODO: Reimplement AddLocation
        CreateLocationViewModel viewModel = new ViewModelProvider(this).get(CreateLocationViewModel.class);

        CreateLocationFlow flow = new CreateLocationFlow(viewModel,this, mapController);
        flowController.startFlow(flow);
    }
    @Override
    public void onAddTask() {
        if (lastLocationClicked == null) {
            //TODO: ReImplement AddTask
        } else {
            onAddTaskToLocation(lastLocationClicked);
        }
    }
    @Override
    public void onAddTaskToLocation(LocationItem location) {
        //TODO: ReImplement AddTaskToLocation
    }

    // --- Map Callbacks ---
    LocationItem lastLocationClicked = null;
    @Override
    public void onLocationSelected(LocationItem location) {
        List<TaskItem> tasks = taskViewModel.getTasksForLocation(location.id);
        fabController.showLocationActions(location);

        if(lastLocationClicked != null && lastLocationClicked.equals(location)){
            //TODO: ReImplement DisplayLocationDetails
        }

        else{lastLocationClicked = location;}
    }

    @Override
    public void onMapClicked(LatLng latLng) {
        Log.d("MapClicked", "onMapClicked() called");
        lastLocationClicked = null;
        fabController.showDefault();
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

    // --- Lifecycle ---
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
