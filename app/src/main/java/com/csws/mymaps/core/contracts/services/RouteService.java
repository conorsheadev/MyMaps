package com.csws.mymaps.core.contracts.services;


import com.csws.mymaps.core.models.navigation.NavigationRoute;
import com.google.android.gms.maps.model.LatLng;

public interface RouteService {
    void calculateRoute(LatLng origin, LatLng destination, RouteCallback callback);

    public interface RouteCallback {

        void onRouteReady(
                NavigationRoute route
        );

        void onRouteError(
                Exception exception
        );
    }
}
