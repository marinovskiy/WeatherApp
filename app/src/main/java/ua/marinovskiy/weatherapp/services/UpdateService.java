package ua.marinovskiy.weatherapp.services;

import android.app.IntentService;
import android.content.Intent;

import ua.marinovskiy.weatherapp.utils.DataUtil;

public class UpdateService extends IntentService {

    public UpdateService() {
        super("UpdateService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        DataUtil.uploadData(getApplicationContext());
    }
}
