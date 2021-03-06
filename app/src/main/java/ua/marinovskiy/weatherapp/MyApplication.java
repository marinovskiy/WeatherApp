package ua.marinovskiy.weatherapp;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import ua.marinovskiy.weatherapp.services.NotificationService;
import ua.marinovskiy.weatherapp.utils.ListUtil;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Context mContext = getApplicationContext();

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this).build();
        Realm.setDefaultConfiguration(realmConfiguration);

        if (ListUtil.isConnected(mContext))
            startUpdateService();
    }

    private void startUpdateService() {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        long time = System.currentTimeMillis();
        Intent intent = new Intent(this, NotificationService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        long INTERVAL_3_HOUR = 3 * 60 * 60 * 1000;
        alarmManager.setRepeating(AlarmManager.RTC, time, INTERVAL_3_HOUR, pendingIntent);
    }

}
