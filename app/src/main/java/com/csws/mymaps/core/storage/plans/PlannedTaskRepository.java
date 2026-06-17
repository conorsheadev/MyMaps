package com.csws.mymaps.core.storage.plans;

import android.content.Context;
import android.util.Log;

import com.csws.mymaps.core.models.plans.PlannedTask;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class PlannedTaskRepository {

    private static final String FILE_NAME = "planned_tasks.json";

    private final Context context;
    private final Gson gson = new Gson();

    public PlannedTaskRepository(Context context) {
        this.context = context;
    }

    public List<PlannedTask> loadPlannedTasks() {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);

            if (!file.exists()) {
                return new ArrayList<>();
            }

            FileReader reader = new FileReader(file);

            Type type = new TypeToken<List<PlannedTask>>(){}.getType();
            List<PlannedTask> tasks = gson.fromJson(reader, type);
            Log.d("PlannedTaskRepository", "PlannedTasks loaded:" + tasks.toString());
            reader.close();

            return tasks != null ? tasks : new ArrayList<>();
        } catch (Exception e) {
            Log.e("PlannedTaskRepository", "Failed to load planned tasks", e);
            return new ArrayList<>();
        }
    }

    public void savePlannedTasks(List<PlannedTask> tasks) {

        try {
            File file = new File(context.getFilesDir(), FILE_NAME);
            FileWriter writer = new FileWriter(file);
            gson.toJson(tasks, writer);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            Log.e("PlannedTaskRepository", "Failed to save planned tasks", e);
        }
    }
}
