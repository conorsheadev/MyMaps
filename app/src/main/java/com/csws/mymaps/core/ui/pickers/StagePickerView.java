package com.csws.mymaps.core.ui.pickers;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.core.ui.forms.TaskStageAdapter;
import com.csws.mymaps.core.models.plans.PlannedStage;
import com.csws.mymaps.core.utils.factories.StageFactory;
import com.csws.mymaps.core.utils.factories.StageTemplateFactory;
import com.csws.mymaps.core.models.tasks.TaskStageTemplate;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

public class StagePickerView extends LinearLayout {

    private RecyclerView recyclerView;
    private MaterialButton addButton;

    private TaskStageAdapter adapter;

    private final List<TaskStageTemplate> stages = new ArrayList<>();

    public StagePickerView(Context context) {
        super(context);
        init();
    }

    public StagePickerView(
            Context context,
            AttributeSet attrs
    ) {
        super(context, attrs);
        init();
    }

    public StagePickerView(
            Context context,
            AttributeSet attrs,
            int defStyleAttr
    ) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        inflate(
                getContext(),
                R.layout.view_stage_picker,
                this
        );

        recyclerView =
                findViewById(R.id.recyclerStages);

        addButton =
                findViewById(R.id.btnAddStage);

        adapter =
                new TaskStageAdapter(stages);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(
                        getContext()
                )
        );

        recyclerView.setAdapter(adapter);

        addButton.setOnClickListener(
                v -> showStagePickerDialog()
        );
    }

    public List<TaskStageTemplate> getStages() {

        return new ArrayList<>(stages);
    }

    public void setStages(
            List<TaskStageTemplate> stages
    ) {

        this.stages.clear();

        if (stages != null) {

            this.stages.addAll(stages);
        }

        adapter.notifyDataSetChanged();
    }
    public List<PlannedStage> createStages() {

        List<PlannedStage> result =
                new ArrayList<>();

        for(TaskStageTemplate template : stages) {

            result.add(
                    StageFactory.create(template)
            );
        }

        return result;
    }
    private void showStagePickerDialog() {

        String[] options = {
                "Pack Bag",
                "Leave Reminder",
                "Navigation",
                "Reminder",
                "Notes"
        };

        new MaterialAlertDialogBuilder(getContext())
                .setTitle("Add Stage")
                .setItems(options, (d, which) -> {

                    TaskStageTemplate stage = null;

                    switch (which) {

                        case 0:
                            stage = StageTemplateFactory.createPackBag();
                            break;

                        case 1:
                            stage = StageTemplateFactory.createLeaveReminder();
                            break;

                        case 2:
                            stage = StageTemplateFactory.createNavigation();
                            break;

                    }

                    if (stage != null) {

                        adapter.add(stage);
                    }
                })
                .show();
    }
}
