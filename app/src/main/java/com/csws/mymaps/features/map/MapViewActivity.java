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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.core.flow.ActionFlowController;
import com.csws.mymaps.core.flow.ActionFlowFactory;
import com.csws.mymaps.core.flow.interfaces.ActivityActions;
import com.csws.mymaps.features.map.controllers.BottomSheetController;
import com.csws.mymaps.features.map.controllers.MapFabController;
import com.csws.mymaps.features.map.flows.CreateTaskFlow;
import com.csws.mymaps.features.map.ui.placesearch.PlaceSearchFragment;
import com.csws.mymaps.features.map.map.MapController_InfoWindowAdapter;
import com.csws.mymaps.features.map.map.MapFragment;
import com.csws.mymaps.core.viewmodel.LocationViewModel;
import com.csws.mymaps.core.viewmodel.TaskViewModel;
import com.csws.mymaps.features.map.viewmodels.CreateLocationViewModel;
import com.csws.mymaps.features.map.viewmodels.CreateTaskViewModel;
import com.csws.mymaps.features.map.viewmodels.DefaultFlowViewModel;
import com.csws.mymaps.core.viewmodel.PlannedTaskViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapViewActivity extends AppCompatActivity implements ActivityActions, MapFabController.FabActionListener, MapFragment.MapCallbacks, BottomSheetController.Listener {

    private static final int LOCATION_PERMISSION_REQUEST = 1;
    private MapFragment mapFragment;
    private MapFabController fabController;
    private BottomSheetController bottomSheetController;

    private ActionFlowController flowController;
    private ActionFlowFactory flowFactory;

    private LocationViewModel locationViewModel;
    private TaskViewModel taskViewModel;
    private PlannedTaskViewModel plannedTaskViewModel;

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

        //Flow Controller
        flowController = new ActionFlowController();
        flowFactory = new ActionFlowFactory(taskViewModel, plannedTaskViewModel, locationViewModel, this, mapFragment);
        cancelCurrentFlow();

        //Setup LiveData
        observeData();

        //Activity Permissions
        checkLocationPermissions();
    }

    // --- SETUP ---
    private void setupViewModels(){
        locationViewModel = new ViewModelProvider(this).get(LocationViewModel.class);
        taskViewModel = new ViewModelProvider(this).get(TaskViewModel.class);
        plannedTaskViewModel = new ViewModelProvider(this).get(PlannedTaskViewModel.class);
    }
    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        toolbar.setNavigationOnClickListener(v -> {
            finish(); // back to Planner
        });
    }
    private void setupMap() {
        mapFragment = new MapFragment();

        MapController_InfoWindowAdapter adapter = new MapController_InfoWindowAdapter(this);

        mapFragment.setListener(this);
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
        fabController.setListener(this);
        fabController.showDefault();
    }
    private void setupBottomSheet() {
        View sheet = findViewById(R.id.locationSheet);
        bottomSheetController = new BottomSheetController(sheet, R.id.bottom_sheet_container);
        bottomSheetController.setListener(this);
    }
    private void observeData(){
        //TODO: Clean Up
        locationViewModel.getLocations().observe(this, locationItems -> {
            if(mapFragment!=null) {
                this.mapFragment.displayLocations(locationItems);
            }
        });
        Observer<Object> refreshTasks = o -> {

            if (mapFragment != null) {

                mapFragment.setTasks(
                        taskViewModel.getTasks().getValue(),
                        plannedTaskViewModel.getPlannedTasks().getValue()
                );
            }
        };

        taskViewModel.getTasks().observe(this, refreshTasks);

        plannedTaskViewModel.getPlannedTasks().observe(this, refreshTasks);
    }

    // --- Activity NEW FLOW Actions ---
    @Override
    public void startCreateLocationFlow() {
        CreateLocationViewModel vm = new ViewModelProvider(this).get(CreateLocationViewModel.class);
        flowController.startFlow(flowFactory.createLocationFlow(vm));
    }
    @Override
    public void startCreateTaskFlow(){
        CreateTaskViewModel vm = new ViewModelProvider(this).get(CreateTaskViewModel.class);
        flowController.startFlow(flowFactory.createTaskFlow(vm));
    }
    @Override
    public void startCreateTaskFromLocationFlow(LocationItem location){
        CreateTaskFlow flow = flowFactory.createTaskFlow(new ViewModelProvider(this).get(CreateTaskViewModel.class));
        flowController.startFlow(flow);
        flow.onLocationSelected(location);
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
    @Override
    public void cancelCurrentFlow() {
        flowController.startFlow(flowFactory.createDefaultFlow(new ViewModelProvider(this).get(DefaultFlowViewModel.class)));
    }
    // --- Activity REAL Actions ---
    @Override
    public void createNewLocation(LocationItem locationItem) {
        locationViewModel.addLocation(locationItem);
    }
    @Override
    public void createNewTask(TaskItem taskItem) {
        taskViewModel.addTask(taskItem);
    }
    @Override
    public void createNewPlannedTask(PlannedTask plannedTask) {
        plannedTaskViewModel.addPlannedTask(plannedTask);
    }


    // --- MAP/FAB/BOTTOM_SHEET Controller Callbacks ---
    @Override //FAB
    public void onFabAction(int actionId) {
        if (flowController.getCurrentFlow() != null) {
            flowController.getCurrentFlow().onAction(actionId);
        }
    }

    @Override //MAP
    public void onMapClicked(LatLng latLng) {
        //if (isBottomSheetOpen()) return;
        flowController.getCurrentFlow().onMapClicked(latLng);
    }

    @Override //MAP
    public void onLocationSelected(LocationItem location) {
        flowController.getCurrentFlow().onLocationSelected(location);
    }

    @Override //BOTTOM_SHEET
    public void onSheetShown() {
        mapFragment.setMapClicksEnabled(false);
    }

    @Override //BOTTOM_SHEET
    public void onSheetHidden() {
        mapFragment.setMapClicksEnabled(true);
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
