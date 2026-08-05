package com.csws.mymaps.activities.planner.tasks.adapters.location;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.activities.planner.models.LocationTasks;
import com.csws.mymaps.activities.planner.tasks.adapters.TaskTileAdapter;

import java.util.ArrayList;
import java.util.List;

public class LocationTasksAdapter extends RecyclerView.Adapter<LocationTasksAdapter.SectionViewHolder> {

    private List<LocationTasks> sections = new ArrayList<>();

    public void submitList(List<LocationTasks> newSections) {
        sections = newSections;
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

    static class SectionViewHolder extends RecyclerView.ViewHolder {

        private final TextView locationTitle;
        private final RecyclerView horizontalRecycler;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);

            locationTitle = itemView.findViewById(R.id.locationTitle);
            horizontalRecycler = itemView.findViewById(R.id.tasksHorizontalRecycler);
        }

        void bind(LocationTasks section) {

            locationTitle.setText(section.location.name);
            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), RecyclerView.HORIZONTAL, false);
            horizontalRecycler.setLayoutManager(layoutManager);
            TaskTileAdapter adapter = new TaskTileAdapter();
            horizontalRecycler.setAdapter(adapter);
            adapter.submitList(section.plannedTasks, section.tasks);
        }
    }
}
