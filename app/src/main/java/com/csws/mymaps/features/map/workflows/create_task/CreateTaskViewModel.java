package com.csws.mymaps.features.map.workflows.create_task;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.csws.mymaps.domain.tasks.TaskCollection;
import com.csws.mymaps.domain.tasks.TaskItem;

public class CreateTaskViewModel extends ViewModel {

    private final MutableLiveData<TaskCollection> selectedCollection =
            new MutableLiveData<>();

    private final MutableLiveData<TaskItem> draftTask =
            new MutableLiveData<>();

    public void reset() {

        selectedCollection.setValue(null);
        draftTask.setValue(null);
    }

    // -------------------------
    // Collection
    // -------------------------

    public void setCollection(TaskCollection collection) {
        selectedCollection.setValue(collection);
    }

    public LiveData<TaskCollection> getCollection() {
        return selectedCollection;
    }

    public TaskCollection getCurrentCollection() {
        return selectedCollection.getValue();
    }

    // -------------------------
    // Draft Task
    // -------------------------

    public void setDraftTask(TaskItem task) {
        draftTask.setValue(task);
    }

    public LiveData<TaskItem> getDraftTask() {
        return draftTask;
    }

    public TaskItem getCurrentTask() {
        return draftTask.getValue();
    }
}