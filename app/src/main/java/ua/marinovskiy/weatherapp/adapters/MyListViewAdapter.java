package ua.marinovskiy.weatherapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ua.marinovskiy.weatherapp.R;
import ua.marinovskiy.weatherapp.entities.Weather;
import ua.marinovskiy.weatherapp.managers.ListManager;

public class MyListViewAdapter extends BaseAdapter {

    Context mContext;
    List<Weather> mWeatherList;
    LayoutInflater mLayoutInflater;

    public MyListViewAdapter(Context context, List<Weather> weatherList) {
        mContext = context;
        mWeatherList = weatherList;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mWeatherList.size();
    }

    @Override
    public Object getItem(int position) {
        return mWeatherList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_view_item, parent, false);
        }

        Weather weather = (Weather) getItem(position);

        String mTime = ListManager.getTextTime(weather.getDateTime());
        String mDate = ListManager.getTextDate(weather.getDateTime());
        String mTemperature = ListManager.formatTemp(mContext, weather.getTemperature());
        String mCondition = weather.getCondition();

        TextView mTvTemperature = (TextView) convertView.findViewById(R.id.lv_temperature);
        TextView mTvCondition = (TextView) convertView.findViewById(R.id.lv_condition);
        TextView mTvDate = (TextView) convertView.findViewById(R.id.lv_date_of_weather);
        TextView mTvTime = (TextView) convertView.findViewById(R.id.lv_time_of_weather);
        ImageView mIvIcon = (ImageView) convertView.findViewById(R.id.lv_icon);

        mTvTemperature.setText(mTemperature);
        mTvCondition.setText(mCondition);
        mTvTime.setText(mTime);
        mTvDate.setText(mDate);
        ListManager.loadImg(mContext, mIvIcon, weather.getIcon());

        return convertView;
    }


}
