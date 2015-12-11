package ua.marinovskiy.weatherapp.db;

import android.content.Context;
import android.os.AsyncTask;

import java.util.List;

import io.realm.Realm;
import ua.marinovskiy.weatherapp.entities.Weather;
import ua.marinovskiy.weatherapp.utils.DataUtil;

public class DatabaseWriter extends AsyncTask<Void, Void, Void> {

    private Context mContext;
    private List<Weather> mWeatherList;

    public DatabaseWriter(Context context, List<Weather> weatherList) {
        mContext = context;
        mWeatherList = weatherList;
    }

    @Override
    protected Void doInBackground(Void... params) {

        /** delete old data **/
        DataUtil.deleteData(mContext);

        Realm mRealm = Realm.getInstance(mContext);
        mRealm.beginTransaction();
        for (int i = 0; i < mWeatherList.size(); i++) {
            Weather weather = new Weather();
            weather.setTime(mWeatherList.get(i).getTime());
            weather.setDate(mWeatherList.get(i).getDate());
            weather.setIcon(mWeatherList.get(i).getIcon());
            weather.setTemperature(mWeatherList.get(i).getTemperature());
            weather.setCondition(mWeatherList.get(i).getCondition());
            weather.setDescription(mWeatherList.get(i).getDescription());
            weather.setWindDeg(mWeatherList.get(i).getWindDeg());
            weather.setWindSpeed(mWeatherList.get(i).getWindSpeed());
            weather.setHumidity(mWeatherList.get(i).getHumidity());
            weather.setPressure(mWeatherList.get(i).getPressure());
            mRealm.copyToRealm(weather);
        }
        mRealm.commitTransaction();
        mRealm.close();
        return null;
    }
}
