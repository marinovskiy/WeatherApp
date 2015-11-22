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
import ua.marinovskiy.weatherapp.managers.ListManager;

public class DetailsFragment extends Fragment {

    int mPosition;
    Context mContext;
    Weather mWeather;
    ImageView mWeatherIcon;
    SharedPreferences mPreferences;

    String mCity;
    String mTime;
    String mDate;
    String mTemperature;
    String mDescription;
    String mWindDeg;
    String mWindSpeed;
    String mHumidity;
    String mPressure;

    TextView mTvCity;
    TextView mTvTime;
    TextView mTvDate;
    TextView mTvTemperature;
    TextView mTvDescription;
    TextView mTvWindDeg;
    TextView mTvWindSpeed;
    TextView mTtvHumidity;
    TextView mTvPressure;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mWeatherIcon = (ImageView) view.findViewById(R.id.details_icon);
        mTvCity = (TextView) view.findViewById(R.id.details_city);
        mTvTime = (TextView) view.findViewById(R.id.details_time);
        mTvDate = (TextView) view.findViewById(R.id.details_date);
        mTvTemperature = (TextView) view.findViewById(R.id.details_temperature_value);
        mTvDescription = (TextView) view.findViewById(R.id.details_description_value);
        mTvWindDeg = (TextView) view.findViewById(R.id.details_wind_deg_value);
        mTvWindSpeed = (TextView) view.findViewById(R.id.details_wind_speed_value);
        mTtvHumidity = (TextView) view.findViewById(R.id.details_humidity_value);
        mTvPressure = (TextView) view.findViewById(R.id.details_pressure_value);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity().getApplicationContext();

        if (getActivity() instanceof DetailsActivity) {
            mPosition = getArguments().getInt("position");
            updateContent(mContext, mPosition);
        } else {
            updateContent(mContext, 0);
        }
    }

    void updateContent(Context context, int position) {
        mWeather = MainFragment.myWeatherList.get(position);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mCity = mPreferences.getString("mCity", "");
        mTime = String.format(" %s", ListManager.getTextTime(mWeather.getDateTime()));
        mDate = String.format(" %s", ListManager.getTextDate(mWeather.getDateTime()));
        mTemperature = String.format(" %s", ListManager.formatTemp(context, mWeather.getTemperature()));
        mDescription = String.format(" %s", mWeather.getDescription());
        mWindDeg = String.format(" %s", mWeather.getWindDeg());
        mWindSpeed = String.format(" %s %s", mWeather.getWindSpeed(), "m/s");
        mHumidity = String.format(" %s", mWeather.getHumidity());
        mPressure = String.format(" %s", mWeather.getPressure());

        ListManager.loadImg(context, mWeatherIcon, mWeather.getIcon());
        mTvCity.setText(mCity);
        mTvTime.setText(mTime);
        mTvDate.setText(mDate);
        mTvTemperature.setText(mTemperature);
        mTvDescription.setText(mDescription);
        mTvWindDeg.setText(mWindDeg);
        mTvWindSpeed.setText(mWindSpeed);
        mTtvHumidity.setText(mHumidity);
        mTvPressure.setText(mPressure);
    }
}
