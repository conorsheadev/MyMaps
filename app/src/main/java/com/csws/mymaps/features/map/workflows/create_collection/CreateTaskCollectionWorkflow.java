package com.csws.mymaps.features.map.workflows.create_collection;

import com.csws.mymaps.domain.tasks.TaskCollection;
import com.csws.mymaps.features.map.coordinators.MapViewContext;
import com.csws.mymaps.core.ui.forms.TaskCollectionConfigFragment;
import com.csws.mymaps.features.map.workflows.BaseWorkflow;

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
