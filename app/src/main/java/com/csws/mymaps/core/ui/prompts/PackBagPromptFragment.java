package com.csws.mymaps.core.ui.prompts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.core.models.prompts.PlannerPrompt;
import com.csws.mymaps.core.models.prompts.PlannerPromptResult;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PackBagPromptFragment extends PlannerPromptFragment {


    public static PackBagPromptFragment newInstance(PlannerPrompt prompt) {

        PackBagPromptFragment fragment = new PackBagPromptFragment();

        fragment.prompt = prompt;

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.top_sheet_pack_bag_prompt, container, false);
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {

        RecyclerView recycler =
                view.findViewById(R.id.itemsRecycler);

        MaterialButton doneButton =
                view.findViewById(R.id.doneButton);

        MaterialButton remindButton =
                view.findViewById(R.id.remindButton);

        recycler.setLayoutManager(
                new LinearLayoutManager(
                        requireContext()
                )
        );

        recycler.setAdapter(
                new PackBagItemAdapter(
                        getItems()
                )
        );

        doneButton.setOnClickListener(v -> {

            PlannerPromptResult result =
                    new PlannerPromptResult();

            result.promptId = prompt.id;
            result.planId = prompt.planId;

            result.type =
                    PlannerPromptResult
                            .ResultType
                            .COMPLETED;

            submitResult(result);
        });

        remindButton.setOnClickListener(v -> {

            PlannerPromptResult result =
                    new PlannerPromptResult();

            result.promptId = prompt.id;
            result.planId = prompt.planId;

            result.type =
                    PlannerPromptResult
                            .ResultType
                            .SNOOZED;

            result.data.put(
                    "minutes",
                    "5"
            );

            submitResult(result);
        });
    }

    private List<String> getItems() {

        String value = prompt.data.get("items");

        if (value == null || value.isEmpty()) {
            return new ArrayList<>();
        }

        return Arrays.asList(value.split(","));
    }
}
