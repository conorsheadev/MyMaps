package com.csws.mymaps.core.ui.prompts;

import androidx.fragment.app.Fragment;

import com.csws.mymaps.core.models.prompts.PlannerPrompt;
import com.csws.mymaps.core.models.prompts.PlannerPromptResult;

public abstract class PlannerPromptFragment extends Fragment {

    protected PlannerPrompt prompt;

    // --- Listener ---
    public interface Listener {
        void onPromptResult(PlannerPromptResult result);
    }
    protected Listener listener;
    public void setListener(Listener listener) {
        this.listener = listener;
    }
    protected void submitResult(PlannerPromptResult result) {

        if (listener != null) {

            listener.onPromptResult(result);
        }
    }
}
