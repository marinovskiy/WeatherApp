package ua.marinovskiy.weatherapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.ExecutionException;

import ua.marinovskiy.weatherapp.R;
import ua.marinovskiy.weatherapp.activities.DetailsActivity;
import ua.marinovskiy.weatherapp.adapters.RecyclerViewAdapter;
import ua.marinovskiy.weatherapp.entities.Weather;
import ua.marinovskiy.weatherapp.rv_helper.RecyclerViewClickListener;
import ua.marinovskiy.weatherapp.rv_helper.SimpleDividerItemDecoration;
import ua.marinovskiy.weatherapp.utils.JSONLoader;
import ua.marinovskiy.weatherapp.utils.ListUtil;

public class MainFragment extends Fragment {

    protected static List<Weather> sWeatherList;

    private String mCity;
    private String mUrlPart1;
    private String mUrlPart2;
    private Context mContext;
    private SharedPreferences mPreferences;
    private DetailsFragment mDetailsFragment;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mRecyclerViewAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mContext = getActivity().getApplicationContext();
        mPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        mCity = mPreferences.getString("city", "");

        if (!ListUtil.checkInternetConnection(mContext)) {
            Toast.makeText(mContext, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (!mCity.equals("")) {
            mUrlPart1 = mContext.getResources().getString(R.string.url_part_1);
            mUrlPart2 = mContext.getResources().getString(R.string.url_part_2);
            try {
                sWeatherList = new JSONLoader().
                        execute(String.format("%s%s%s", mUrlPart1, mCity, mUrlPart2)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            mRecyclerView.setHasFixedSize(true);
            mRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(mContext));

            mLayoutManager = new LinearLayoutManager(getActivity());
            mRecyclerView.setLayoutManager(mLayoutManager);

            mRecyclerViewAdapter = new RecyclerViewAdapter(mContext, sWeatherList);
            mRecyclerView.setAdapter(mRecyclerViewAdapter);

            mRecyclerView.addOnItemTouchListener(
                    new RecyclerViewClickListener(mContext, new RecyclerViewClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            onItemSelected(position);
                        }
                    })
            );
        }
    }

    void onItemSelected(int position) {
        mDetailsFragment = (DetailsFragment) getFragmentManager().
                findFragmentById(R.id.fragment_weather_details);
        if ((mDetailsFragment == null) || (!mDetailsFragment.isInLayout())) {
            Intent intent = new Intent(mContext, DetailsActivity.class);
            intent.putExtra("position", position);
            startActivity(intent);
        } else {
            mDetailsFragment.updateContent(mContext, position);
        }
    }

}
