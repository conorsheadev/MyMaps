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
import com.csws.mymaps.domain.planner.engine.PlannerPrompt;
import com.google.android.material.button.MaterialButton;

public class PlannerPromptFragment extends Fragment {

    // --- Args ---
    private static final String ARG_TITLE = "title";
    private static final String ARG_MESSAGE = "message";

    private PlannerPrompt prompt;

    // --- Listener ---
    public interface Listener {

        void onPromptDismissed();

        void onPromptAction(PlannerPrompt prompt);
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    // --- Factory ---
    public static PlannerPromptFragment newInstance(PlannerPrompt prompt) {

        PlannerPromptFragment fragment = new PlannerPromptFragment();

        Bundle args = new Bundle();

        args.putString(ARG_TITLE, prompt.title);
        args.putString(ARG_MESSAGE, prompt.message);

        fragment.setArguments(args);

        fragment.prompt = prompt;

        return fragment;
    }

    // --- Lifecycle ---
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Log.d("PlannerPromptFragment", "onCreateView");
        return inflater.inflate(
                R.layout.top_sheet_planner_prompt,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        TextView titleText = view.findViewById(R.id.promptTitle);
        TextView messageText = view.findViewById(R.id.promptMessage);
        MaterialButton actionButton = view.findViewById(R.id.promptActionButton);
        MaterialButton dismissButton = view.findViewById(R.id.promptDismissButton);

        Bundle args = getArguments();

        if (args != null) {

            titleText.setText(args.getString(ARG_TITLE));
            messageText.setText(args.getString(ARG_MESSAGE));
        }

        actionButton.setOnClickListener(v -> {

            if (listener != null) {

                listener.onPromptAction(prompt);
            }
        });

        dismissButton.setOnClickListener(v -> {

            if (listener != null) {

                listener.onPromptDismissed();
            }
        });
    }
}
