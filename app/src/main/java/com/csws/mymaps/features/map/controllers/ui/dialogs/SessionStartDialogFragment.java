package com.csws.mymaps.features.map.controllers.ui.dialogs;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.csws.mymaps.domain.session.SessionStartType;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class SessionStartDialogFragment extends DialogFragment {
    public interface Listener {
        void onSessionStartSelected(SessionStartType startType);
    }

    private Listener listener; public void setListener(Listener listener) { this.listener = listener; }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(requireContext());

        builder.setTitle("Start Your Day");

        String[] options = {
                "I'm Awake",
                "I'm Up",
                "I'm Ready",
                "Continue Previous Day"
        };

        builder.setItems(options, (dialog, which) -> {

            if (listener == null) return;

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

        return builder.create();
    }
}
