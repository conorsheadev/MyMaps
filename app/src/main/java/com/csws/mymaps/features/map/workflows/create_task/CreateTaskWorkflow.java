package com.csws.mymaps.features.map.workflows.create_task;

import com.csws.mymaps.domain.tasks.TaskItem;
import com.csws.mymaps.core.ui.forms.TaskConfigFragment;
import com.csws.mymaps.core.ui.pickers.CollectionPickerFragment;
import com.csws.mymaps.features.map.coordinators.MapViewContext;
import com.csws.mymaps.features.map.workflows.BaseWorkflow;

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
        taskConfigFragment.setSelectedCollection(viewModel.getCurrentCollection());

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