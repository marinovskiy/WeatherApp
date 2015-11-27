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

    private int mPosition;
    private Context mContext;
    private Weather mWeather;
    private SharedPreferences mPreferences;

    private String mCity;
    private String mTime;
    private String mDate;
    private String mTemperature;
    private String mDescription;
    private String mWindDeg;
    private String mWindSpeed;
    private String mHumidity;
    private String mPressure;

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

        mContext = getActivity().getApplicationContext();

        if (getActivity() instanceof DetailsActivity) {
            mPosition = getArguments().getInt("position");
            updateContent(mContext, mPosition);
        } else {
            updateContent(mContext, 0);
        }
    }

    void updateContent(Context context, int position) {
        mWeather = MainFragment.sWeatherList.get(position);

        mPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        mCity = mPreferences.getString("mCity", "");
        mTime = String.format(" %s", ListUtil.formatTime(mWeather.getDateTime()));
        mDate = String.format(" %s", ListUtil.formatDate(mWeather.getDateTime()));
        mTemperature = String.format(" %s", ListUtil.formatTemp(context, mWeather.getTemperature()));
        mDescription = String.format(" %s", mWeather.getDescription());
        mWindDeg = String.format(" %s", mWeather.getWindDeg());
        mWindSpeed = String.format(" %s %s", mWeather.getWindSpeed(), "m/s");
        mHumidity = String.format(" %s", mWeather.getHumidity());
        mPressure = String.format(" %s", mWeather.getPressure());

        ListUtil.loadImg(context, mWeatherIcon, mWeather.getIcon());
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
