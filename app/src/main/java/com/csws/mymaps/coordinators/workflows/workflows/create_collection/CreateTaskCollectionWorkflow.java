package com.csws.mymaps.coordinators.workflows.workflows.create_collection;

import com.csws.mymaps.core.models.tasks.TaskCollection;
import com.csws.mymaps.coordinators.map.MapViewContext;
import com.csws.mymaps.core.ui.forms.TaskCollectionConfigFragment;
import com.csws.mymaps.coordinators.workflows.workflows.BaseWorkflow;

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

        context.uiCoordinator.showBottomSheet(fragment);
    }

    private void completeFlow() {

        TaskCollection collection = viewModel.getDraftCollection();

        if (collection == null) {
            return;
        }

        context.entityCreationService.createTaskCollection(collection);

        context.workflowManager.finishWorkflow();
    }
}
