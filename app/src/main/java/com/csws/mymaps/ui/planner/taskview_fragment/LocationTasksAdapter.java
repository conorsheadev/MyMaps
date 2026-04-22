package com.csws.mymaps.ui.planner.taskview_fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.model.tasks.TaskItem;

import java.util.ArrayList;
import java.util.List;

public class LocationTasksAdapter extends RecyclerView.Adapter<LocationTasksAdapter.TaskViewHolder> {
    private List<TaskItem> tasks = new ArrayList<>();

    public void submitList(List<TaskItem> newTasks) {
        tasks = newTasks;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_task_tile, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        holder.bind(tasks.get(position));
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView taskTitle, taskTime;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskTime = itemView.findViewById(R.id.taskTime);
        }

        void bind(TaskItem task) {
            taskTitle.setText(task.title);
            taskTime.setText(task.startTimeMillis + " - " + task.endTimeMillis);
        }
    }
}
