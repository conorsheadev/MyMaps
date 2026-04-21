package com.csws.mymaps.ui.planner;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;

public class WeeklyPlannerFragment extends Fragment {
    private RecyclerView recyclerView;

    public WeeklyPlannerFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.pagefragment_weeklyplanner, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.plannerCarousel);

        setupCarousel();
    }

    private void setupCarousel() {

    }
}
