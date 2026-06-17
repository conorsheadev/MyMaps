package com.csws.mymaps.core.ui.prompts;




import com.csws.mymaps.core.models.prompts.PlannerPrompt;

public class PromptFragmentFactory {

    public static PlannerPromptFragment create(
            PlannerPrompt prompt) {

        switch (prompt.type) {

            case LEAVE_NOW:
                return SimplePromptFragment.newInstance(prompt);

            case PACK_BAGS:
                return PackBagPromptFragment.newInstance(prompt);

            default:
                return SimplePromptFragment.newInstance(prompt);
        }
    }
}

