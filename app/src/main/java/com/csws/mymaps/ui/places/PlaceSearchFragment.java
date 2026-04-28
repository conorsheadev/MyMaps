package com.csws.mymaps.ui.places;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
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

public class PlaceSearchFragment extends Fragment {
    public interface PlaceSelectionListener{
        void onPlaceSelected(String name, double lat, double lng);
        void onSearchCancelled();
    }

    private PlaceSelectionListener listener; public void setListener(PlaceSelectionListener listener){this.listener = listener;}

    private SearchBar searchBar;
    private SearchView searchView;
    private RecyclerView recyclerView;

    private PlacesClient placesClient;
    private PlacesAdapter adapter;

    private CreateLocationViewModel viewModel;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_placesearch, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViewModel();
        setupSearchUI(view);
        setupRecycler(view);
        setupPlacesClient();
    }

    private void setupViewModel() {
        viewModel = new ViewModelProvider(this).get(CreateLocationViewModel.class);
    }

    private void setupSearchUI(View view) {
        searchBar = view.findViewById(R.id.searchBar);
        searchView = view.findViewById(R.id.searchView);

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

    private void setupRecycler(View view) {
        recyclerView = view.findViewById(R.id.resultsRecycler);

        adapter = new PlacesAdapter(this::onPlaceSelected);

        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.setAdapter(adapter);
    }

    private void setupPlacesClient() {
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), "AIzaSyCL0FPqe4IgRy-QQO42y1P5xCg09LwHLuc");
        }

        placesClient = Places.createClient(requireContext());
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
                    searchBar.setText(place.getName());
                    searchView.hide();

                    if (listener != null && place.getLatLng() != null) {
                        listener.onPlaceSelected(
                                place.getName(),
                                place.getLatLng().latitude,
                                place.getLatLng().longitude
                        );
                    }

                    closeFragment();
                });
    }

    private void closeFragment() {
        if (isAdded()) {
            requireActivity()
                    .getSupportFragmentManager()
                    .popBackStack();
        }
    }

}