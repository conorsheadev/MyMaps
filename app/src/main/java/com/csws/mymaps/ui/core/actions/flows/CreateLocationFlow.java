package com.csws.mymaps.ui.core.actions.flows;

import com.csws.mymaps.R;
import com.csws.mymaps.model.locations.LocationItem;
import com.csws.mymaps.model.locations.MarkerConfig;
import com.csws.mymaps.model.locations.PolygonConfig;
import com.csws.mymaps.ui.core.actions.ActionFlow;
import com.csws.mymaps.ui.map.MapActions;
import com.csws.mymaps.ui.map.MapController;
import com.csws.mymaps.ui.map.deprecated.ActivityActions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.model.Place;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CreateLocationFlow implements ActionFlow, MapController.MapCallbacks {

    private final ActivityActions activityActions;
    private final MapActions mapActions;

    private List<LatLng> points = new ArrayList<>();
    private LatLng center;

    public CreateLocationFlow(ActivityActions activityActions, MapActions mapActions) {
        this.activityActions = activityActions;
        this.mapActions = mapActions;
    }

    @Override
    public void start() {
        // Step 1: waiting for user to pick location
    }

    @Override
    public void onMapClicked(LatLng latLng) {

    }
    @Override
    public void onLocationSelected(LocationItem location) {

    }
    @Override
    public void onAction(int action) {

        if (action == R.id.fab_confirm_polygon) {
            if (points.size() < 3) return;

            PolygonConfig polygon = new PolygonConfig(210f, points);

            LocationItem item = new LocationItem(
                    UUID.randomUUID().toString(),
                    "New Location",
                    points.get(0).latitude,
                    points.get(0).longitude,
                    polygon,
                    new MarkerConfig(0f,"default")
            );

            activityActions.createNewLocation(item);
        }

        else if (action == R.id.fab_undo_polygon) {
            if (!points.isEmpty()) {
                points.remove(points.size() - 1);
                mapActions.renderTempPolygon(points);
            }
        }

        else if (action == R.id.fab_cancel_polygon) {
            mapActions.clearTemp();
            activityActions.cancelCurrentFlow();
        }
    }
}
