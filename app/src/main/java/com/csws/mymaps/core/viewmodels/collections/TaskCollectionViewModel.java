package com.csws.mymaps.core.viewmodels.collections;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csws.mymaps.core.storage.collections.TaskCollectionRepository;
import com.csws.mymaps.core.models.tasks.TaskCollection;

import java.util.ArrayList;
import java.util.List;

public class TaskCollectionViewModel extends AndroidViewModel {
    private final TaskCollectionRepository repository;
    private final MutableLiveData<List<TaskCollection>> collections = new MutableLiveData<>();


    public TaskCollectionViewModel(@NonNull Application application) {
        super(application);
        repository = new TaskCollectionRepository(application);
        loadCollections();
    }

    private void loadCollections() {
        List<TaskCollection> loaded = repository.loadCollections();
        collections.setValue(loaded);
    }

    public LiveData<List<TaskCollection>> getCollections() {return collections;}

    public void addCollection(TaskCollection collection) {

        List<TaskCollection> current = collections.getValue();

        if (current == null) {
            current = new ArrayList<>();
        }

        current.add(collection);
        repository.saveCollections(current);
        collections.setValue(current);
    }

    public void removeCollection(String collectionId) {

        List<TaskCollection> current = collections.getValue();

        if (current == null) {
            return;
        }

        current.removeIf(collection -> collection.id.equals(collectionId));
        repository.saveCollections(current);
        collections.setValue(current);
    }
}
