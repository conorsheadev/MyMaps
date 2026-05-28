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

public class InitialiseSessionFlow extends BaseFlow implements SessionStartFragment.Listener {

    private final SessionStartFragment fragment;

    public InitialiseSessionFlow(FlowContext context) {

        super(context);

        fragment = new SessionStartFragment();

        fragment.setListener(this);
    }

    @Override
    public void start() {
        context.topSheetController.show(fragment);
    }

    @Override
    public void stop() {
        context.topSheetController.hide();
    }

    @Override
    public void onSessionStartSelected(SessionStartType startType) {

        if (startType != SessionStartType.CONTINUED) {

            context.sessionViewModel.createSession(startType);

        } else {

            context.sessionViewModel.loadLatestSession();
        }

        context.flowNavigator.cancelCurrentFlow();

        context.flowNavigator.startDefaultFlow();
    }
}