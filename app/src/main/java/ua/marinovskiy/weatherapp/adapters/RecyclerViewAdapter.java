package ua.marinovskiy.weatherapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.realm.RealmResults;
import ua.marinovskiy.weatherapp.MyApplication;
import ua.marinovskiy.weatherapp.R;
import ua.marinovskiy.weatherapp.entities.Weather;
import ua.marinovskiy.weatherapp.interfaces.OnItemClickListener;
import ua.marinovskiy.weatherapp.utils.ListUtil;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private Context mContext;
    private RealmResults<Weather> mWeatherRealmResults;
    private OnItemClickListener mOnItemClickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTvTime;
        private TextView mTvDate;
        private TextView mTvTemp;
        private TextView mTvDescription;
        private ImageView mImvIcon;

        public ViewHolder(View itemView) {
            super(itemView);

            mTvTime = (TextView) itemView.findViewById(R.id.list_time_tv);
            mTvDate = (TextView) itemView.findViewById(R.id.list_date_tv);
            mTvTemp = (TextView) itemView.findViewById(R.id.list_temp_tv);
            mTvDescription = (TextView) itemView.findViewById(R.id.list_description_tv);
            mImvIcon = (ImageView) itemView.findViewById(R.id.list_icon);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public RecyclerViewAdapter(Context context, RealmResults<Weather> weatherRealmResults) {
        mContext = context;
        mWeatherRealmResults = weatherRealmResults;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_list_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String time = mWeatherRealmResults.get(position).getTime();
        String date = mWeatherRealmResults.get(position).getDate();
        String temp = ListUtil.formatTemp(mContext, mWeatherRealmResults.get(position).getTemperature());
        String description = mWeatherRealmResults.get(position).getDescription();
        String iconId = mWeatherRealmResults.get(position).getIcon();

        holder.mTvTime.setText(time);
        holder.mTvDate.setText(date);
        holder.mTvTemp.setText(String.valueOf(temp));
        holder.mTvDescription.setText(description);

        holder.mTvTime.setTypeface(MyApplication.sLightTypeface);
        holder.mTvDate.setTypeface(MyApplication.sLightTypeface);
        holder.mTvTemp.setTypeface(MyApplication.sLightTypeface);
        holder.mTvDescription.setTypeface(MyApplication.sLightTypeface);

        ListUtil.loadImg(mContext, holder.mImvIcon, iconId);
    }

    @Override
    public int getItemCount() {
        return mWeatherRealmResults.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

}
