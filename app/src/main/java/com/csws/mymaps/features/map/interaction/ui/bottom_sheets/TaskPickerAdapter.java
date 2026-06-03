package com.csws.mymaps.features.map.interaction.ui.bottom_sheets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.tasks.TaskItem;

import java.util.List;

public class TaskPickerAdapter
        extends RecyclerView.Adapter<TaskPickerAdapter.ViewHolder> {

    public interface Listener {
        void onTaskClicked(TaskItem task);
    }

    private final List<TaskItem> tasks;
    private final Listener listener;

    public TaskPickerAdapter(
            List<TaskItem> tasks,
            Listener listener
    ) {
        this.tasks = tasks;
        this.listener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        ViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(
            ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_task_picker,
                                parent,
                                false
                        );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            ViewHolder holder,
            int position
    ) {

        TaskItem task = tasks.get(position);

        holder.title.setText(task.title);

        holder.itemView.setOnClickListener(v ->
                listener.onTaskClicked(task)
        );
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }
}
