package com.csws.mymaps.features.map.ui.bottom_sheets;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
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

    private Calendar selectedDate = Calendar.getInstance();

    private int startHour = 9;
    private int startMinute = 0;

    private int endHour = 10;
    private int endMinute = 0;

    private boolean useDurationMode = true;

    LinearLayout durationContainer;
    TextInputEditText durationHoursInput;
    TextInputEditText durationMinutesInput;
    TextInputLayout endTimeContainer;
    MaterialAutoCompleteTextView travelModeSelector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_plannedtask_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        taskId = getArguments() != null ? getArguments().getString(ARG_TASK_ID) : null;


        // INIT UI
        MaterialButtonToggleGroup toggleGroup = view.findViewById(R.id.schedulingModeToggle);
        durationContainer = view.findViewById(R.id.durationContainer);
        endTimeContainer = view.findViewById(R.id.endTimeContainer);
        TextInputEditText dateInput = view.findViewById(R.id.dateInput);
        TextInputEditText startTimeInput = view.findViewById(R.id.startTimeInput);
        TextInputEditText endTimeInput = view.findViewById(R.id.endTimeInput);
        durationHoursInput = view.findViewById(R.id.durationHoursInput);
        durationMinutesInput = view.findViewById(R.id.durationMinutesInput);
        MaterialButton confirmButton = view.findViewById(R.id.confirmButton);
        travelModeSelector = view.findViewById(R.id.travelModeSelector);

        // DEFAULT STATE
        toggleGroup.check(R.id.modeDurationButton);
        updateDisplayedDate(dateInput);
        updateDisplayedTime(startTimeInput, startHour, startMinute);
        updateDisplayedTime(endTimeInput, endHour, endMinute);


        // TRAVEL MODES
        String[] travelModes = {
                "WALKING",
                "DRIVING"
        };
        travelModeSelector.setSimpleItems(travelModes);
        travelModeSelector.setText("WALKING", false);

        // MODE TOGGLE
        toggleGroup.addOnButtonCheckedListener(this::toggleMode);

        // DATE PICKER
        dateInput.setOnClickListener(v -> openDatePicker(dateInput));

        // START TIME
        startTimeInput.setOnClickListener(v ->

                openTimePicker(
                        startHour,
                        startMinute,
                        (hour, minute) -> {

                            startHour = hour;
                            startMinute = minute;

                            updateDisplayedTime(
                                    startTimeInput,
                                    hour,
                                    minute
                            );
                        }
                )
        );

        // END TIME
        endTimeInput.setOnClickListener(v ->

                openTimePicker(
                        endHour,
                        endMinute,
                        (hour, minute) -> {

                            endHour = hour;
                            endMinute = minute;

                            updateDisplayedTime(
                                    endTimeInput,
                                    hour,
                                    minute
                            );
                        }
                )
        );

        // CONFIRM
        confirmButton.setOnClickListener(v -> confirm());
    }

    // --- Internal Lifecycle ---
    private void toggleMode(MaterialButtonToggleGroup group, int checkedId, boolean isChecked){
        if (!isChecked) return;

        useDurationMode = checkedId == R.id.modeDurationButton;

        durationContainer.setVisibility(useDurationMode ? View.VISIBLE : View.GONE);
        endTimeContainer.setVisibility(useDurationMode ? View.GONE : View.VISIBLE);
    }
    private void confirm(){
        PlannedTask plannedTask = new PlannedTask(UUID.randomUUID().toString(), taskId);

        Calendar startCalendar = (Calendar) selectedDate.clone();
        startCalendar.set(Calendar.HOUR_OF_DAY, startHour);
        startCalendar.set(Calendar.MINUTE, startMinute);
        startCalendar.set(Calendar.SECOND, 0);

        long startMillis = startCalendar.getTimeInMillis();

        long endMillis;

        if (useDurationMode) {

            int durationHours = parseInt(durationHoursInput.getText());
            int durationMinutes = parseInt(durationMinutesInput.getText());
            long durationMillis = ((durationHours * 60L) + durationMinutes) * 60_000L;

            endMillis = startMillis + durationMillis;

        } else {

            Calendar endCalendar = (Calendar) selectedDate.clone();

            endCalendar.set(Calendar.HOUR_OF_DAY, endHour);
            endCalendar.set(Calendar.MINUTE, endMinute);
            endCalendar.set(Calendar.SECOND, 0);

            endMillis = endCalendar.getTimeInMillis();
        }

        plannedTask.startTimeMillis = startMillis;
        plannedTask.endTimeMillis = endMillis;

        plannedTask.travelMode = travelModeSelector.getText().toString();

        if (listener != null) {
            listener.onPlannedTaskConfirmed(plannedTask);
        }
    }

    // --- Time Selection ---
    private interface TimeSelectionListener {
        void onTimeSelected(int hour, int minute);
    }
    private void openTimePicker(int initialHour, int initialMinute, TimeSelectionListener listener) {

        MaterialTimePicker picker =
                new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(initialHour)
                        .setMinute(initialMinute)
                        .setTitleText("Select Time")
                        .build();

        picker.addOnPositiveButtonClickListener(v ->

                listener.onTimeSelected(picker.getHour(), picker.getMinute())
        );

        picker.show(
                getParentFragmentManager(),
                "TIME_PICKER"
        );
    }
    private void updateDisplayedTime(TextInputEditText input, int hour, int minute) {

        String formatted = String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        hour,
                        minute
                );

        input.setText(formatted);
    }

    // --- Date Selection ---
    private void openDatePicker(TextInputEditText input) {

        MaterialDatePicker<Long> picker = MaterialDatePicker.Builder.datePicker().setTitleText("Select Date").build();

        picker.addOnPositiveButtonClickListener(selection -> {

            selectedDate.setTimeInMillis(selection);
            updateDisplayedDate(input);
        });

        picker.show(getParentFragmentManager(), "DATE_PICKER");
    }
    private void updateDisplayedDate(TextInputEditText input) {

        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy", Locale.getDefault());
        input.setText(sdf.format(selectedDate.getTime()));
    }

    // --- Helpers ---
    private int parseInt(Editable editable) {

        if (editable == null) return 0;

        String text = editable.toString().trim();

        if (text.isEmpty()) return 0;

        return Integer.parseInt(text);
    }
}
