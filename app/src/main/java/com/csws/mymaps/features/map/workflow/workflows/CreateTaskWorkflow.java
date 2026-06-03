package com.csws.mymaps.features.map.workflow.workflows;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.locations.MarkerConfig;
import com.csws.mymaps.domain.locations.PolygonConfig;
import com.csws.mymaps.domain.planner.PlannedTask;
import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.features.map.interaction.ui.bottom_sheets.PlannedTaskConfigFragment;
import com.csws.mymaps.features.map.interaction.ui.bottom_sheets.TaskConfigFragment;
import com.csws.mymaps.features.map.interaction.ui.top_sheets.PlaceSearchFragment;
import com.csws.mymaps.features.map.coordinators.MapViewContext;
import com.csws.mymaps.features.map.viewmodels.CreateTaskViewModel;
import com.csws.mymaps.features.map.workflow.BaseWorkflow;
import com.google.android.gms.maps.model.LatLng;

import java.util.UUID;

public class CreateTaskWorkflow extends BaseWorkflow {

    private final CreateTaskViewModel viewModel;

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

    private void openTaskConfig() {

        TaskConfigFragment fragment =
                new TaskConfigFragment();

        fragment.setListener(task -> {

            viewModel.setDraftTask(task);

            completeFlow();
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