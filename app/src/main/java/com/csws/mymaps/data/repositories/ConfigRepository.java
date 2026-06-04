package com.csws.mymaps.data.repositories;

import android.content.Context;
import android.util.Log;

import com.csws.mymaps.domain.planner.PlannerConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class ConfigRepository {

    private static final String TAG = "ConfigRepository";
    private static final String CONFIG_FOLDER = "config";

    private final Context context;
    private final Gson gson;

    public ConfigRepository(Context context) {

        this.context = context;

        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        ensureConfigDirectory();
    }

    public void saveConfig(PlannerConfig config) {

        try {

            File file = getConfigFile();

            FileWriter writer = new FileWriter(file);

            gson.toJson(config, writer);

            writer.flush();
            writer.close();

        } catch (Exception e) {

            Log.e(TAG, "Failed to save config", e);
        }
    }

    public PlannerConfig loadConfig() {
        try {

            File file = getConfigFile();

            if (!file.exists()) {
                PlannerConfig defaultConfig = new PlannerConfig();
                saveConfig(defaultConfig);
                return defaultConfig;
            }

            FileReader reader = new FileReader(file);

            PlannerConfig config = gson.fromJson(reader, PlannerConfig.class);

            reader.close();

            return config;

        } catch (Exception e) {
            Log.e(TAG, "Failed to load session", e);
            return null;
        }
    }

    private void ensureConfigDirectory() {

        File dir = getConfigDirectory();

        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private File getConfigDirectory() {
        return new File(context.getFilesDir(), CONFIG_FOLDER);
    }

    private File getConfigFile() {

        String filename = "config.json";

        return new File(
                getConfigDirectory(),
                filename
        );
    }
}
