package com.csws.mymaps.ui.planner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.model.planner.PlannerDay;
import com.csws.mymaps.model.tasks.TaskItem;

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

        TextView dateText;
        RelativeLayout taskContainer;


        public DayViewHolder(@NonNull View itemView) {
            super(itemView);
            dateText = itemView.findViewById(R.id.dayTitle);
            taskContainer = itemView.findViewById(R.id.taskContainer);
        }

        void bind(PlannerDay day) {

            dateText.setText(day.date.getDayOfWeek() + "\n" + day.date);

            taskContainer.removeAllViews();

            for (TaskItem task : day.tasks) {
                TextView tv = new TextView(itemView.getContext());
                tv.setText(task.title);
                taskContainer.addView(tv);
            }
        }
    }
}
