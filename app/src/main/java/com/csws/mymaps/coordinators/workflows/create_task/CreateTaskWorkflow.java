package com.csws.mymaps.coordinators.workflows.create_task;

import com.csws.mymaps.core.models.tasks.TaskItem;
import com.csws.mymaps.core.ui.forms.TaskConfigFragment;
import com.csws.mymaps.core.ui.pickers.CollectionPickerFragment;
import com.csws.mymaps.coordinators.CoordinatorContext;
import com.csws.mymaps.coordinators.workflows.BaseWorkflow;

public class CreateTaskWorkflow extends BaseWorkflow implements TaskConfigFragment.Listener {

    private final CreateTaskViewModel viewModel;
    private TaskConfigFragment taskConfigFragment;

    public CreateTaskWorkflow(CreateTaskViewModel viewModel, CoordinatorContext context) {
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

        context.uiCoordinator.showBottomSheet(taskConfigFragment);
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

        CollectionPickerFragment pickerFragment = new CollectionPickerFragment();

        pickerFragment.setListener(collection -> {

            viewModel.setCollection(collection);
            openTaskConfig();
        });

        context.uiCoordinator.showBottomSheet(pickerFragment);
    }

    private void completeFlow() {
        TaskItem task = viewModel.getCurrentTask();

        if (task == null) {return;}
        context.entityCreationService.createTask(task);
        context.workflowManager.finishWorkflow();
    }


}