package com.csws.mymaps.map.bottomsheets;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.csws.mymaps.R;
import com.csws.mymaps.data.tasks.TaskItem;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.UUID;

public class TaskCreatorBottomSheet {
    public interface Listener {
        void onTaskCreated(TaskItem task);
    }

    private final Context context;
    private BottomSheetDialog dialog;

    public TaskCreatorBottomSheet(Context context) {
        this.context = context;
    }

    public void show(String locationId, Listener listener) {

        dialog = new BottomSheetDialog(context);
        View view = LayoutInflater.from(context)
                .inflate(R.layout.bottom_sheet_task_create, null);

        // --- Views ---
        TextInputEditText editName = view.findViewById(R.id.editTaskName);
        TextInputEditText editDesc = view.findViewById(R.id.editTaskDescription);
        TextInputEditText editPrereq = view.findViewById(R.id.editPrerequisites);

        MaterialAutoCompleteTextView typeSelector = view.findViewById(R.id.typeSelector);
        MaterialAutoCompleteTextView travelModeSelector = view.findViewById(R.id.travelModeSelector);

        LinearLayout timeSection = view.findViewById(R.id.timeSection);
        MaterialButton startTimeButton = view.findViewById(R.id.startTimeButton);
        MaterialButton endTimeButton = view.findViewById(R.id.endTimeButton);

        MaterialButton confirmButton = view.findViewById(R.id.confirmButton);

        // --- Dropdown Setup ---
        String[] types = {"BASIC", "SCHEDULED", "LOCATION_BASED", "UNIVERSITY"};
        typeSelector.setSimpleItems(types);
        typeSelector.setText("BASIC", false);

        String[] travelModes = {"WALKING", "DRIVING"};
        travelModeSelector.setSimpleItems(travelModes);
        travelModeSelector.setText("WALKING", false);

        // --- Time State ---
        final long[] startTimeMillis = {0};
        final long[] endTimeMillis = {0};

        // --- Type Change Logic ---
        typeSelector.setOnItemClickListener((parent, v, pos, id) -> {
            String type = typeSelector.getText().toString();

            if (type.equals("SCHEDULED") || type.equals("UNIVERSITY")) {
                timeSection.setVisibility(View.VISIBLE);
            } else {
                timeSection.setVisibility(View.GONE);
            }
        });

        // --- Time Pickers (simple version) ---
        startTimeButton.setOnClickListener(v -> {
            long now = System.currentTimeMillis();
            startTimeMillis[0] = now; // TODO: replace with proper picker
            startTimeButton.setText("Start: Selected");
        });

        endTimeButton.setOnClickListener(v -> {
            long now = System.currentTimeMillis();
            endTimeMillis[0] = now;
            endTimeButton.setText("End: Selected");
        });

        // --- Confirm ---
        confirmButton.setOnClickListener(v -> {

            String name = editName.getText() != null
                    ? editName.getText().toString()
                    : "";

            String desc = editDesc.getText() != null
                    ? editDesc.getText().toString()
                    : "";

            String typeStr = typeSelector.getText().toString();
            String travelMode = travelModeSelector.getText().toString();

            // --- Convert enums ---
            TaskItem.TaskType type = TaskItem.TaskType.valueOf(typeStr);

            // --- Build Task ---
            TaskItem task = new TaskItem(
                    UUID.randomUUID().toString(),
                    name,
                    desc,
                    locationId,
                    type
            );

            // --- Optional fields ---
            if (timeSection.getVisibility() == View.VISIBLE) {
                task.startTimeMillis = startTimeMillis[0];
                task.endTimeMillis = endTimeMillis[0];
            }

            task.travelMode = travelMode;

            // --- Prerequisites ---
            String prereqText = editPrereq.getText() != null
                    ? editPrereq.getText().toString()
                    : "";

            if (!prereqText.isEmpty()) {
                String[] items = prereqText.split(",");
                for (String item : items) {
                    task.prerequisites.add(item.trim());
                }
            }

            // --- Callback ---
            if (listener != null) {
                listener.onTaskCreated(task);
            }

            dialog.dismiss();
        });

        dialog.setContentView(view);
        dialog.show();
    }
}
