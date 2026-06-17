package com.csws.mymaps.core.ui.prompts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.csws.mymaps.R;
import com.csws.mymaps.core.models.prompts.PlannerPrompt;
import com.csws.mymaps.core.models.prompts.PlannerPromptResult;
import com.google.android.material.button.MaterialButton;

public class SimplePromptFragment extends  PlannerPromptFragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_MESSAGE = "message";

    public static SimplePromptFragment newInstance(PlannerPrompt prompt) {

        SimplePromptFragment fragment = new SimplePromptFragment();
        Bundle args = new Bundle();

        args.putString(ARG_TITLE, prompt.title);
        args.putString(ARG_MESSAGE, prompt.message);

        fragment.setArguments(args);

        fragment.prompt = prompt;

        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.top_sheet_planner_prompt, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

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

            PlannerPromptResult result = new PlannerPromptResult();

            result.promptId = prompt.id;
            result.planId = prompt.planId;

            result.type = PlannerPromptResult.ResultType.COMPLETED;

            submitResult(result);
        });

        dismissButton.setOnClickListener(v -> {

            PlannerPromptResult result = new PlannerPromptResult();

            result.promptId = prompt.id;
            result.planId = prompt.planId;

            result.type = PlannerPromptResult.ResultType.DISMISSED;

            submitResult(result);
        });
    }
}
