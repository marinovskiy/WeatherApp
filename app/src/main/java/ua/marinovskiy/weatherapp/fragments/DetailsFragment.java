package ua.marinovskiy.weatherapp.fragments;

import android.content.Context;
import android.os.Bundle;
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
import ua.marinovskiy.weatherapp.managers.ListManager;

public class DetailsFragment extends Fragment {

    int position;
    Context context;
    Weather weather;
    ImageView iv_weather_icon;
    TextView tv_city, tv_time, tv_date, tv_temperature, tv_description, tv_wind_deg, tv_wind_speed, tv_humidity, tv_pressure;
    String city, time, date, temperature, description, wind_deg, wind_speed, humidity, pressure;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iv_weather_icon = (ImageView) view.findViewById(R.id.details_icon);
        tv_city = (TextView) view.findViewById(R.id.details_city);
        tv_time = (TextView) view.findViewById(R.id.details_time);
        tv_date = (TextView) view.findViewById(R.id.details_date);
        tv_temperature = (TextView) view.findViewById(R.id.details_temperature_value);
        tv_description = (TextView) view.findViewById(R.id.details_description_value);
        tv_wind_deg = (TextView) view.findViewById(R.id.details_wind_deg_value);
        tv_wind_speed = (TextView) view.findViewById(R.id.details_wind_speed_value);
        tv_humidity = (TextView) view.findViewById(R.id.details_humidity_value);
        tv_pressure = (TextView) view.findViewById(R.id.details_pressure_value);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity().getApplicationContext();

        if (getActivity() instanceof DetailsActivity) {
            position = getArguments().getInt("position");
            updateContent(position);
        }
    }

    void updateContent(int position) {
        weather = MainFragment.myWeatherList.get(position);

        time = ListManager.getTextTime(weather.getDateTime());
        date = ListManager.getTextDate(weather.getDateTime());
        temperature = ListManager.formatTemp(context, weather.getTemperature());
        description = weather.getDescription();
        wind_deg = weather.getWindDeg();
        wind_speed = weather.getWindSpeed();
        humidity = weather.getHumidity();
        pressure = weather.getPressure();

        ListManager.loadImg(context, iv_weather_icon, weather.getIcon());
        tv_time.setText(time);
        tv_date.setText(date);
        tv_temperature.setText(temperature);
        tv_description.setText(description);
        tv_wind_deg.setText(wind_deg);
        tv_wind_speed.setText(wind_speed);
        tv_humidity.setText(humidity);
        tv_pressure.setText(pressure);
    }
}
