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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.csws.mymaps.R;
import com.csws.mymaps.features.map.controllers.MapToolbarController;
import com.csws.mymaps.core.flow.interfaces.ActivityActions;
import com.csws.mymaps.features.map.controllers.BottomSheetController;
import com.csws.mymaps.features.map.controllers.MapFabController;
import com.csws.mymaps.features.map.coordinators.MapViewCoordinator;
import com.csws.mymaps.features.map.controllers.ui.placesearch.PlaceSearchFragment;
import com.csws.mymaps.features.map.controllers.map.MapController_InfoWindowAdapter;
import com.csws.mymaps.features.map.controllers.map.MapFragment;
import com.csws.mymaps.core.viewmodel.LocationViewModel;
import com.csws.mymaps.core.viewmodel.TaskViewModel;
import com.csws.mymaps.core.viewmodel.PlannedTaskViewModel;
import com.csws.mymaps.features.map.viewmodels.SessionViewModel;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapViewActivity extends AppCompatActivity implements ActivityActions {

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private MapFragment mapFragment;
    private MapFabController fabController;
    private BottomSheetController bottomSheetController;
    private MapToolbarController toolbarController;

    private LocationViewModel locationViewModel;
    private TaskViewModel taskViewModel;
    private PlannedTaskViewModel plannedTaskViewModel;
    private SessionViewModel sessionViewModel;

    private MapViewCoordinator coordinator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapview);

        //Setup ViewModels
        setupViewModels();
        //Setup Components
        setupToolbar();
        setupMap();
        setupFab();
        setupBottomSheet();
        //Setup Coordinator
        coordinator = new MapViewCoordinator(
                this,
                this,
                mapFragment,
                locationViewModel,
                taskViewModel,
                plannedTaskViewModel,
                sessionViewModel
        );

        bindCoordinator();

        coordinator.start();
        coordinator.observe(this);



        //Activity Permissions
        checkLocationPermissions();
    }

    // --- SETUP ---
    private void setupViewModels(){
        ViewModelProvider vmProvider = new ViewModelProvider(this);

        locationViewModel = vmProvider.get(LocationViewModel.class);
        taskViewModel = vmProvider.get(TaskViewModel.class);
        plannedTaskViewModel = vmProvider.get(PlannedTaskViewModel.class);
        sessionViewModel = vmProvider.get(SessionViewModel.class);
    }
    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbarController = new MapToolbarController(toolbar);
    }
    private void setupMap() {
        mapFragment = new MapFragment();

        MapController_InfoWindowAdapter adapter = new MapController_InfoWindowAdapter(this);
        mapFragment.setInfoWindowAdapter(adapter);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.map_container, mapFragment)
                .commit();
    }
    private void setupFab() {
        FloatingActionButton fab = findViewById(R.id.mapFab);
        FrameLayout fabContainer = findViewById(R.id.fabContainer);

        fabController = new MapFabController(this, fab, fabContainer);
        fabController.showDefault();
    }
    private void setupBottomSheet() {
        View sheet = findViewById(R.id.locationSheet);
        bottomSheetController = new BottomSheetController(sheet, R.id.bottom_sheet_container);
    }
    private void bindCoordinator() {
        toolbarController.setListener(coordinator);
        mapFragment.setListener(coordinator);
        fabController.setListener(coordinator);
        bottomSheetController.setListener(coordinator);
    }

    // --- Activity UI Actions ---
    @Override
    public void openPlaceSearch(PlaceSearchFragment.PlaceSelectionListener listener) {
        PlaceSearchFragment fragment = new PlaceSearchFragment();

        fragment.setListener(listener);

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.map_container, fragment)
                .addToBackStack(null)
                .commit();
    }
    @Override
    public void closePlaceSearch() {

    }
    @Override
    public void setFabMenu(int menuRes) {
        fabController.setMenu(menuRes);
    }
    @Override
    public void showBottomSheet(Fragment fragment) {
        bottomSheetController.show(getSupportFragmentManager(), fragment);
    }
    @Override
    public void hideBottomSheet() {
        bottomSheetController.hide();
    }



    // --- Permissions ---
    private void checkLocationPermissions() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCL0FPqe4IgRy-QQO42y1P5xCg09LwHLuc");
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            //If permissions already granted
            mapFragment.enableUserLocation();
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
