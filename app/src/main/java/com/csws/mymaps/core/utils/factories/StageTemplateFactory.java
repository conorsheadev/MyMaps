package com.csws.mymaps.core.utils.factories;

import com.csws.mymaps.core.models.plans.PlannedStage;
import com.csws.mymaps.core.models.tasks.TaskStageTemplate;

import java.util.UUID;

public class StageTemplateFactory {

    public static TaskStageTemplate createLeaveReminder() {

        TaskStageTemplate stage =
                new TaskStageTemplate();

        stage.id =
                UUID.randomUUID().toString();

        stage.title =
                "Leave";

        stage.type =
                PlannedStage.StageType.LEAVE;

        stage.category =
                PlannedStage.StageCategory.PREPARATION;

        stage.config.put(
                "durationMinutes",
                "5"
        );

        return stage;
    }
    public static TaskStageTemplate createPackBag() {

        TaskStageTemplate stage =
                new TaskStageTemplate();

        stage.id =
                UUID.randomUUID().toString();

        stage.title =
                "Pack Bag";

        stage.type =
                PlannedStage.StageType.PACK_BAG;

        stage.category =
                PlannedStage.StageCategory.PREPARATION;

        stage.config.put(
                "durationMinutes",
                "10"
        );

        stage.config.put(
                "items",
                "Laptop,Charger,Notepad,Pen"
        );

        return stage;
    }

    public static TaskStageTemplate createNavigation() {

        TaskStageTemplate stage =
                new TaskStageTemplate();

        stage.id =
                UUID.randomUUID().toString();

        stage.title =
                "Navigation";

        stage.type =
                PlannedStage.StageType.NAVIGATION;

        stage.category =
                PlannedStage.StageCategory.ACTIVE;

        stage.config.put(
                "travelMode",
                "DRIVE"
        );

        stage.config.put(
                "dynamicDuration",
                "true"
        );

        return stage;
    }
}
