package com.csws.mymaps.features.map.workflow.workflows;

import com.csws.mymaps.domain.tasks.TaskCollection;
import com.csws.mymaps.features.map.coordinators.MapViewContext;
import com.csws.mymaps.features.map.interaction.ui.bottom_sheets.TaskCollectionConfigFragment;
import com.csws.mymaps.features.map.viewmodels.CreateCollectionViewModel;
import com.csws.mymaps.features.map.workflow.BaseWorkflow;

public class CreateTaskCollectionWorkflow extends BaseWorkflow {

    private final CreateCollectionViewModel viewModel;

    public CreateTaskCollectionWorkflow(CreateCollectionViewModel viewModel, MapViewContext context) {
        super(context);
        this.viewModel = viewModel;
    }

    @Override
    public void start() {

        viewModel.reset();
        openCollectionConfig();
    }

    @Override
    public void stop() {

        resetUI();
        viewModel.reset();
    }

    private void openCollectionConfig() {

        TaskCollectionConfigFragment fragment = new TaskCollectionConfigFragment();

        fragment.setListener(collection -> {

            viewModel.setDraftCollection(collection);
            completeFlow();
        });

        context.bottomSheetController.show(fragment);
    }

    private void completeFlow() {

        TaskCollection collection = viewModel.getDraftCollection();

        if (collection == null) {
            return;
        }

        context.sessionActions.createTaskCollection(collection);

        context.workflowNavigator.finishWorkflow();
    }
}
