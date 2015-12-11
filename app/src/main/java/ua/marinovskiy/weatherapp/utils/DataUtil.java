package ua.marinovskiy.weatherapp.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.List;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmResults;
import ua.marinovskiy.weatherapp.R;
import ua.marinovskiy.weatherapp.db.DatabaseWriter;
import ua.marinovskiy.weatherapp.entities.Weather;
import ua.marinovskiy.weatherapp.loaders.JSONLoader;

public class DataUtil {

    public static void uploadData(Context context) {
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        String mCity = mPreferences.getString("city", "");
        String mUrlPart1 = context.getResources().getString(R.string.urlPart1);
        String mUrlPart2 = context.getResources().getString(R.string.urlPart2);
        String mUrl = String.format("%s%s%s", mUrlPart1, mCity, mUrlPart2);

        try {
            /** get new data from JSON **/
            List<Weather> weatherList = new JSONLoader().execute(mUrl).get();
            if (!weatherList.isEmpty()) {
                /** write new data into database **/
                new DatabaseWriter(context, weatherList).execute().get();
            } else {
                deleteData(context);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    public static void deleteData(Context context) {
        Realm realm = Realm.getInstance(context);
        RealmResults<Weather> realmResults = realm.where(Weather.class).findAll();
        realm.beginTransaction();
        if (realmResults.size() != 0) {
            realmResults.clear();
        }
        realm.commitTransaction();
        realm.close();
    }

}
