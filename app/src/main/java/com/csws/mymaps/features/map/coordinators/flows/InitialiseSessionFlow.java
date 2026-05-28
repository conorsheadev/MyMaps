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
import com.csws.mymaps.features.map.controllers.ui.top_sheets.SessionStartFragment;
import com.csws.mymaps.features.map.coordinators.FlowContext;
import com.google.android.gms.maps.model.LatLng;

public class InitialiseSessionFlow implements ActionFlow, SessionStartFragment.Listener {
    //Debugging
    private static final String TAG = "InitialiseSessionFlow";
    private static final String FRAGMENT_TAG = "session_start";

    private final FlowContext flowContext;

    private SessionStartFragment fragment;

    public InitialiseSessionFlow(FlowContext flowContext) {
        this.flowContext = flowContext;
    }

    @Override
    public void start() {

        Log.d(TAG, "start()");

        fragment = new SessionStartFragment();

        fragment.setListener(this);

        Log.d(TAG, "Fragment created");

        flowContext.topSheetController.show(fragment);

        Log.d(TAG, "Fragment Passed to top sheet controller");


    }

    @Override
    public void onCancel() {

        flowContext.topSheetController.hide();
    }



    @Override
    public void onSessionStartSelected(SessionStartType startType) {

        if(startType != SessionStartType.CONTINUED) {
            flowContext.sessionViewModel.createSession(startType);
        } else {
            flowContext.sessionViewModel.loadLatestSession();
        }

        flowContext.flowNavigator.cancelCurrentFlow();
        flowContext.flowNavigator.startDefaultFlow();
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