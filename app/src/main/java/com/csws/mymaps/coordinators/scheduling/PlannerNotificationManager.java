package com.csws.mymaps.coordinators.scheduling;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;

import com.csws.mymaps.R;
import com.csws.mymaps.core.contracts.services.NotificationService;

import java.util.concurrent.atomic.AtomicInteger;

public class PlannerNotificationManager implements NotificationService {

    private final Context context;

    public PlannerNotificationManager(Context context) {
        this.context = context;
    }

    private static final AtomicInteger NEXT_ID = new AtomicInteger(1);

    private int generateNotificationId() {

        return NEXT_ID.getAndIncrement();
    }

    @Override
    public void showPrompt(
            String title,
            String message
    ) {



        if (ContextCompat.checkSelfPermission(context,Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context,"planner")
                            .setSmallIcon(R.drawable.school_24px)
                            .setContentTitle(title)
                            .setContentText(message)
                            .setPriority(
                                    NotificationCompat.PRIORITY_HIGH
                            )
                            .setAutoCancel(true);

            NotificationManagerCompat.from(context).notify(generateNotificationId(), builder.build());
        }
    }


}
