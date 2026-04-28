package com.csws.mymaps.ui.map;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.csws.mymaps.R;
import com.csws.mymaps.model.locations.MarkerConfig;
import com.csws.mymaps.model.locations.PolygonConfig;
import com.csws.mymaps.utils.Utilities;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

public class LocationConfigFragment extends Fragment {

    private static final String ARG_NAME = "name";
    private static final String ARG_TYPE = "type";
    private static final String ARG_MARKER = "marker";
    private static final String ARG_POLYGON = "polygon";

    //Base
    private String name = "";
    private String type = "Building";

    //Marker Config
    private MarkerConfig markerConfig;
    private float markerColor = 0f;
    private String markerIcon = "default";

    //Polygon Config
    private PolygonConfig polygonConfig;
    private float polygonColor = 0f;

    public interface Listener {
        void onConfirmed(String name, String type, MarkerConfig markerConfig, PolygonConfig polygonConfig);
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public static LocationConfigFragment newInstance(String name, String type, MarkerConfig markerConfig, PolygonConfig polygonConfig){
        LocationConfigFragment fragment = new LocationConfigFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NAME, name);
        args.putString(ARG_TYPE, type);
        args.putParcelable(ARG_MARKER, markerConfig);
        args.putParcelable(ARG_POLYGON, polygonConfig);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_place_confirm, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        Bundle args = getArguments();
        if (args != null) {
            name = args.getString(ARG_NAME);
            markerConfig = args.getParcelable(ARG_MARKER);
            polygonConfig = args.getParcelable(ARG_POLYGON);

            if (markerConfig != null) {
                markerColor = markerConfig.colorHue;
                markerIcon = markerConfig.iconType;
            }

            if (polygonConfig != null) {
                polygonColor = polygonConfig.colorHue;
            }
        }

        // --- Views ---
        TextInputEditText editName = view.findViewById(R.id.editName);
        MaterialAutoCompleteTextView typeSelector = view.findViewById(R.id.typeSelector);
        MaterialAutoCompleteTextView iconSelector = view.findViewById(R.id.iconSelector);
        ImageView iconPreview = view.findViewById(R.id.iconPreview);
        Slider colorSlider = view.findViewById(R.id.colorSlider);
        MaterialButton confirmButton = view.findViewById(R.id.confirmButton);

        // --- Set initial values ---
        editName.setText(name);

        String[] types = {"building", "poi"};
        typeSelector.setSimpleItems(types);
        typeSelector.setText("building", false);

        String[] icons = {"default", "home", "school", "work"};
        iconSelector.setSimpleItems(icons);
        iconSelector.setText(markerIcon, false);

        colorSlider.setValue(markerColor);

        // --- Initial preview ---
        updatePreview(iconPreview, markerIcon, markerColor);

        // --- Icon change ---
        iconSelector.setOnItemClickListener((parent, v, position, id) -> {
            markerIcon = iconSelector.getText().toString();
            updatePreview(iconPreview, markerIcon, markerColor);
        });

        // --- Color change ---
        colorSlider.addOnChangeListener((slider, value, fromUser) -> {
            markerColor = value;
            updatePreview(iconPreview, markerIcon, markerColor);
        });


        // --- Confirm ---
        confirmButton.setOnClickListener(v -> {
            if (listener != null) {
                name = editName.getText().toString();
                type = typeSelector.getText().toString();

                MarkerConfig markerConfig =
                        new MarkerConfig(markerColor, markerIcon);

                PolygonConfig polygonConfig =
                        new PolygonConfig(polygonColor, null);

                listener.onConfirmed(name, type, markerConfig, polygonConfig);
            }
        });
    }

    private void updatePreview(ImageView iconPreview, String icon, float color) {
        iconPreview.setImageResource(
                Utilities.getIconResource64px(icon)
        );

        iconPreview.setColorFilter(
                Color.HSVToColor(new float[]{color, 1f, 1f})
        );
    }
}