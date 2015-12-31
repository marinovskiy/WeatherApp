package ua.marinovskiy.weatherapp.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import ua.marinovskiy.weatherapp.utils.ListUtil;
import ua.marinovskiy.weatherapp.utils.NotificationUtil;

public class NotificationService extends Service {

    private Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (ListUtil.isConnected(mContext)) {
            NotificationUtil.callUpdateNotification(mContext);
        }
        stopSelf();
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
