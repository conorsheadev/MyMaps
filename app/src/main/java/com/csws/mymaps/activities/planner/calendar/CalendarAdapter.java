package com.csws.mymaps.activities.planner.calendar;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.google.android.material.card.MaterialCardView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.DayViewHolder> {

    public interface Listener {
        void onDateSelected(LocalDate date);
    }

    private final Listener listener;
    private List<CalendarDay> days = new ArrayList<>();

    public CalendarAdapter(Listener listener) {
        this.listener = listener;
    }

    public void submitList(List<CalendarDay> days) {
        this.days = days;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar_day, parent, false);
        return new DayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DayViewHolder holder, int position) {

        CalendarDay day = days.get(position);
        holder.bind(day, listener);
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    static class DayViewHolder extends RecyclerView.ViewHolder {

        private final MaterialCardView card;
        private final TextView label;

        public DayViewHolder(@NonNull View itemView) {
            super(itemView);

            card = (MaterialCardView) itemView;
            label = itemView.findViewById(R.id.dayLabel);
        }

        public void bind(CalendarDay day, Listener listener) {

            if (day.date == null) {

                label.setText("");
                card.setCardBackgroundColor(Color.TRANSPARENT);
                itemView.setOnClickListener(null);

                return;
            }

            label.setText(String.valueOf(day.date.getDayOfMonth()));
            Context context = itemView.getContext();
            int background = Color.TRANSPARENT;
            int textColor = label.getCurrentTextColor();

            // SELECTED
            if (day.isSelected) {

                background = ContextCompat.getColor(
                        context,
                        R.color.md_theme_error
                );
            }

            // TODAY
            else if (day.isToday) {

                background = ContextCompat.getColor(
                        context,
                        R.color.md_theme_inversePrimary
                );
            }

            card.setCardBackgroundColor(background);

            label.setTextColor(textColor);

            itemView.setOnClickListener(v -> {
                listener.onDateSelected(day.date);
            });
        }
    }
}
