package com.csws.mymaps.ui.mapviewer.deprecated.bottomsheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.csws.mymaps.R;
import com.csws.mymaps.model.tasks.TaskItem;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Consumer;

public class TaskConfigFragment extends Fragment {

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
        return inflater.inflate(R.layout.bottom_sheet_task_create, container, false);
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
        MaterialAutoCompleteTextView travelModeSelector = view.findViewById(R.id.travelModeSelector);

        LinearLayout timeSection = view.findViewById(R.id.timeSection);
        MaterialButton startTimeButton = view.findViewById(R.id.startTimeButton);
        MaterialButton endTimeButton = view.findViewById(R.id.endTimeButton);
        MaterialButton confirmButton = view.findViewById(R.id.confirmButton);

        // --- Dropdowns ---
        String[] types = {"BASIC", "SCHEDULED", "LOCATION_BASED", "UNIVERSITY"};
        typeSelector.setSimpleItems(types);
        typeSelector.setText("SCHEDULED", false);

        String[] travelModes = {"WALKING", "DRIVING"};
        travelModeSelector.setSimpleItems(travelModes);
        travelModeSelector.setText("WALKING", false);

        // --- Time State ---
        final long[] startTimeMillis = {0};
        final long[] endTimeMillis = {0};

        // --- Type change ---
        typeSelector.setOnItemClickListener((parent, v, pos, id) -> {
            String type = typeSelector.getText().toString();

            if (type.equals("SCHEDULED") || type.equals("UNIVERSITY")) {
                timeSection.setVisibility(View.VISIBLE);
            } else {
                timeSection.setVisibility(View.GONE);
            }
        });

        // --- Time pickers ---
        startTimeButton.setOnClickListener(v ->
                pickDateTime(result -> {
                    startTimeMillis[0] = result;
                    startTimeButton.setText("Start: " + formatDateTime(result));
                })
        );

        endTimeButton.setOnClickListener(v ->
                pickDateTime(result -> {
                    endTimeMillis[0] = result;
                    endTimeButton.setText("End: " + formatDateTime(result));
                })
        );

        // --- Confirm ---
        confirmButton.setOnClickListener(v -> {

            String name = getText(editName);
            String desc = getText(editDesc);
            String typeStr = typeSelector.getText().toString();
            String travelMode = travelModeSelector.getText().toString();

            TaskItem.TaskType type = TaskItem.TaskType.valueOf(typeStr);

            TaskItem task = new TaskItem(
                    UUID.randomUUID().toString(),
                    name,
                    desc,
                    locationId,
                    type
            );

            if (timeSection.getVisibility() == View.VISIBLE) {
                task.startTimeMillis = startTimeMillis[0];
                task.endTimeMillis = endTimeMillis[0];
            }

            task.travelMode = travelMode;

            // --- Prerequisites ---
            String prereqText = getText(editPrereq);
            if (!prereqText.isEmpty()) {
                String[] items = prereqText.split(",");
                for (String item : items) {
                    task.prerequisites.add(item.trim());
                }
            }

            if (listener != null) {
                listener.onTaskConfirmed(task);
            }
        });
    }

    // --- Helpers ---
    private String getText(TextInputEditText editText) {
        return editText.getText() != null
                ? editText.getText().toString()
                : "";
    }

    private String formatDateTime(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, HH:mm", Locale.getDefault());
        return sdf.format(new Date(millis));
    }

    private void pickDateTime(Consumer<Long> onResult) {

        MaterialDatePicker<Long> datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .build();

        datePicker.addOnPositiveButtonClickListener(date -> {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date);

            MaterialTimePicker timePicker =
                    new MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_24H)
                            .setHour(calendar.get(Calendar.HOUR_OF_DAY))
                            .setMinute(calendar.get(Calendar.MINUTE))
                            .setTitleText("Select time")
                            .build();

            timePicker.addOnPositiveButtonClickListener(v -> {

                calendar.set(Calendar.HOUR_OF_DAY, timePicker.getHour());
                calendar.set(Calendar.MINUTE, timePicker.getMinute());
                calendar.set(Calendar.SECOND, 0);

                onResult.accept(calendar.getTimeInMillis());
            });

            timePicker.show(getParentFragmentManager(), "TIME_PICKER");
        });

        datePicker.show(getParentFragmentManager(), "DATE_PICKER");
    }
}