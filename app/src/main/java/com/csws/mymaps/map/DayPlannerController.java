package com.csws.mymaps.map;

import android.content.Context;
import android.widget.RelativeLayout;

import com.csws.mymaps.data.tasks.TaskItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DayPlannerController {
    private final TimelineRenderer renderer;

    public DayPlannerController(Context context, RelativeLayout container) {

        TimelineRenderer.Config config = new TimelineRenderer.Config();
        config.startHour = 6;
        config.endHour = 22;

        renderer = new TimelineRenderer(context, container, config);
    }
    public void showDay(List<TaskItem> allTasks) {

        List<TaskItem> todayTasks = filterToday(allTasks);

        renderer.render(todayTasks);
    }

    private List<TaskItem> filterToday(List<TaskItem> tasks) {

        List<TaskItem> result = new ArrayList<>();

        Calendar today = Calendar.getInstance();

        for (TaskItem task : tasks) {

            if (task.startTimeMillis == 0) continue;

            Calendar taskCal = Calendar.getInstance();
            taskCal.setTimeInMillis(task.startTimeMillis);

            boolean sameDay =
                    taskCal.get(Calendar.YEAR) == today.get(Calendar.YEAR) &&
                            taskCal.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR);

            if (sameDay) result.add(task);
        }

        return result;
    }
}
