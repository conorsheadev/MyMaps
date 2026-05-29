package com.csws.mymaps.features.map.ui.bottom_sheets;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import java.util.function.Consumer;

public class PlannedTaskConfigFragment extends Fragment {
    //TODO: Clean Up
    public interface Listener {
        void onPlannedTaskConfirmed(PlannedTask plannedTask);
    }
    private Listener listener;
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private static final String ARG_TASK_ID = "task_id";

    public static PlannedTaskConfigFragment newInstance(String taskId) {

        PlannedTaskConfigFragment fragment = new PlannedTaskConfigFragment();

        Bundle args = new Bundle();
        args.putString(ARG_TASK_ID, taskId);
        fragment.setArguments(args);

        return fragment;
    }

    private String taskId;

    private long startTimeMillis = 0;
    private long endTimeMillis = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_plannedtask_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskId = getArguments() != null
                ? getArguments().getString(ARG_TASK_ID)
                : null;

        // --- Views ---
        MaterialButton startTimeButton = view.findViewById(R.id.startTimeButton);
        MaterialButton endTimeButton = view.findViewById(R.id.endTimeButton);
        MaterialButton confirmButton = view.findViewById(R.id.confirmButton);
        MaterialAutoCompleteTextView travelModeSelector = view.findViewById(R.id.travelModeSelector);

        // --- Travel Modes ---
        String[] travelModes = {
                "WALKING",
                "DRIVING"
        };
        travelModeSelector.setSimpleItems(travelModes);
        travelModeSelector.setText("WALKING", false);

        // --- Start Time ---
        startTimeButton.setOnClickListener(v ->
                pickDateTime(result -> {

                    startTimeMillis = result;
                    startTimeButton.setText("Start: " + formatDateTime(result));

                })
        );

        // --- End Time ---
        endTimeButton.setOnClickListener(v ->
                pickDateTime(result -> {

                    endTimeMillis = result;
                    endTimeButton.setText("End: " + formatDateTime(result));

                })
        );

        // --- Confirm ---
        confirmButton.setOnClickListener(v -> {

            PlannedTask plannedTask = new PlannedTask(UUID.randomUUID().toString(), taskId);

            plannedTask.startTimeMillis = startTimeMillis;

            plannedTask.endTimeMillis = endTimeMillis;

            plannedTask.travelMode = travelModeSelector.getText().toString();

            if (listener != null) {
                listener.onPlannedTaskConfirmed(plannedTask);
            }
        });
    }

    // ----------------------------------------------------
    // HELPERS
    // ----------------------------------------------------

    private String formatDateTime(long millis) {
        //TODO: Setup DateTime Utils for Formatting
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, HH:mm", Locale.getDefault());
        return sdf.format(new Date(millis));
    }

    private void pickDateTime(Consumer<Long> onResult) {

        //TODO: Clean Up (Refactor logic into custom MaterialDatePicker Designed for our purpose?)
        MaterialDatePicker<Long> datePicker =
                MaterialDatePicker.Builder
                        .datePicker()
                        .setTitleText("Select date")
                        .build();

        datePicker.addOnPositiveButtonClickListener(date -> {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date);
            MaterialTimePicker timePicker =
                    new MaterialTimePicker.Builder()
                            .setTimeFormat(TimeFormat.CLOCK_24H)
                            .setHour(
                                    calendar.get(Calendar.HOUR_OF_DAY)
                            )
                            .setMinute(
                                    calendar.get(Calendar.MINUTE)
                            )
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
