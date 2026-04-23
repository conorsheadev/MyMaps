package com.csws.mymaps.ui.map.deprecated.bottomsheets;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.csws.mymaps.R;
import com.csws.mymaps.model.locations.MarkerConfig;
import com.csws.mymaps.utils.Utilities;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.slider.Slider;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

public class LocationCreatorBottomSheet {
    public interface Listener {
        void onConfirmed(String name, String type, MarkerConfig config);
    }

    private final Context context;
    private BottomSheetDialog dialog;

    public LocationCreatorBottomSheet(Context context) {
        this.context = context;
    }

    public void show(String suggestedName, Listener listener) {

        dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.bottom_sheet_place_confirm, null);

        // UI Components
        TextInputEditText editName = view.findViewById(R.id.editName);
        MaterialAutoCompleteTextView typeSelector = view.findViewById(R.id.typeSelector);
        MaterialAutoCompleteTextView iconSelector = view.findViewById(R.id.iconSelector);
        ImageView iconPreview = view.findViewById(R.id.iconPreview);
        Slider colorSlider = view.findViewById(R.id.colorSlider);
        MaterialButton confirmButton = view.findViewById(R.id.confirmButton);
        TextView title = view.findViewById(R.id.placeTitle);

        title.setText(suggestedName);
        editName.setText(suggestedName);

        // dropdown values
        String[] types = {"building", "poi"};
        typeSelector.setSimpleItems(types);
        typeSelector.setText("building", false);
        String[] icons = {"default", "home", "school", "work"};
        iconSelector.setSimpleItems(icons);
        iconSelector.setText("default", false);

        // icon preview
        iconSelector.setOnItemClickListener((parent, v, position, id) -> {
            String selectedIcon = iconSelector.getText().toString();
            iconPreview.setImageResource(Utilities.getIconResource64px(selectedIcon));
        });
        // icon preview color
        colorSlider.addOnChangeListener((slider, value, fromUser) -> {
            iconPreview.setColorFilter(Color.HSVToColor(new float[]{value, 1f, 1f}));
        });

        confirmButton.setOnClickListener(v -> {

            String finalName = editName.getText() != null
                    ? editName.getText().toString()
                    : suggestedName;

            String type = typeSelector.getText().toString();
            String icon = iconSelector.getText().toString();
            float color = colorSlider.getValue();

            MarkerConfig config = new MarkerConfig(
                    color,
                    icon
            );

            listener.onConfirmed(finalName, type, config);

            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.show();
    }

}
