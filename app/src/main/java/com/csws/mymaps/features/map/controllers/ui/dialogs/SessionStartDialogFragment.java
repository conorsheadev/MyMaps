package com.csws.mymaps.features.map.controllers.ui.dialogs;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.csws.mymaps.domain.session.SessionStartType;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SessionStartDialogFragment extends DialogFragment {

    private static final String TAG = "SessionStartDialog";

    public interface Listener {
        void onSessionStartSelected(SessionStartType startType);
    }

    private Listener listener;

    public void setListener(Listener listener) {

        Log.d(TAG, "setListener");

        this.listener = listener;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, "onCreate");
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(TAG, "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume");
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);

        Log.d(TAG, "onDismiss");
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);

        Log.d(TAG, "onCancel");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Log.d(TAG, "onCreateDialog START");

        MaterialAlertDialogBuilder builder =
                new MaterialAlertDialogBuilder(requireContext());

        builder.setTitle("Start Your Day");

        String[] options = {
                "I'm Awake",
                "I'm Up",
                "I'm Ready",
                "Continue Previous Day"
        };

        builder.setItems(options, (dialog, which) -> {

            Log.d(TAG, "Option selected index=" + which);

            if (listener == null) {

                Log.e(TAG, "listener == null");

                return;
            }

            switch (which) {

                case 0:
                    listener.onSessionStartSelected(SessionStartType.IM_AWAKE);
                    break;

                case 1:
                    listener.onSessionStartSelected(SessionStartType.IM_UP);
                    break;

                case 2:
                    listener.onSessionStartSelected(SessionStartType.IM_READY);
                    break;

                case 3:
                    listener.onSessionStartSelected(SessionStartType.CONTINUED);
                    break;
            }
        });

        builder.setCancelable(false);

        Dialog dialog = builder.create();

        Log.d(TAG, "onCreateDialog END");

        return dialog;
    }
}