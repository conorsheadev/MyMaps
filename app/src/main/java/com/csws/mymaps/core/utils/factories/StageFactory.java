package com.csws.mymaps.core.utils.factories;

import com.csws.mymaps.core.models.plans.PlannedStage;
import com.csws.mymaps.core.models.tasks.TaskStageTemplate;

import java.util.UUID;

public class StageFactory {

    public static PlannedStage create(
            TaskStageTemplate template
    ) {

        PlannedStage stage =
                new PlannedStage();

        stage.id =
                UUID.randomUUID().toString();

        stage.title =
                template.title;

        stage.type =
                template.type;

        stage.category =
                template.category;

        stage.order =
                template.order;

        stage.config.putAll(
                template.config
        );

        stage.scheduledStartMillis =
                null;

        stage.scheduledEndMillis =
                null;

        stage.actualStartMillis =
                null;

        stage.actualEndMillis =
                null;

        stage.requiresScheduleCalculation =
                stage.type == PlannedStage.StageType.NAVIGATION;

        return stage;
    }
}
