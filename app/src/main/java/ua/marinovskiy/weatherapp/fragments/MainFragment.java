package ua.marinovskiy.weatherapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;
import ua.marinovskiy.weatherapp.R;
import ua.marinovskiy.weatherapp.activities.DetailsActivity;
import ua.marinovskiy.weatherapp.adapters.RecyclerViewAdapter;
import ua.marinovskiy.weatherapp.entities.Weather;
import ua.marinovskiy.weatherapp.interfaces.OnItemClickListener;
import ua.marinovskiy.weatherapp.utils.DataUtil;

public class MainFragment extends Fragment
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private Context mContext;
    private Realm mRealm;
    private RealmResults<Weather> mRealmResults;
    private RecyclerViewAdapter mRecyclerViewAdapter;
    private RecyclerView mRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mContext = getActivity().getApplicationContext();

        SharedPreferences mPrefSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
        mPrefSettings.registerOnSharedPreferenceChangeListener(this);

        mRecyclerView.setHasFixedSize(false);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        mRealm = Realm.getDefaultInstance();
        mRealmResults = mRealm.where(Weather.class).findAll();

        mRecyclerViewAdapter = new RecyclerViewAdapter(mContext, mRealmResults);
        mRecyclerView.setAdapter(mRecyclerViewAdapter);
        mRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onItemSelected(position);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mRealm.close();
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("city")) {
            DataUtil.uploadData(mContext);
        }
        updateData();
    }

    public void updateData() {
        mRealmResults = mRealm.where(Weather.class).findAll();
        mRecyclerViewAdapter.notifyDataSetChanged();
    }

    private void onItemSelected(int position) {
        DetailsFragment mDetailsFragment = (DetailsFragment) getFragmentManager().
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