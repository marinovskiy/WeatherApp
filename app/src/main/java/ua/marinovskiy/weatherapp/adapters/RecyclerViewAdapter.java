package ua.marinovskiy.weatherapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ua.marinovskiy.weatherapp.R;
import ua.marinovskiy.weatherapp.entities.Weather;
import ua.marinovskiy.weatherapp.utils.ListUtil;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private List<Weather> mWeatherList;

    public RecyclerViewAdapter(Context context, List<Weather> weatherList) {
        mContext = context;
        mWeatherList = weatherList;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTime;
        public TextView tvDate;
        public TextView tvTemperature;
        public TextView tvCondition;
        public ImageView ivIcon;

        public ViewHolder(View v) {
            super(v);
            tvTime = (TextView) v.findViewById(R.id.list_time_of_weather);
            tvDate = (TextView) v.findViewById(R.id.list_date_of_weather);
            tvTemperature = (TextView) v.findViewById(R.id.list_temperature);
            tvCondition = (TextView) v.findViewById(R.id.list_condition);
            ivIcon = (ImageView) v.findViewById(R.id.list_icon);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Weather weather = mWeatherList.get(position);

        String time = ListUtil.formatTime(weather.getDateTime());
        String date = ListUtil.formatDate(weather.getDateTime());
        String temp = ListUtil.formatTemp(mContext, weather.getTemperature());
        String condition = weather.getCondition();

        holder.tvTime.setText(time);
        holder.tvDate.setText(date);
        holder.tvTemperature.setText(temp);
        holder.tvCondition.setText(condition);
        ListUtil.loadImg(mContext, holder.ivIcon, weather.getIcon());
    }

    @Override
    public int getItemCount() {
        return mWeatherList.size();
    }

}
