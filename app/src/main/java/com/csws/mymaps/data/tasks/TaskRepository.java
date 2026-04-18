package com.csws.mymaps.data.tasks;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static final String FILE_NAME = "tasks.json";

    private final Context context;
    private final Gson gson = new Gson();

    public TaskRepository(Context context){
        this.context = context;
    }

    public List<TaskItem> loadTasks() {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);

            if (!file.exists()) return new ArrayList<>();

            FileReader reader = new FileReader(file);

            Type type = new TypeToken<List<TaskItem>>(){}.getType();
            List<TaskItem> tasks = gson.fromJson(reader, type);

            reader.close();
            Log.d("TaskRepository", "Tasks loaded:" + tasks.toString());
            return tasks != null ? tasks : new ArrayList<>();

        } catch (Exception e) {
            Log.d("TaskRepository", "Error loading tasks");
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveTasks(List<TaskItem> tasks) {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);

            FileWriter writer = new FileWriter(file);
            gson.toJson(tasks, writer);
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
