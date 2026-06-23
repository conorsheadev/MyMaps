package com.csws.mymaps.services.activities.planner.weekly;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.core.models.plans.PlannedStage;
import com.csws.mymaps.core.models.plans.PlannedTask;
import com.csws.mymaps.core.ui.timeline.TimelineEntry;
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
            List<TimelineEntry> entries = buildEntries(day.getPlannedTasks(), day.getTasks());
            timelineView.render(entries);
        }
        private List<TimelineEntry> buildEntries(
                List<PlannedTask> plannedTasks,
                List<TaskItem> tasks
        ) {

            List<TimelineEntry> entries = new ArrayList<>();

            Map<String, TaskItem> taskMap = new HashMap<>();
            for (TaskItem t : tasks) {
                taskMap.put(t.id, t);
            }

            for (PlannedTask pt : plannedTasks) {

                TaskItem task = taskMap.get(pt.taskId);
                if (task == null) continue;

                if (pt.targetStartTimeMillis != null && pt.targetEndTimeMillis != null) {

                    TimelineEntry entry = new TimelineEntry();
                    entry.id = pt.id;
                    entry.title = task.title;
                    entry.startMillis = pt.targetStartTimeMillis;
                    entry.endMillis = pt.targetEndTimeMillis;
                    entry.level = 0;
                    entry.color = Color.parseColor("#4CAF50");

                    entries.add(entry);
                }

                if (pt.stages != null) {
                    for (PlannedStage stage : pt.stages) {

                        if (stage.scheduledStartMillis == null ||
                                stage.scheduledEndMillis == null) continue;

                        TimelineEntry entry = new TimelineEntry();
                        entry.id = stage.id;
                        entry.title = stage.title;
                        entry.startMillis = stage.scheduledStartMillis;
                        entry.endMillis = stage.scheduledEndMillis;
                        entry.level = 1;
                        entry.color = getStageColor(stage);

                        entries.add(entry);
                    }
                }
            }

            return entries;
        }

        //TODO: Move to Utils
        private int getStageColor(PlannedStage stage) {

            switch (stage.type) {
                case PACK_BAG:
                    return Color.parseColor("#FF9800");
                case LEAVE:
                    return Color.parseColor("#F44336");
                case NAVIGATION:
                    return Color.parseColor("#2196F3");
                case REMINDER:
                    return Color.parseColor("#9C27B0");
                case NOTES:
                    return Color.parseColor("#607D8B");
                default:
                    return Color.GRAY;
            }
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
