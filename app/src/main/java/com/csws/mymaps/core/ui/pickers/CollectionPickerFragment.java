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
import com.csws.mymaps.core.state.TaskCollectionViewModel;
import com.csws.mymaps.domain.tasks.TaskCollection;

public class CollectionPickerFragment extends Fragment {

    public interface Listener {
        void onCollectionSelected(TaskCollection collection);
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    private CollectionPickerAdapter adapter;

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {

        return inflater.inflate(
                R.layout.bottom_sheet_collection_picker,
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

        RecyclerView recyclerView =
                view.findViewById(R.id.recyclerView);

        adapter = new CollectionPickerAdapter();

        adapter.setListener(collection -> {

            if (listener != null) {
                listener.onCollectionSelected(collection);
            }
        });

        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        recyclerView.setAdapter(adapter);

        TaskCollectionViewModel viewModel =
                new ViewModelProvider(requireActivity())
                        .get(TaskCollectionViewModel.class);

        viewModel.getCollections().observe(
                getViewLifecycleOwner(),
                adapter::submitList
        );
    }
}