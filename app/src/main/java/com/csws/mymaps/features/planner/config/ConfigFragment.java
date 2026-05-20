package com.csws.mymaps.features.planner.config;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.planner.PlannerConfig;
import com.csws.mymaps.features.planner.viewmodels.PlannerConfigViewModel;
import com.google.android.material.timepicker.MaterialTimePicker;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class ConfigFragment extends Fragment {

    private PlannerConfigViewModel configViewModel;
    private LinearLayout wakeUpContainer;

    public ConfigFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pagefragment_config, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        wakeUpContainer = view.findViewById(R.id.wakeUpContainer);

        configViewModel = new ViewModelProvider(requireActivity()).get(PlannerConfigViewModel.class);
        configViewModel.getConfig().observe(getViewLifecycleOwner(), this::renderWakeUpTimes);
    }

    private void renderWakeUpTimes(PlannerConfig config) {

        wakeUpContainer.removeAllViews();

        for (DayOfWeek day : DayOfWeek.values()) {

            View row = LayoutInflater.from(requireContext())
                    .inflate(R.layout.item_config_time, wakeUpContainer, false);

            TextView dayLabel = row.findViewById(R.id.dayLabel);
            Button timeButton = row.findViewById(R.id.timeButton);
            LocalTime wakeTime = config.getWakeUpTime(day);

            dayLabel.setText(day.name());

            timeButton.setText(wakeTime.format(DateTimeFormatter.ofPattern("HH:mm")));

            timeButton.setOnClickListener(v -> {
                showTimePicker(day, wakeTime);
            });

            wakeUpContainer.addView(row);
        }
    }

    private void showTimePicker(DayOfWeek day, LocalTime current) {

        MaterialTimePicker picker =
                new MaterialTimePicker.Builder()
                        .setHour(current.getHour())
                        .setMinute(current.getMinute())
                        .setTitleText("Select wake up time")
                        .build();

        picker.addOnPositiveButtonClickListener(v -> {

            LocalTime selected = LocalTime.of(picker.getHour(), picker.getMinute());

            configViewModel.setWakeUpTime(day, selected);
        });

        picker.show(
                getParentFragmentManager(),
                "wake_time_picker"
        );
    }
}
