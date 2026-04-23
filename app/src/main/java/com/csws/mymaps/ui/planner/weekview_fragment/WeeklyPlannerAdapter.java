package com.csws.mymaps.ui.planner.weekview_fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.model.planner.PlannerDay;
import com.csws.mymaps.ui.core.TimelineRenderer;

import java.util.ArrayList;
import java.util.List;

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

    static class DayViewHolder extends RecyclerView.ViewHolder {

        TextView dayTitle, dayDate, taskCount;
        RelativeLayout timelineContainer;


        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            dayTitle = itemView.findViewById(R.id.dayTitle);
            dayDate = itemView.findViewById(R.id.dayDate);
            taskCount = itemView.findViewById(R.id.taskCount);
            timelineContainer = itemView.findViewById(R.id.timelineContainer);
        }

        void bind(PlannerDay day) {

            dayTitle.setText(day.getDayName());
            dayDate.setText(day.getFormattedDate());
            taskCount.setText(day.getTasks().size() + " tasks");

            TimelineRenderer.Config config = new TimelineRenderer.Config();
            TimelineRenderer renderer = new TimelineRenderer(itemView.getContext(), timelineContainer, config);
            renderer.render(day.getTasks());
        }
    }
}
