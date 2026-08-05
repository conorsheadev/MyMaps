package com.csws.mymaps.services.routes;

import android.util.Log;

import com.csws.mymaps.core.models.navigation.NavigationEstimate;
import com.csws.mymaps.core.models.navigation.NavigationRoute;
import com.csws.mymaps.core.contracts.services.RouteService;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.UUID;

public class GoogleRouteService implements RouteService {

    private static final String TAG = "GoogleRouteService";
    private final OkHttpClient client = new OkHttpClient();
    private final String apiKey;

    public GoogleRouteService(String apiKey) {
        this.apiKey = apiKey;
    }

    @Override
    public void calculateRoute(LatLng origin, LatLng destination, RouteCallback callback) {

        requestRoute(
                origin,
                destination,
                new JsonResponseCallback() {

                    @Override
                    public void onSuccess(String json) {

                        parseRoute(json, callback);
                    }

                    @Override
                    public void onError(Exception exception) {

                        callback.onRouteError(exception);
                    }
                }
        );
    }

    @Override
    public void estimateRoute(
            LatLng origin,
            LatLng destination,
            TravelTimeCallback callback
    ) {

        requestRoute(
                origin,
                destination,
                new JsonResponseCallback() {

                    @Override
                    public void onSuccess(String json) {

                        parseEstimate(
                                json,
                                callback
                        );
                    }

                    @Override
                    public void onError(
                            Exception exception
                    ) {

                        callback.onEstimateError(
                                exception
                        );
                    }
                }
        );
    }


    private void parseRoute(String json, RouteCallback callback) {

        try {

            JSONObject root =
                    new JSONObject(json);

            JSONArray routes =
                    root.getJSONArray("routes");

            if (routes.length() == 0) {

                callback.onRouteError(
                        new Exception(
                                "No route found"
                        )
                );

                return;
            }

            JSONObject routeObj =
                    routes.getJSONObject(0);

            NavigationRoute route =
                    new NavigationRoute();

            route.id =
                    UUID.randomUUID().toString();

            route.distanceMeters =
                    routeObj.getLong(
                            "distanceMeters"
                    );

            route.durationSeconds =
                    parseDuration(
                            routeObj.getString(
                                    "duration"
                            )
                    );

            route.encodedPolyline =
                    routeObj
                            .getJSONObject(
                                    "polyline"
                            )
                            .getString(
                                    "encodedPolyline"
                            );

            callback.onRouteReady(route);

        } catch (Exception e) {

            callback.onRouteError(e);
        }
    }

    private void parseEstimate(
            String json,
            TravelTimeCallback callback
    ) {

        try {

            JSONObject root =
                    new JSONObject(json);

            JSONArray routes =
                    root.getJSONArray("routes");

            if (routes.length() == 0) {

                callback.onEstimateError(
                        new Exception(
                                "No route found"
                        )
                );

                return;
            }

            JSONObject routeObj =
                    routes.getJSONObject(0);

            NavigationEstimate estimate =
                    new NavigationEstimate();

            estimate.distanceMeters =
                    routeObj.getLong(
                            "distanceMeters"
                    );

            estimate.durationSeconds =
                    parseDuration(
                            routeObj.getString(
                                    "duration"
                            )
                    );

            callback.onEstimateReady(
                    estimate
            );

        } catch (Exception e) {

            callback.onEstimateError(
                    e
            );
        }
    }
    private long parseDuration(String duration) {

        return Long.parseLong(duration.replace("s", ""));
    }
    private interface JsonResponseCallback {

        void onSuccess(String json);

        void onError(Exception exception);
    }
    private void requestRoute(
            LatLng origin,
            LatLng destination,
            JsonResponseCallback callback
    ) {

        if (origin == null) {
            callback.onError(
                    new IllegalArgumentException("Origin is null")
            );
            return;
        }

        if (destination == null) {
            callback.onError(
                    new IllegalArgumentException("Destination is null")
            );
            return;
        }

        try {

            JSONObject body = new JSONObject();

            JSONObject originObj = new JSONObject();
            JSONObject destinationObj = new JSONObject();

            originObj.put(
                    "location",
                    new JSONObject()
                            .put(
                                    "latLng",
                                    new JSONObject()
                                            .put("latitude", origin.latitude)
                                            .put("longitude", origin.longitude)
                            )
            );

            destinationObj.put(
                    "location",
                    new JSONObject()
                            .put(
                                    "latLng",
                                    new JSONObject()
                                            .put("latitude", destination.latitude)
                                            .put("longitude", destination.longitude)
                            )
            );

            body.put("origin", originObj);
            body.put("destination", destinationObj);
            body.put("travelMode", "WALK");

            Request request =
                    new Request.Builder()
                            .url(
                                    "https://routes.googleapis.com/directions/v2:computeRoutes"
                            )
                            .post(
                                    RequestBody.create(
                                            MediaType.parse(
                                                    "application/json"
                                            ),
                                            body.toString()
                                    )
                            )
                            .addHeader(
                                    "X-Goog-Api-Key",
                                    apiKey
                            )
                            .addHeader(
                                    "X-Goog-FieldMask",
                                    "routes.distanceMeters,routes.duration,routes.polyline.encodedPolyline"
                            )
                            .build();

            client.newCall(request)
                    .enqueue(
                            new Callback() {

                                @Override
                                public void onFailure(
                                        Request request,
                                        IOException e
                                ) {
                                    callback.onError(e);
                                }

                                @Override
                                public void onResponse(
                                        Response response
                                ) throws IOException {

                                    if (!response.isSuccessful()) {

                                        String body = "";

                                        if (response.body() != null) {
                                            body =
                                                    response.body().string();
                                        }

                                        callback.onError(
                                                new IOException(
                                                        "Route request failed: "
                                                                + response.code()
                                                                + " "
                                                                + body
                                                )
                                        );

                                        return;
                                    }

                                    callback.onSuccess(
                                            response.body().string()
                                    );
                                }
                            }
                    );

        } catch (Exception e) {

            callback.onError(e);
        }
    }
}
