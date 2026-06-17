package com.csws.mymaps.services.activities.planner.weekly;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.core.ui.timeline.TimelineView;
import com.csws.mymaps.services.activities.planner.models.PlannerDay;
import com.csws.mymaps.core.models.tasks.TaskItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WeeklyPlannerAdapter extends RecyclerView.Adapter<WeeklyPlannerAdapter.DayViewHolder> {

    private List<PlannerDay> days = new ArrayList<>();

    public void submitList(List<PlannerDay> newDays) {
        days = newDays;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_day_card, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {
        holder.bind(days.get(position));
    }

    @Override
    public int getItemCount() {
        return days.size();
    }


    // --- View Holder ---
    static class DayViewHolder extends RecyclerView.ViewHolder {

        TextView dayTitle, dayDate, taskCount;
        TimelineView timelineView;


        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTitle = itemView.findViewById(R.id.dayTitle);
            dayDate = itemView.findViewById(R.id.dayDate);
            taskCount = itemView.findViewById(R.id.taskCount);
            timelineView = itemView.findViewById(R.id.timeline_view);
        }

        void bind(PlannerDay day) {
            dayTitle.setText(day.getDayName());
            dayDate.setText(day.getFormattedDate());
            taskCount.setText(day.getTasks().size() + " tasks");

            Map<String, TaskItem> taskMap = buildTaskMap(day.getTasks());
            timelineView.render(day.getPlannedTasks(), taskMap);
        }

        private Map<String, TaskItem> buildTaskMap(List<TaskItem> tasks) {

            Map<String, TaskItem> map = new HashMap<>();

            for (TaskItem task : tasks) {
                map.put(task.id, task);
            }

            return map;
        }
    }
}
