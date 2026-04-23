package com.csws.mymaps.ui.places;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.google.android.libraries.places.api.model.AutocompletePrediction;

import java.util.ArrayList;
import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder> {

    public interface Listener {
        void onPlaceClicked(AutocompletePrediction place);
    }

    private List<AutocompletePrediction> items = new ArrayList<>();
    private final Listener listener;

    public PlacesAdapter(Listener listener) {
        this.listener = listener;
    }

    public void submitList(List<AutocompletePrediction> list) {
        items = list;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_place_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        AutocompletePrediction item = items.get(position);

        holder.name.setText(item.getPrimaryText(null));
        holder.address.setText(item.getSecondaryText(null));

        holder.itemView.setOnClickListener(v -> listener.onPlaceClicked(item));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, address;

        ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.placeName);
            address = view.findViewById(R.id.placeAddress);
        }
    }
}
