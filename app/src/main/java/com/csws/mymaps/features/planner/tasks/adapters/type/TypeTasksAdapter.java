package com.csws.mymaps.features.planner.tasks.adapters.type;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.planner.taskviews.TypeTasks;
import com.csws.mymaps.features.planner.tasks.adapters.TaskTileAdapter;

import java.util.ArrayList;
import java.util.List;

public class TypeTasksAdapter extends RecyclerView.Adapter<TypeTasksAdapter.SectionViewHolder> {

    private List<TypeTasks> sections = new ArrayList<>();

    public void submitList(List<TypeTasks> newSections) {

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

    // --- VIEW HOLDER ---

    static class SectionViewHolder extends RecyclerView.ViewHolder {

        TextView sectionTitle;
        RecyclerView horizontalRecycler;

        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);

            sectionTitle = itemView.findViewById(R.id.locationTitle);
            horizontalRecycler = itemView.findViewById(R.id.tasksHorizontalRecycler);
        }

        void bind(TypeTasks section) {

            sectionTitle.setText(section.type);

            horizontalRecycler.setLayoutManager(new LinearLayoutManager(itemView.getContext(), RecyclerView.HORIZONTAL, false));

            TaskTileAdapter adapter = new TaskTileAdapter();

            horizontalRecycler.setAdapter(adapter);

            adapter.submitList(section.plannedTasks, section.tasks);
        }
    }
}
