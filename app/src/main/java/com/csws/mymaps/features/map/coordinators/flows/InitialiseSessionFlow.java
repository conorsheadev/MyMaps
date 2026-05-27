package com.csws.mymaps.features.map.coordinators.flows;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.csws.mymaps.core.flow.ActionFlow;
import com.csws.mymaps.domain.locations.LocationItem;
import com.csws.mymaps.domain.session.SessionStartType;
import com.csws.mymaps.features.map.controllers.ui.dialogs.SessionStartDialogFragment;
import com.csws.mymaps.features.map.coordinators.FlowContext;
import com.google.android.gms.maps.model.LatLng;

public class InitialiseSessionFlow implements ActionFlow, SessionStartDialogFragment.Listener {
    //Debugging
    private static final String TAG = "InitialiseSessionFlow";

    private final AppCompatActivity activity;
    private final FlowContext flowContext;

    private SessionStartDialogFragment dialog;

    public InitialiseSessionFlow(AppCompatActivity activity, FlowContext flowContext) {
        this.activity = activity;
        this.flowContext = flowContext;
    }

    @Override
    public void start() {

        Log.d(TAG, "start()");

        FragmentManager fm = activity.getSupportFragmentManager();

        Log.d(TAG, "FragmentManager state:"
                + " destroyed=" + fm.isDestroyed()
                + " stateSaved=" + fm.isStateSaved());

        Log.d(TAG, "Activity state:"
                + " destroyed=" + activity.isDestroyed()
                + " finishing=" + activity.isFinishing());

        Fragment existing = fm.findFragmentByTag("session_start");

        Log.d(TAG, "Existing dialog fragment = " + existing);

        dialog = new SessionStartDialogFragment();

        Log.d(TAG, "Dialog created");

        dialog.setListener(this);

        Log.d(TAG, "Calling dialog.show()");

        new Handler(Looper.getMainLooper()).post(() -> {

            Log.d(TAG, "Posting dialog.show()");

            dialog.show(
                    activity.getSupportFragmentManager(),
                    "session_start"
            );
        });

        Log.d(TAG, "dialog.show() complete");

        fm.executePendingTransactions();

        Log.d(TAG, "executePendingTransactions complete");
    }

    @Override
    public void onCancel() {

        if(dialog != null) {
            dialog.dismiss();
        }
    }



    @Override
    public void onSessionStartSelected(SessionStartType startType) {

        if(startType != SessionStartType.CONTINUED) {

            flowContext.sessionViewModel.createSession(startType);

        } else {

            flowContext.sessionViewModel.loadLatestSession();
        }

        //flowContext.flowNavigator.cancelCurrentFlow();
        //flowContext.flowNavigator.startDefaultFlow();
    }

    // --- Unused FAB Callbacks ---
    @Override
    public void onAction(int action) {

    }

    // --- Unused MapCallbacks ---
    @Override
    public void onMapClicked(LatLng latLng) {}
    @Override
    public void onLocationSelected(LocationItem location) {}
    @Override
    public void onRecenterClicked() {}
}