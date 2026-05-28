package com.csws.mymaps.features.map;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.csws.mymaps.R;
import com.csws.mymaps.features.map.controllers.MapToolbarController;
import com.csws.mymaps.features.map.controllers.BottomSheetController;
import com.csws.mymaps.features.map.controllers.MapFabController;
import com.csws.mymaps.features.map.controllers.ui.TopSheetController;
import com.csws.mymaps.features.map.coordinators.FlowContext;
import com.csws.mymaps.features.map.coordinators.MapViewCoordinator;
import com.csws.mymaps.features.map.controllers.map.MapController_InfoWindowAdapter;
import com.csws.mymaps.features.map.controllers.map.MapFragment;
import com.csws.mymaps.core.viewmodel.LocationViewModel;
import com.csws.mymaps.core.viewmodel.TaskViewModel;
import com.csws.mymaps.core.viewmodel.PlannedTaskViewModel;
import com.csws.mymaps.features.map.viewmodels.SessionViewModel;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapViewActivity extends AppCompatActivity {
    //Debugging
    private static final String TAG = "MapViewActivity";

    private static final int LOCATION_PERMISSION_REQUEST = 1;

    private MapToolbarController toolbarController;
    private TopSheetController topSheetController;
    private MapFragment mapFragment;
    private MapFabController fabController;
    private BottomSheetController bottomSheetController;

    private MapViewCoordinator coordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*Debug*/Log.d(TAG, "onCreate START");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);

        /*Debug*/Log.d(TAG, "setContentView COMPLETE");

        //Setup Components
        setupToolbar();
        setupTopSheet();
        setupMap();
        setupFab();
        setupBottomSheet();

        /*Debug*/Log.d(TAG, "onCreate END");
    }

    public void onMapLoaded() {

        Log.d(TAG, "Map Loaded");

        checkLocationPermissions();

        setupCoordinator();

        coordinator.observe(this);
    }

    public void onMapPrepared() {

        Log.d(TAG, "Map Prepared");

        coordinator.start();
    }

    // --- SETUP ---
    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbarController = new MapToolbarController(toolbar);
    }
    private void setupTopSheet() {

        View sheet = findViewById(R.id.topSheet);

        topSheetController = new TopSheetController(
                sheet,
                R.id.top_sheet_container,
                getSupportFragmentManager()
        );
    }
    private void setupMap() {
        /*Debug*/Log.d(TAG, "setupMap START");

        mapFragment = new MapFragment();

        /*Debug*/Log.d(TAG, "MapFragment instance created");

        MapController_InfoWindowAdapter adapter = new MapController_InfoWindowAdapter(this);
        mapFragment.setInfoWindowAdapter(adapter);

        /*Debug*/Log.d(TAG, "InfoWindowAdapter attached");

        mapFragment.setOnMapLoadedListener(this::onMapLoaded);
        mapFragment.setOnMapPreparedListener(this::onMapPrepared);

        /*Debug*/Log.d(TAG, "OnMap(Loaded/Prepared)Listeners attached");

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map_container, mapFragment)
                .commitNow();

        /*Debug*/Log.d(TAG, "MapFragment transaction committed");
    }
    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.mapFab);
        FrameLayout fabContainer = findViewById(R.id.fabContainer);

        fabController = new MapFabController(this, fab, fabContainer);
        fabController.showDefault();
    }
    private void setupBottomSheet() {
        View sheet = findViewById(R.id.locationSheet);
        bottomSheetController = new BottomSheetController(sheet, R.id.bottom_sheet_container, getSupportFragmentManager());
    }

    private void setupCoordinator(){
        ViewModelProvider vmProvider = new ViewModelProvider(this);

        LocationViewModel locationViewModel = vmProvider.get(LocationViewModel.class);
        TaskViewModel taskViewModel = vmProvider.get(TaskViewModel.class);
        PlannedTaskViewModel plannedTaskViewModel = vmProvider.get(PlannedTaskViewModel.class);
        SessionViewModel sessionViewModel = vmProvider.get(SessionViewModel.class);

        FlowContext flowContext = new FlowContext(

                taskViewModel,
                plannedTaskViewModel,
                locationViewModel,
                sessionViewModel,

                toolbarController,
                topSheetController,
                mapFragment,
                fabController,
                bottomSheetController,

                null,
                null
        );

        // --- Coordinator ---
        coordinator = new MapViewCoordinator(this, flowContext);

        coordinator.flowController.bindCallbacks(toolbarController, mapFragment, fabController, bottomSheetController);

        // inject session actions after creation
        flowContext.bootstrap(coordinator.flowController, coordinator);
    }

    // --- Permissions ---
    private void checkLocationPermissions() {
        Log.d(TAG, "checkLocationPermissions");

        if (!Places.isInitialized()) {

            Log.d(TAG, "Initialising Places SDK");

            Places.initialize(getApplicationContext(), "AIzaSyCL0FPqe4IgRy-QQO42y1P5xCg09LwHLuc");
        }

        boolean granted = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

        Log.d(TAG, "Location permission granted = " + granted);

        if (granted){
            //If permissions already granted

            Log.d(TAG, "Enabling user location");

            mapFragment.enableUserLocation();
        } else {
            //If permissions not granted, request them

            Log.d(TAG, "Requesting permission");

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

                mapFragment.enableUserLocation();
                mapFragment.moveToUserLocation();
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
