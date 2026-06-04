package com.csws.mymaps.features.map.interaction.ui.bottom_sheets.pickers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.domain.locations.LocationItem;

import java.util.List;

public class LocationPickerAdapter
        extends RecyclerView.Adapter<LocationPickerAdapter.ViewHolder> {

    public interface Listener {
        void onLocationClicked(LocationItem location);
    }

    private final List<LocationItem> locations;
    private final Listener listener;

    public LocationPickerAdapter(
            List<LocationItem> locations,
            Listener listener
    ) {
        this.locations = locations;
        this.listener = listener;
    }

    static class ViewHolder
            extends RecyclerView.ViewHolder {

        TextView title;
        TextView subtitle;

        ViewHolder(View view) {
            super(view);

            title =
                    view.findViewById(R.id.title);

            subtitle =
                    view.findViewById(R.id.subtitle);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent,
            int viewType
    ) {

        View view =
                LayoutInflater.from(parent.getContext())
                        .inflate(
                                R.layout.item_location_picker,
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

        LocationItem location = locations.get(position);

        holder.title.setText(location.name);

        holder.subtitle.setText(location.type);

        holder.itemView.setOnClickListener(v ->
                listener.onLocationClicked(location)
        );
    }

    @Override
    public int getItemCount() {
        return locations.size();
    }
}
