package com.csws.mymaps.activities.planner.tasks;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.csws.mymaps.R;
import com.google.android.material.button.MaterialButtonToggleGroup;
import com.google.android.material.textfield.TextInputEditText;

public class TasksFragment extends Fragment {

    private TextInputEditText searchInput;
    private MaterialButtonToggleGroup viewModeGroup;
    private TasksViewController controller;

    public TasksFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pagefragment_tasks, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        //Init UI
        searchInput = view.findViewById(R.id.searchInput);
        viewModeGroup = view.findViewById(R.id.viewModeGroup);

        controller = new TasksViewController(getChildFragmentManager(), R.id.tasksViewContainer);

        setupChips();
        setupSearch();

        controller.setViewMode(TasksViewController.TaskViewMode.LOCATION);
    }

    // --- SETUP ---
    private void setupChips() {

        viewModeGroup.addOnButtonCheckedListener(
                (group, checkedId, isChecked) -> {

                    if (!isChecked) {
                        return;
                    }

                    if (checkedId == R.id.buttonLocation) {
                        controller.setViewMode(TasksViewController.TaskViewMode.LOCATION);
                    }

                    else if (checkedId == R.id.buttonType) {
                        controller.setViewMode(TasksViewController.TaskViewMode.TYPE);
                    }

                    else if (checkedId == R.id.buttonDate) {
                        controller.setViewMode(TasksViewController.TaskViewMode.DATE);
                    }
                }
        );
    }

    private void setupSearch() {

        searchInput.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                controller.notifySearchChanged(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
}
