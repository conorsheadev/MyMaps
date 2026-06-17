package com.csws.mymaps.core.models.navigation;

import com.csws.mymaps.core.models.prompts.PlannerPromptResult;

public class NavigationRoute {

    public String id;

    public String planId;

    public String destinationName;

    public String encodedPolyline;

    public long durationSeconds;

    public long distanceMeters;

    public static NavigationRoute fromPrompt(
            PlannerPromptResult result
    ) {

        NavigationRoute route =
                new NavigationRoute();

        route.encodedPolyline =
                result.data.get("encodedPolyline");

        try {

            route.distanceMeters =
                    Integer.parseInt(
                            result.data.get("distanceMeters")
                    );

        } catch (Exception ignored) {}

        try {

            route.durationSeconds =
                    Integer.parseInt(
                            result.data.get("durationSeconds")
                    );

        } catch (Exception ignored) {}

        return route;
    }
}
