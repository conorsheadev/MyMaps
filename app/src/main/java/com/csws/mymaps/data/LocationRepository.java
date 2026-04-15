package com.csws.mymaps.data;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class LocationRepository {

    private static final String FILE_NAME = "locations.json";

    private final Context context;
    private final Gson gson = new Gson();

    public LocationRepository(Context context) {
        this.context = context;
    }

    public List<LocationItem> loadLocations() {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);

            if (!file.exists()) return new ArrayList<>();

            FileReader reader = new FileReader(file);

            Type type = new TypeToken<List<LocationItem>>(){}.getType();
            List<LocationItem> locations = gson.fromJson(reader, type);

            reader.close();

            return locations != null ? locations : new ArrayList<>();

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void saveLocations(List<LocationItem> locations) {
        try {
            File file = new File(context.getFilesDir(), FILE_NAME);

            FileWriter writer = new FileWriter(file);
            gson.toJson(locations, writer);
            writer.flush();
            writer.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
