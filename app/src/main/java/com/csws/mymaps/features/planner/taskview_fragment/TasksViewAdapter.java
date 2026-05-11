package com.csws.mymaps.features.planner.taskview_fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.planner.LocationTasks;

import java.util.ArrayList;
import java.util.List;

public class TasksViewAdapter extends RecyclerView.Adapter<TasksViewAdapter.SectionViewHolder> {

    private List<LocationTasks> sections = new ArrayList<>();

    public void submitList(List<LocationTasks> newSections) {
        this.sections = newSections;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_task_section, parent, false);
        return new SectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, int position) {
        holder.bind(sections.get(position));
    }

    @Override
    public int getItemCount() {
        return sections.size();
    }

    // --- View Holder ---
    static class SectionViewHolder extends RecyclerView.ViewHolder {

        TextView locationTitle;
        RecyclerView horizontalRecycler;

        public SectionViewHolder(View itemView) {
            super(itemView);
            locationTitle = itemView.findViewById(R.id.locationTitle);
            horizontalRecycler = itemView.findViewById(R.id.tasksHorizontalRecycler);
        }

        void bind(LocationTasks section) {

            locationTitle.setText(section.location.name);
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), RecyclerView.HORIZONTAL, false);

            //Internal Adapter
            horizontalRecycler.setLayoutManager(layoutManager);
            LocationTasksAdapter adapter = new LocationTasksAdapter();
            horizontalRecycler.setAdapter(adapter);
            adapter.submitList(section.plannedTasks, section.tasks);
        }
    }
}
