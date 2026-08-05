package com.csws.mymaps.core.ui.forms;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.csws.mymaps.R;
import com.csws.mymaps.core.models.tasks.TaskCollection;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.UUID;

public class TaskCollectionConfigFragment extends Fragment {

    public interface Listener {
        void onCollectionConfirmed(TaskCollection collection);
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {

        return inflater.inflate(
                R.layout.bottom_sheet_taskcollection_create,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextInputEditText editName = view.findViewById(R.id.editCollectionName);
        TextInputEditText editDescription = view.findViewById(R.id.editCollectionDescription);
        MaterialAutoCompleteTextView colorSelector = view.findViewById(R.id.colorSelector);
        MaterialAutoCompleteTextView iconSelector = view.findViewById(R.id.iconSelector);
        MaterialButton confirmButton = view.findViewById(R.id.confirmButton);

        setupColorSelector(colorSelector);
        setupIconSelector(iconSelector);

        confirmButton.setOnClickListener(v -> {

            TaskCollection collection = new TaskCollection();

            collection.id = UUID.randomUUID().toString();
            collection.title = getText(editName);
            collection.description = getText(editDescription);
            collection.color = parseColor(colorSelector.getText().toString());
            collection.iconName = iconSelector.getText().toString();

            if (listener != null) {
                listener.onCollectionConfirmed(
                        collection
                );
            }
        });
    }

    private void setupColorSelector(MaterialAutoCompleteTextView selector) {

        String[] colors = {
                "Blue",
                "Green",
                "Red",
                "Orange",
                "Purple"
        };

        selector.setSimpleItems(colors);
        selector.setText("Blue", false);
    }

    private void setupIconSelector(MaterialAutoCompleteTextView selector) {

        String[] icons = {
                "school",
                "science",
                "book",
                "computer",
                "event",
                "travel"
        };

        selector.setSimpleItems(icons);
        selector.setText("school", false);
    }

    private int parseColor(String value) {

        switch (value) {

            case "Green":
                return Color.GREEN;

            case "Red":
                return Color.RED;

            case "Orange":
                return Color.rgb(
                        255,
                        165,
                        0
                );

            case "Purple":
                return Color.MAGENTA;

            case "Blue":
            default:
                return Color.BLUE;
        }
    }

    private String getText(TextInputEditText editText) {

        return editText.getText() != null
                ? editText.getText().toString()
                : "";
    }
}
