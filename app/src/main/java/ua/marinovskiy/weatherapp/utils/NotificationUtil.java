package ua.marinovskiy.weatherapp.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import ua.marinovskiy.weatherapp.R;
import ua.marinovskiy.weatherapp.activities.MainActivity;
import ua.marinovskiy.weatherapp.services.UpdateService;

public class NotificationUtil {

    private static final int NOTIFICATION_ID = 1;

    public static void callUpdateNotification(Context context) {

        Intent notificationIntent = new Intent(context, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                Intent.FLAG_ACTIVITY_CLEAR_TASK);

        Intent updateIntent = new Intent(context, UpdateService.class);
        PendingIntent updatePendingIntent = PendingIntent.getService(context, 0,
                updateIntent, 0);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.icon_update_notification)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                R.mipmap.ic_launcher))
                        .setContentTitle("New forecast")
                        .setContentText("Touch to view new forecast")
                        .addAction(R.drawable.icon_refresh, "Update", updatePendingIntent);

        PendingIntent notificationPendingIntent =
                PendingIntent.getActivity(
                        context,
                        0,
                        notificationIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );

        builder.setContentIntent(notificationPendingIntent);

        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, notification);
        //builder.getNotification().flags |= Notification.FLAG_AUTO_CANCEL;
    }

}
