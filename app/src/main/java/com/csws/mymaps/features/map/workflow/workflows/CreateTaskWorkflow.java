package com.csws.mymaps.features.map.workflow.workflows;

import android.util.Log;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.locations.MarkerConfig;
import com.csws.mymaps.domain.locations.PolygonConfig;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskCollection;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.features.map.interaction.ui.bottom_sheets.PlannedTaskConfigFragment;
import com.csws.mymaps.features.map.interaction.ui.bottom_sheets.TaskConfigFragment;
import com.csws.mymaps.features.map.interaction.ui.bottom_sheets.pickers.CollectionPickerFragment;
import com.csws.mymaps.features.map.interaction.ui.top_sheets.PlaceSearchFragment;
import com.csws.mymaps.features.map.coordinators.MapViewContext;
import com.csws.mymaps.features.map.viewmodels.CreateTaskViewModel;
import com.csws.mymaps.features.map.workflow.BaseWorkflow;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class CreateTaskWorkflow extends BaseWorkflow implements TaskConfigFragment.Listener {

    private final CreateTaskViewModel viewModel;
    private TaskConfigFragment taskConfigFragment;

    public CreateTaskWorkflow(CreateTaskViewModel viewModel, MapViewContext context) {
        super(context);
        this.viewModel = viewModel;
    }

    @Override
    public void start() {

        viewModel.reset();

        openTaskConfig();
    }

    @Override
    public void stop() {

        resetUI();

        viewModel.reset();
    }

    // --- TaskConfig ---
    private void openTaskConfig() {

        taskConfigFragment = new TaskConfigFragment();

        taskConfigFragment.setListener(this);
        taskConfigFragment.setSelectedCollection(
                viewModel.getCurrentCollection()
        );

        context.bottomSheetController.show(taskConfigFragment);
    }
    @Override
    public void onTaskConfirmed(TaskItem task) {

        viewModel.setDraftTask(task);
        completeFlow();
    }

    @Override
    public void onSelectCollectionRequested() {

        openCollectionPicker();
    }

    // --- Collection Picker ---
    private void openCollectionPicker() {

        CollectionPickerFragment fragment = new CollectionPickerFragment();

        fragment.setListener(collection -> {

            viewModel.setCollection(collection);
            openTaskConfig();
        });

        context.bottomSheetController.show(fragment);
    }

    private void completeFlow() {

        TaskItem task =
                viewModel.getCurrentTask();

        if (task == null) {
            return;
        }

        context.sessionActions.createNewTask(task);

        context.workflowNavigator.finishWorkflow();
    }


}