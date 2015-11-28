package ua.marinovskiy.weatherapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import ua.marinovskiy.weatherapp.R;
import ua.marinovskiy.weatherapp.activities.DetailsActivity;
import ua.marinovskiy.weatherapp.entities.Weather;
import ua.marinovskiy.weatherapp.utils.ListUtil;

public class DetailsFragment extends Fragment {

    private TextView mTvCity;
    private TextView mTvTime;
    private TextView mTvDate;
    private TextView mTvTemperature;
    private TextView mTvDescription;
    private TextView mTvWindDeg;
    private TextView mTvWindSpeed;
    private TextView mTtvHumidity;
    private TextView mTvPressure;
    private ImageView mWeatherIcon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvCity = (TextView) view.findViewById(R.id.details_city);
        mTvTime = (TextView) view.findViewById(R.id.details_time);
        mTvDate = (TextView) view.findViewById(R.id.details_date);
        mTvTemperature = (TextView) view.findViewById(R.id.details_temperature_value);
        mTvDescription = (TextView) view.findViewById(R.id.details_description_value);
        mTvWindDeg = (TextView) view.findViewById(R.id.details_wind_deg_value);
        mTvWindSpeed = (TextView) view.findViewById(R.id.details_wind_speed_value);
        mTtvHumidity = (TextView) view.findViewById(R.id.details_humidity_value);
        mTvPressure = (TextView) view.findViewById(R.id.details_pressure_value);
        mWeatherIcon = (ImageView) view.findViewById(R.id.details_icon);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Context mContext = getActivity().getApplicationContext();
        
        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String mCity = mPreferences.getString("city", "");
        mTvCity.setText(mCity);

        if (getActivity() instanceof DetailsActivity) {
            int mPosition = getArguments().getInt("position");
            updateContent(mContext, mPosition);
        } else {
            updateContent(mContext, 0);
        }
    }

    void updateContent(Context context, int position) {
        Weather mWeather = MainFragment.sWeatherList.get(position);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String city = preferences.getString("mCity", "");
        String time = String.format(" %s", mWeather.getTime());
        String date = String.format(" %s", mWeather.getDate());
        String temperature = String.format(" %s", ListUtil.formatTemp(context, mWeather.getTemperature()));
        String description = String.format(" %s", mWeather.getDescription());
        String windDeg = String.format(" %s", mWeather.getWindDeg());
        String windSpeed = String.format(" %s %s", mWeather.getWindSpeed(), "m/s");
        String humidity = String.format(" %s", mWeather.getHumidity());
        String pressure = String.format(" %s", mWeather.getPressure());

        ListUtil.loadImg(context, mWeatherIcon, mWeather.getIcon());
        mTvCity.setText(city);
        mTvTime.setText(time);
        mTvDate.setText(date);
        mTvTemperature.setText(temperature);
        mTvDescription.setText(description);
        mTvWindDeg.setText(windDeg);
        mTvWindSpeed.setText(windSpeed);
        mTtvHumidity.setText(humidity);
        mTvPressure.setText(pressure);
    }
}
