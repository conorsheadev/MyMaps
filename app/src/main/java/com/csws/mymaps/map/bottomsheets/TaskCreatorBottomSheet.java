package com.csws.mymaps.map.bottomsheets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.csws.mymaps.R;
import com.csws.mymaps.data.tasks.TaskItem;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
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

public class TaskCreatorBottomSheet {
    public interface Listener {
        void onTaskCreated(TaskItem task);
    }

    private final AppCompatActivity activity;
    private Dialog dialog;

    public TaskCreatorBottomSheet(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void show(String locationId, Listener listener) {

        dialog = new Dialog(activity);
        //dialog.getWindow().getDecorView().setBackgroundColor(Color.RED);

        View view = LayoutInflater.from(activity)
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
        typeSelector.setText("SCHEDULED", false);

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

        // --- Time Pickers ---
        startTimeButton.setOnClickListener(v -> {
            pickDateTime(result -> {
                startTimeMillis[0] = result;
                startTimeButton.setText("Start: " + formatDateTime(result));
            });
        });

        endTimeButton.setOnClickListener(v -> {
            pickDateTime(result -> {
                endTimeMillis[0] = result;
                endTimeButton.setText("End: " + formatDateTime(result));
            });
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

        if (view == null) Log.e("TaskSheet", "View is NULL");
        Log.d("TaskSheet", "dialog.show() called");

        Window window = dialog.getWindow();
        if (window != null) {
            window.setDimAmount(0.8f);
            window.setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
        }


    }
    private String formatDateTime(long millis) {
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, HH:mm", Locale.getDefault());
        return sdf.format(new Date(millis));
    }
    private void pickDateTime(Consumer<Long> onResult) {

        // --- Date Picker ---
        MaterialDatePicker<Long> datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .setTitleText("Select date")
                        .build();

        datePicker.addOnPositiveButtonClickListener(date -> {

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(date);

            // --- Time Picker ---
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

            timePicker.show(activity.getSupportFragmentManager(), "TIME_PICKER");

        });

        datePicker.show(activity.getSupportFragmentManager(), "DATE_PICKER");
    }
}
