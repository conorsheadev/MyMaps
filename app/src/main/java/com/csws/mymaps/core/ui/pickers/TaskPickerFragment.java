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
import com.csws.mymaps.core.viewmodels.tasks.TaskViewModel;
import com.csws.mymaps.core.models.tasks.TaskItem;

public class TaskPickerFragment extends Fragment {

    public interface Listener {
        void onTaskSelected(TaskItem task);
    }

    private Listener listener;

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_task_picker, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recycler =
                view.findViewById(R.id.taskRecycler);

        recycler.setLayoutManager(
                new LinearLayoutManager(requireContext())
        );

        TaskViewModel taskViewModel =
                new ViewModelProvider(requireActivity())
                        .get(TaskViewModel.class);

        taskViewModel.getTasks().observe(
                getViewLifecycleOwner(),
                tasks -> {

                    TaskPickerAdapter adapter =
                            new TaskPickerAdapter(
                                    tasks,
                                    task -> {

                                        if (listener != null) {
                                            listener.onTaskSelected(task);
                                        }
                                    });

                    recycler.setAdapter(adapter);
                });
    }
}