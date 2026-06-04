package com.csws.mymaps.features.map.workflows.create_collection;

import androidx.lifecycle.ViewModel;

import com.csws.mymaps.domain.tasks.TaskCollection;

public class CreateCollectionViewModel extends ViewModel {

    private TaskCollection draftCollection;

    public void setDraftCollection(TaskCollection collection) {
        this.draftCollection = collection;
    }

    public TaskCollection getDraftCollection() {
        return draftCollection;
    }

    public void reset() {
        draftCollection = null;
    }
}