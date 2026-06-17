package com.csws.mymaps.core.storage.sessions;

import android.content.Context;
import android.util.Log;

import com.csws.mymaps.core.models.sessions.DailySession;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SessionRepository {

    private static final String TAG = "SessionRepository";
    private static final String SESSION_FOLDER = "sessions";

    private final Context context;
    private final Gson gson;

    public SessionRepository(Context context) {

        this.context = context;

        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

        ensureSessionDirectory();
    }

    // ---------------------------------------------------
    // PUBLIC API
    // ---------------------------------------------------

    public void saveSession(DailySession session) {

        try {

            File file = getSessionFile(session.date);

            FileWriter writer = new FileWriter(file);

            gson.toJson(session, writer);

            writer.flush();
            writer.close();

        } catch (Exception e) {

            Log.e(TAG, "Failed to save session", e);
        }
    }

    public DailySession loadSession(String date) {

        try {

            File file = getSessionFile(date);

            if (!file.exists()) {
                return null;
            }

            FileReader reader = new FileReader(file);

            DailySession session = gson.fromJson(reader, DailySession.class);

            reader.close();

            return session;

        } catch (Exception e) {

            Log.e(TAG, "Failed to load session", e);

            return null;
        }
    }

    public boolean hasSession(String date) {

        return getSessionFile(date).exists();
    }

    public DailySession loadLatestSession() {

        File dir = getSessionDirectory();

        File[] files = dir.listFiles();

        if (files == null || files.length == 0) {
            return null;
        }

        Arrays.sort(files, (a, b) ->
                b.getName().compareTo(a.getName())
        );

        try {

            FileReader reader = new FileReader(files[0]);

            DailySession session =
                    gson.fromJson(reader, DailySession.class);

            reader.close();

            return session;

        } catch (Exception e) {

            Log.e(TAG, "Failed to load latest session", e);

            return null;
        }
    }

    public List<DailySession> loadAllSessions() {

        List<DailySession> result = new ArrayList<>();

        File dir = getSessionDirectory();

        File[] files = dir.listFiles();

        if (files == null) {
            return result;
        }

        for (File file : files) {

            try {

                FileReader reader = new FileReader(file);

                DailySession session =
                        gson.fromJson(reader, DailySession.class);

                reader.close();

                if (session != null) {
                    result.add(session);
                }

            } catch (Exception e) {

                Log.e(TAG,
                        "Failed to load session: " + file.getName(),
                        e);
            }
        }

        return result;
    }

    // ---------------------------------------------------
    // FILE HELPERS
    // ---------------------------------------------------

    private void ensureSessionDirectory() {

        File dir = getSessionDirectory();

        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private File getSessionDirectory() {

        return new File(
                context.getFilesDir(),
                SESSION_FOLDER
        );
    }

    private File getSessionFile(String date) {

        String filename = date + ".json";

        return new File(
                getSessionDirectory(),
                filename
        );
    }
}
