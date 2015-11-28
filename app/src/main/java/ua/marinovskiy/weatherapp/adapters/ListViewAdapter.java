package ua.marinovskiy.weatherapp.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import ua.marinovskiy.weatherapp.R;
import ua.marinovskiy.weatherapp.entities.Weather;
import ua.marinovskiy.weatherapp.utils.ListUtil;

public class ListViewAdapter extends RealmBaseAdapter<Weather> implements ListAdapter {

    private Context mContext;

    public ListViewAdapter(Context context, RealmResults<Weather> realmResults,
                           boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
        mContext = context;
    }

    private static class ViewHolder {
        TextView tvTime;
        TextView tvDate;
        TextView tvTemperature;
        TextView tvCondition;
        ImageView ivIcon;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.weather_list_item, parent, false);

            holder = new ViewHolder();
            holder.tvTime = (TextView) convertView.findViewById(R.id.list_time_of_weather);
            holder.tvDate = (TextView) convertView.findViewById(R.id.list_date_of_weather);
            holder.tvTemperature = (TextView) convertView.findViewById(R.id.list_temperature);
            holder.tvCondition = (TextView) convertView.findViewById(R.id.list_condition);
            holder.ivIcon = (ImageView) convertView.findViewById(R.id.list_icon);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Weather weather = realmResults.get(position);

        String time = weather.getTime();
        String date = weather.getDate();
        String condition = weather.getCondition();
        String temp = ListUtil.formatTemp(mContext, weather.getTemperature());

        holder.tvTime.setText(time);
        holder.tvDate.setText(date);
        holder.tvTemperature.setText(temp);
        holder.tvCondition.setText(condition);
        ListUtil.loadImg(mContext, holder.ivIcon, weather.getIcon());

        return convertView;
    }
}
