package com.csws.mymaps.activities.planner.tasks.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.core.models.tasks.TaskItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TaskTileAdapter extends RecyclerView.Adapter<TaskTileAdapter.TaskViewHolder> {

    private List<PlannedTask> plannedTasks = new ArrayList<>();
    private Map<String,TaskItem> tasks = new HashMap<>();

    public void submitList(List<PlannedTask> newPlannedTasks,Map<String, TaskItem> newTasks) {
        plannedTasks = newPlannedTasks;
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
        PlannedTask plannedTask = plannedTasks.get(position);
        TaskItem task = tasks.get(plannedTask.taskId);
        if(task!= null){
            holder.bind(plannedTask, task);
        }
    }

    @Override
    public int getItemCount() {
        return plannedTasks.size();
    }

    static class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView taskTitle, taskTime;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskTime = itemView.findViewById(R.id.taskTime);
        }

        void bind(PlannedTask plannedTask, TaskItem task) {
            taskTitle.setText(task.title);
            taskTime.setText(formatTimeRange(plannedTask.targetStartTimeMillis, plannedTask.targetEndTimeMillis));
        }

        private String formatTimeRange(Long start, Long end) {
            //TODO: Setup DateTime Utils for Formatting
            if (start == null || end == null) {
                return "Unscheduled";
            }

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

            return sdf.format(new Date(start)) + " - " + sdf.format(new Date(end));
        }
    }
}
