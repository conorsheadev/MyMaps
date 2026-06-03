package com.csws.mymaps.features.map.interaction.ui.bottom_sheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.UUID;

public class TaskConfigFragment extends Fragment {
    //TODO: Clean Up
    public interface Listener {
        void onTaskConfirmed(TaskItem task);
    }
    private Listener listener;
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private static final String ARG_LOCATION_ID = "location_id";

    public static TaskConfigFragment newInstance(String locationId) {

        TaskConfigFragment fragment = new TaskConfigFragment();

        Bundle args = new Bundle();
        args.putString(ARG_LOCATION_ID, locationId);

        fragment.setArguments(args);

        return fragment;
    }

    private String locationId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(
                R.layout.bottom_sheet_task_create,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        locationId = getArguments() != null
                ? getArguments().getString(ARG_LOCATION_ID)
                : null;

        // --- Views ---
        TextInputEditText editName = view.findViewById(R.id.editTaskName);
        TextInputEditText editDesc = view.findViewById(R.id.editTaskDescription);
        TextInputEditText editPrereq = view.findViewById(R.id.editPrerequisites);
        MaterialAutoCompleteTextView typeSelector = view.findViewById(R.id.typeSelector);
        MaterialButton confirmButton = view.findViewById(R.id.confirmButton);

        // --- Dropdown ---

        String[] types = {
                "BASIC",
                "LOCATION_BASED"
        };

        typeSelector.setSimpleItems(types);
        typeSelector.setText("BASIC", false);

        // --- Confirm ---

        confirmButton.setOnClickListener(v -> {

            String name = getText(editName);
            String desc = getText(editDesc);
            String typeStr = typeSelector.getText().toString();
            TaskItem.TaskType type = TaskItem.TaskType.valueOf(typeStr);

            TaskItem task = new TaskItem(
                    UUID.randomUUID().toString(),
                    "",
                    name,
                    desc,
                    locationId,
                    type
            );

            String prereqText = getText(editPrereq);
            if (!prereqText.isEmpty()) {

                String[] items = prereqText.split(",");

                for (String item : items) {
                    //TODO: Reimplement Prerequisites?
                    //task.prerequisites.add(item.trim());
                }
            }

            if (listener != null) {
                listener.onTaskConfirmed(task);
            }
        });
    }

    // ----------------------------------------------------
    // HELPERS
    // ----------------------------------------------------

    private String getText(TextInputEditText editText) {

        return editText.getText() != null
                ? editText.getText().toString()
                : "";
    }
}