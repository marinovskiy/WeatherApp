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

    private Context mContext;

    private TextView mTvDateTime;
    private TextView mTvTemperature;
    private TextView mTvDescription;
    private TextView mTvWindDeg;
    private TextView mTvWindSpeed;
    private TextView mTtvHumidity;
    private TextView mTvPressure;
    private ImageView mIvIcon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_details, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mIvIcon = (ImageView) view.findViewById(R.id.details_icon_weather_state);
        mTvDateTime = (TextView) view.findViewById(R.id.details_date_time_tv);
        mTvTemperature = (TextView) view.findViewById(R.id.details_temperature_tv);
        mTvDescription = (TextView) view.findViewById(R.id.details_description_tv);
        mTvWindDeg = (TextView) view.findViewById(R.id.details_wind_deg_tv);
        mTvWindSpeed = (TextView) view.findViewById(R.id.details_wind_speed_tv);
        mTtvHumidity = (TextView) view.findViewById(R.id.details_humidity_tv);
        mTvPressure = (TextView) view.findViewById(R.id.details_pressure_tv);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity().getApplicationContext();

        if (getActivity() instanceof DetailsActivity) {
            int mPosition = getArguments().getInt("position");
            updateContent(mContext, mPosition);
        } else {
            updateContent(mContext, 0);
        }
    }

    protected void updateContent(Context context, int position) {

        Weather mWeather = MainFragment.sRealmResults.get(position);

        String iconId = mWeather.getIcon();
        String dateTime = String.format("%s %s", mWeather.getTime(), mWeather.getDate());
        String temperature = String.format("%s", ListUtil.formatTemp(context,
                mWeather.getTemperature()));
        String description = String.format("%s", mWeather.getDescription());
        String windDeg = String.format("%s", mWeather.getWindDeg());
        String windSpeed = String.format("%s m/s", mWeather.getWindSpeed());
        String humidity = String.format("%s %%", mWeather.getHumidity());
        String pressure = String.format("%s hpa", mWeather.getPressure());

        ListUtil.loadImg(mContext, mIvIcon, iconId);
        mTvDateTime.setText(dateTime);
        mTvTemperature.setText(temperature);
        mTvDescription.setText(description);
        mTvWindDeg.setText(windDeg);
        mTvWindSpeed.setText(windSpeed);
        mTtvHumidity.setText(humidity);
        mTvPressure.setText(pressure);
    }
}
