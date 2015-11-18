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

    Context context;
    List<Weather> weatherList;
    LayoutInflater layoutInflater;

    public MyListViewAdapter(Context context, List<Weather> weatherList) {
        this.context = context;
        this.weatherList = weatherList;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return weatherList.size();
    }

    @Override
    public Object getItem(int position) {
        return weatherList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_view_item, parent, false);
        }

        Weather weather = (Weather) getItem(position);

        String time = ListManager.getTextTime(weather.getDateTime());
        String date = ListManager.getTextDate(weather.getDateTime());
        String temperature = ListManager.formatTemp(context, weather.getTemperature());
        String condition = weather.getCondition();

        TextView tv_temperature = (TextView) convertView.findViewById(R.id.lv_temperature);
        TextView tv_condition = (TextView) convertView.findViewById(R.id.lv_condition);
        TextView tv_date = (TextView) convertView.findViewById(R.id.lv_date_of_weather);
        TextView tv_time = (TextView) convertView.findViewById(R.id.lv_time_of_weather);
        ImageView imageView = (ImageView) convertView.findViewById(R.id.lv_icon);

        tv_temperature.setText(temperature);
        tv_condition.setText(condition);
        tv_time.setText(time);
        tv_date.setText(date);
        ListManager.loadImg(context, imageView, weather.getIcon());

        return convertView;
    }


}
