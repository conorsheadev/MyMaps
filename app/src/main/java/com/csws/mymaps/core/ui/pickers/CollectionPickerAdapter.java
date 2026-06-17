package com.csws.mymaps.core.ui.pickers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.core.models.tasks.TaskCollection;

import java.util.ArrayList;
import java.util.List;

public class CollectionPickerAdapter
        extends RecyclerView.Adapter<CollectionPickerAdapter.ViewHolder> {

    public interface Listener {
        void onCollectionSelected(TaskCollection collection);
    }

    private final List<TaskCollection> collections = new ArrayList<>();
    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    public void submitList(List<TaskCollection> newCollections) {

        collections.clear();

        if (newCollections != null) {
            collections.addAll(newCollections);
        }

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_collection,
                        parent,
                        false
                );

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(
            @NonNull ViewHolder holder,
            int position
    ) {

        holder.bind(collections.get(position));
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private final TextView titleText;
        private final TextView descriptionText;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            titleText = itemView.findViewById(R.id.titleText);
            descriptionText = itemView.findViewById(R.id.descriptionText);
        }

        void bind(TaskCollection collection) {

            titleText.setText(collection.title);

            descriptionText.setText(
                    collection.description != null
                            ? collection.description
                            : ""
            );

            itemView.setOnClickListener(v -> {

                if (listener != null) {
                    listener.onCollectionSelected(collection);
                }
            });
        }
    }
}
