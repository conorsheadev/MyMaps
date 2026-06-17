package com.csws.mymaps.core.ui.forms;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.csws.mymaps.R;
import com.csws.mymaps.core.ui.pickers.StagePickerView;
import com.csws.mymaps.core.models.tasks.TaskCollection;
import com.csws.mymaps.core.models.tasks.TaskItem;
import com.csws.mymaps.core.models.tasks.TaskStageTemplate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.UUID;

public class TaskConfigFragment extends Fragment {
    //TODO: Clean Up
    public interface Listener {
        void onTaskConfirmed(TaskItem task);
        void onSelectCollectionRequested();
    }
    private Listener listener;
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public static TaskConfigFragment newInstance() {
        return new TaskConfigFragment();
    }

    private TaskCollection selectedCollection;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.bottom_sheet_task_create, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // --- Views ---
        editCollection = view.findViewById(R.id.editCollection);
        TextInputEditText editName = view.findViewById(R.id.editTaskName);
        TextInputEditText editDesc = view.findViewById(R.id.editTaskDescription);
        TextInputEditText editIcon = view.findViewById(R.id.editIcon);
        MaterialAutoCompleteTextView typeSelector = view.findViewById(R.id.typeSelector);
        MaterialButton confirmButton = view.findViewById(R.id.confirmButton);
        StagePickerView stagePicker = view.findViewById(R.id.stagePicker);


        // --- Edit Collection ---
        if(selectedCollection != null) { editCollection.setText(selectedCollection.title);}
        editCollection.setOnClickListener(v -> {

            if (listener != null) {
                listener.onSelectCollectionRequested();
            }
        });
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
                    selectedCollection.id,
                    name,
                    desc,
                    getText(editIcon),
                    type
            );

            List<TaskStageTemplate> stageTemplates = stagePicker.getStages();

            for (int i = 0; i < stageTemplates.size(); i++) {

                stageTemplates.get(i).order = i;
            }

            task.stageTemplates = stageTemplates;

            if (listener != null) {
                listener.onTaskConfirmed(task);
            }
        });
    }

    // --- Collection Picker ---
    private TextInputEditText editCollection;
    public void setSelectedCollection(TaskCollection collection) {

        selectedCollection = collection;

        if (editCollection != null && collection != null) {
            editCollection.setText(collection.title);
        }
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