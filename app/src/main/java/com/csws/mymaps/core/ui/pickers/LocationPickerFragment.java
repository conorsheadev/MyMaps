package com.csws.mymaps.core.ui.pickers;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.csws.mymaps.R;
import com.csws.mymaps.core.viewmodels.locations.LocationViewModel;
import com.csws.mymaps.core.models.locations.LocationItem;

public class LocationPickerFragment extends Fragment {

    public interface Listener {
        void onLocationSelected(LocationItem location);
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        return inflater.inflate(
                R.layout.bottom_sheet_location_picker,
                container,
                false
        );
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recycler =
                view.findViewById(R.id.locationRecycler);

        recycler.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        LocationViewModel locationViewModel =
                new ViewModelProvider(requireActivity())
                        .get(LocationViewModel.class);

        locationViewModel.getLocations().observe(
                getViewLifecycleOwner(),
                locations -> {

                    LocationPickerAdapter adapter =
                            new LocationPickerAdapter(
                                    locations,
                                    location -> {

                                        if (listener != null) {
                                            listener.onLocationSelected(location);
                                        }
                                    }
                            );

                    recycler.setAdapter(adapter);
                }
        );
    }
}
