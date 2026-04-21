package com.csws.mymaps.ui.map.googleplaces;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.csws.mymaps.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;

public class PlacesDialogFragment extends DialogFragment {

    public interface PlacesResultListener {
        void onPlaceSelected(Place place, LatLng latLng);
        void onPlacesError(String message);
    }

    private PlacesResultListener listener;
    private AutocompleteSupportFragment autocompleteFragment;

    public static PlacesDialogFragment newInstance() {
        return new PlacesDialogFragment();
    }

    public void setListener(PlacesResultListener listener) {
        this.listener = listener;
    }
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, com.google.android.material.R.style.ThemeOverlay_Material3_MaterialAlertDialog);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_places, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        autocompleteFragment = new AutocompleteSupportFragment();

        getChildFragmentManager()
                .beginTransaction()
                .replace(R.id.autocomplete_container, autocompleteFragment)
                .commitNow();

        setupAutocompleteFragment();
    }

    @Override
    public void onStart() {
        super.onStart();

        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setLayout(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
        }
    }

    private void setupAutocompleteFragment() {
        autocompleteFragment.setPlaceFields(Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
        ));

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                if (listener != null) {
                    listener.onPlaceSelected(place, place.getLatLng());
                }
                dismiss();
            }

            @Override
            public void onError(Status status) {
                if (listener != null) {
                    listener.onPlacesError(status.getStatusMessage());
                }
            }
        });

    }


}