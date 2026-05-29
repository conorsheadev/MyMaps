package com.csws.mymaps.features.map.ui.top_sheets;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.session.SessionStartType;
import com.google.android.material.button.MaterialButton;

public class SessionStartFragment extends Fragment {

    private static final String TAG = "SessionStartFragment";

    public interface Listener {
        void onSessionStartSelected(SessionStartType startType);
    }
    private Listener listener; public void setListener(Listener listener) { this.listener = listener; }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.top_sheet_session_start, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Log.d(TAG, "onViewCreated");

        MaterialButton imAwakeButton = view.findViewById(R.id.imAwakeButton);
        MaterialButton imUpButton = view.findViewById(R.id.imUpButton);
        MaterialButton imReadyButton = view.findViewById(R.id.imReadyButton);
        TextView continueText = view.findViewById(R.id.continueText);

        imAwakeButton.setOnClickListener(v -> {
            Log.d(TAG, "IM_AWAKE selected");
            notifySelection(SessionStartType.IM_AWAKE);
        });

        imUpButton.setOnClickListener(v -> {
            Log.d(TAG, "IM_UP selected");
            notifySelection(SessionStartType.IM_UP);
        });

        imReadyButton.setOnClickListener(v -> {
            Log.d(TAG, "IM_READY selected");
            notifySelection(SessionStartType.IM_READY);
        });

        continueText.setOnClickListener(v -> {
            Log.d(TAG, "CONTINUED selected");
            notifySelection(SessionStartType.CONTINUED);
        });
    }

    private void notifySelection(SessionStartType startType) {

        if (listener == null) {

            Log.e(TAG, "listener == null");

            return;
        }

        listener.onSessionStartSelected(startType);
    }
}