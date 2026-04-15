package com.csws.mymaps.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.csws.mymaps.R;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

public class PlaceConfirmBottomSheet {
    public interface Listener {
        void onConfirmed(String name, String type);
    }

    private final Context context;
    private BottomSheetDialog dialog;

    private String placeName;

    public PlaceConfirmBottomSheet(Context context) {
        this.context = context;
    }

    public void show(String suggestedName, Listener listener) {

        dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.bottom_sheet_place_confirm, null);

        TextInputEditText editName = view.findViewById(R.id.editName);
        MaterialAutoCompleteTextView typeSelector = view.findViewById(R.id.typeSelector);
        MaterialButton confirmButton = view.findViewById(R.id.confirmButton);
        TextView title = view.findViewById(R.id.placeTitle);

        title.setText(suggestedName);
        editName.setText(suggestedName);

        // dropdown values
        String[] types = {"building", "poi"};
        typeSelector.setSimpleItems(types);
        typeSelector.setText("building", false);

        confirmButton.setOnClickListener(v -> {

            String finalName = editName.getText() != null
                    ? editName.getText().toString()
                    : suggestedName;

            String type = typeSelector.getText().toString();

            listener.onConfirmed(finalName, type);

            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.show();
    }

}
