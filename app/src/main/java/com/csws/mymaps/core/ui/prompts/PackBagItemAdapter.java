package com.csws.mymaps.core.ui.prompts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;

import java.util.List;

public class PackBagItemAdapter
        extends RecyclerView.Adapter<PackBagItemAdapter.ViewHolder> {

    private final List<String> items;

    public PackBagItemAdapter(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(
                        R.layout.item_pack_bag,
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

        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        private final CheckBox checkBox;

        ViewHolder(View itemView) {
            super(itemView);

            checkBox =
                    itemView.findViewById(
                            R.id.itemCheckbox
                    );
        }

        void bind(String item) {

            checkBox.setText(item);
            checkBox.setChecked(false);
        }
    }
}