package com.csws.mymaps.features.map.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.csws.mymaps.data.SessionRepository;
import com.csws.mymaps.domain.session.DailySession;
import com.csws.mymaps.domain.session.SessionStartType;

import java.time.LocalDate;

public class SessionViewModel extends AndroidViewModel {

    private final SessionRepository repository;
    private final MutableLiveData<DailySession> currentSession = new MutableLiveData<>();

    public SessionViewModel(@NonNull Application application) {
        super(application);

        repository = new SessionRepository(application);
    }

    // ---------------------------------------------------
    // GETTERS
    // ---------------------------------------------------

    public LiveData<DailySession> getCurrentSession() {
        return currentSession;
    }

    // ---------------------------------------------------
    // SESSION MANAGEMENT
    // ---------------------------------------------------

    public boolean hasSessionToday() {
        return repository.hasSession(getTodayDateString());
    }

    public void loadTodaySession() {

        DailySession session = repository.loadSession(getTodayDateString());

        currentSession.setValue(session);
    }

    public void createSession(SessionStartType startType) {

        String today = getTodayDateString();

        DailySession session = new DailySession(today, System.currentTimeMillis(),startType);

        repository.saveSession(session);

        currentSession.setValue(session);
    }

    public void loadLatestSession() {

        DailySession session =
                repository.loadLatestSession();

        currentSession.setValue(session);
    }

    public void saveCurrentSession() {

        DailySession session = currentSession.getValue();

        if (session != null) {
            repository.saveSession(session);
        }
    }

    // ---------------------------------------------------
    // HELPERS
    // ---------------------------------------------------

    private String getTodayDateString() {
        return LocalDate.now().toString();
    }
}
