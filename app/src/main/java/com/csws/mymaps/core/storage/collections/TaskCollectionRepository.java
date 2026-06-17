package com.csws.mymaps.core.storage.collections;

import android.content.Context;
import android.util.Log;

import com.csws.mymaps.core.models.tasks.TaskCollection;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TaskCollectionRepository {

    private static final String FILE_NAME =
            "task_collections.json";

    private final Context context;

    private final Gson gson = new Gson();

    public TaskCollectionRepository(Context context) {
        this.context = context;
    }

    public List<TaskCollection> loadCollections() {

        try {

            File file = new File(context.getFilesDir(), FILE_NAME);

            if (!file.exists()) {
                return new ArrayList<>();
            }

            FileReader reader = new FileReader(file);

            Type type = new TypeToken<List<TaskCollection>>() {}.getType();

            List<TaskCollection> collections = gson.fromJson(reader, type);

            reader.close();

            Log.d("TaskCollectionRepository", "Collections loaded: " + collections);
            return collections != null ? collections : new ArrayList<>();
        }
        catch (Exception e) {

            Log.e("TaskCollectionRepository", "Error loading collections", e);
            return new ArrayList<>();
        }
    }

    public void saveCollections(List<TaskCollection> collections) {

        try {

            File file = new File(context.getFilesDir(), FILE_NAME);
            FileWriter writer = new FileWriter(file);

            gson.toJson(collections, writer);

            writer.flush();
            writer.close();

        }
        catch (Exception e) {

            Log.e("TaskCollectionRepository", "Error saving collections", e);
        }
    }
}
