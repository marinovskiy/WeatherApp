package ua.marinovskiy.weatherapp.services;


import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import ua.marinovskiy.weatherapp.utils.NotificationUtil;

public class ReminderService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NotificationUtil.callUpdateNotification(getApplicationContext());
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
