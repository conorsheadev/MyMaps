package com.csws.mymaps.map;

import android.content.Intent;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.csws.mymaps.data.LocationItem;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class PlacesController {
    public interface PlacesResultListener {
        void onPlaceSelected(Place place, LatLng latLng);
        void onPlacesError(String message);
    }
    private final AppCompatActivity activity;
    private final PlacesResultListener listener;

    private final ActivityResultLauncher<Intent> launcher;

    public PlacesController(AppCompatActivity activity) {
        this.activity = activity;
        this.listener = (PlacesResultListener) activity;


        launcher = activity.registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {

                    if (result.getResultCode() == AppCompatActivity.RESULT_OK && result.getData() != null) {

                        Place place = Autocomplete.getPlaceFromIntent(result.getData());
                        LatLng latLng = place.getLatLng();

                        listener.onPlaceSelected(place, latLng);
                    }

                    else if (result.getResultCode() == AutocompleteActivity.RESULT_ERROR && result.getData() != null) {
                        listener.onPlacesError("Place selection failed");
                    }
                }
        );
    }

    public void launchPlacePicker() {

        List<Place.Field> fields = Arrays.asList(
                Place.Field.ID,
                Place.Field.NAME,
                Place.Field.LAT_LNG
        );

        Intent intent = new Autocomplete.IntentBuilder(
                AutocompleteActivityMode.FULLSCREEN,
                fields
        ).build(activity);

        launcher.launch(intent);
    }
}
