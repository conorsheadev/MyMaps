package com.csws.mymaps.ui.places;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.model.flows.CreateLocationState;
import com.csws.mymaps.viewmodel.flows.CreateLocationViewModel;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.AutocompleteSessionToken;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.search.SearchBar;
import com.google.android.material.search.SearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlaceSearchActivity extends AppCompatActivity {

    private SearchBar searchBar;
    private SearchView searchView;
    private RecyclerView recyclerView;

    private PlacesClient placesClient;
    private PlacesAdapter adapter;

    private CreateLocationViewModel viewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placesearch);

        setupViewModel();
        setupToolbar();
        setupSearchUI();
        setupRecycler();
        setupPlacesClient();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(CreateLocationViewModel.class);
    }

    private void setupToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void setupSearchUI() {
        searchBar = findViewById(R.id.searchBar);
        searchView = findViewById(R.id.searchView);

        searchBar.setOnClickListener(v -> searchView.show());
        searchView.setupWithSearchBar(searchBar);

        searchView.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                searchPlaces(s.toString());
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void setupRecycler() {
        recyclerView = findViewById(R.id.resultsRecycler);

        adapter = new PlacesAdapter(this::onPlaceSelected);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void setupPlacesClient() {
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCL0FPqe4IgRy-QQO42y1P5xCg09LwHLuc");
        }

        placesClient = Places.createClient(this);
    }

    // --- Internal Functionality ---
    private void searchPlaces(String query) {

        Log.d("Places", "Query: " + query);
        if (query.isEmpty()) {
            Log.d("Places", "Empty query");
            adapter.submitList(new ArrayList<>());
            return;
        }

        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(52.8, -1.3),   // SW (Nottingham-ish)
                new LatLng(53.1, -1.0)    // NE
        );

        AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

        FindAutocompletePredictionsRequest request =
                FindAutocompletePredictionsRequest.builder()
                        .setQuery(query)
                        .setLocationBias(bounds)
                        .setSessionToken(token)
                        .build();

        placesClient.findAutocompletePredictions(request)
                .addOnSuccessListener(response -> {
                    List<AutocompletePrediction> predictions =
                            response.getAutocompletePredictions();

                    Log.d("Places", "Results: " + predictions.size());

                    adapter.submitList(predictions);
                })
                .addOnFailureListener(e -> {
                    Log.e("Places", "Error: ", e);
                })
                .addOnCanceledListener(() -> {
                    Log.e("Places", "CANCELLED");
                });
    }

    private void onPlaceSelected(AutocompletePrediction prediction) {

        List<Place.Field> fields = Arrays.asList(
                Place.Field.NAME,
                Place.Field.LAT_LNG
        );

        FetchPlaceRequest request =
                FetchPlaceRequest.newInstance(prediction.getPlaceId(), fields);

        placesClient.fetchPlace(request)
                .addOnSuccessListener(response -> {

                    Place place = response.getPlace();

                    Intent result = new Intent();
                    result.putExtra("place_name", place.getName());

                    if (place.getLatLng() != null) {
                        result.putExtra("lat", place.getLatLng().latitude);
                        result.putExtra("lng", place.getLatLng().longitude);
                    }

                    setResult(RESULT_OK, result);
                    finish();
                });
    }

}