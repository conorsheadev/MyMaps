package com.csws.mymaps.coordinators.scheduling.executors;

import com.csws.mymaps.core.models.plans.PlannedStage;

import java.util.ArrayList;
import java.util.List;

public class StageConfigUtils {

    public static int getMinutesBefore(
            PlannedStage stage,
            int defaultValue
    ) {

        String value =
                stage.config.get("minutesBefore");

        if (value == null) {
            return defaultValue;
        }

        try {

            return Integer.parseInt(value);

        } catch (Exception e) {

            return defaultValue;
        }
    }

    public static List<String> getPackItems(
            PlannedStage stage
    ) {

        String value =
                stage.config.get("items");

        if (value == null || value.isEmpty()) {

            return new ArrayList<>();
        }

        String[] parts =
                value.split(",");

        List<String> items =
                new ArrayList<>();

        for (String item : parts) {

            items.add(item.trim());
        }

        return items;
    }

    public static double getDestinationLat(
            PlannedStage stage
    ) {

        String value =
                stage.config.get("destinationLat");

        if (value == null) {
            return 0;
        }

        try {

            return Double.parseDouble(value);

        } catch (Exception e) {

            return 0;
        }
    }

    public static double getDestinationLng(
            PlannedStage stage
    ) {

        String value =
                stage.config.get("destinationLng");

        if (value == null) {
            return 0;
        }

        try {

            return Double.parseDouble(value);

        } catch (Exception e) {

            return 0;
        }
    }
    public static String getDestinationId(
            PlannedStage stage
    ) {

        return stage.config.get(
                "destinationId"
        );
    }
}
