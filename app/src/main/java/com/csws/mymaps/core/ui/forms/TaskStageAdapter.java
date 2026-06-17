package com.csws.mymaps.core.ui.forms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.core.models.tasks.TaskStageTemplate;

import java.util.Collections;
import java.util.List;

public class TaskStageAdapter
        extends RecyclerView.Adapter<TaskStageAdapter.StageViewHolder> {

    private final List<TaskStageTemplate> stages;

    public TaskStageAdapter(List<TaskStageTemplate> stages) {

        this.stages = stages;
    }

    @NonNull
    @Override
    public StageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(
                        R.layout.item_task_stage,
                        parent,
                        false
                );

        return new StageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StageViewHolder holder, int position) {

        TaskStageTemplate stage = stages.get(position);

        holder.txtTitle.setText(stage.title);

        holder.btnMoveUp.setOnClickListener(v -> moveUp(holder.getAdapterPosition()));

        holder.btnMoveDown.setOnClickListener(v -> moveDown(holder.getAdapterPosition()));

        holder.btnDelete.setOnClickListener(v -> remove(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return stages.size();
    }

    public void add(TaskStageTemplate stage) {

        stages.add(stage);

        notifyItemInserted(stages.size() - 1);
    }

    public void remove(int position) {

        if(position < 0 || position >= stages.size()) {return;}

        stages.remove(position);

        notifyItemRemoved(position);
    }

    public void moveUp(int position) {

        if(position <= 0) {return;}

        Collections.swap(stages, position, position - 1);

        notifyItemMoved(position, position - 1);
    }

    public void moveDown(int position) {

        if(position >= stages.size() - 1) {
            return;
        }

        Collections.swap(stages, position, position + 1);

        notifyItemMoved(position, position + 1);
    }

    public List<TaskStageTemplate> getStages() {
        return stages;
    }

    static class StageViewHolder
            extends RecyclerView.ViewHolder {

        TextView txtTitle;

        ImageButton btnMoveUp;
        ImageButton btnMoveDown;
        ImageButton btnDelete;

        public StageViewHolder(@NonNull View itemView) {

            super(itemView);

            txtTitle =
                    itemView.findViewById(
                            R.id.txtStageTitle);

            btnMoveUp =
                    itemView.findViewById(
                            R.id.btnMoveUp);

            btnMoveDown =
                    itemView.findViewById(
                            R.id.btnMoveDown);

            btnDelete =
                    itemView.findViewById(
                            R.id.btnDelete);
        }
    }
}