package ua.marinovskiy.weatherapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmResults;
import ua.marinovskiy.weatherapp.R;
import ua.marinovskiy.weatherapp.activities.DetailsActivity;
import ua.marinovskiy.weatherapp.adapters.RecyclerViewAdapter;
import ua.marinovskiy.weatherapp.entities.Weather;
import ua.marinovskiy.weatherapp.interfaces.OnItemClickListener;
import ua.marinovskiy.weatherapp.utils.ListUtil;
import ua.marinovskiy.weatherapp.utils.db.DatabaseWriter;
import ua.marinovskiy.weatherapp.utils.loaders.JSONLoader;

public class MainFragment extends Fragment {

    protected static List<Weather> sWeatherList = new ArrayList<>();

    private static Realm sRealm;
    private static Context sContext;
    private static RealmResults<Weather> sRealmResults;

    private RecyclerView mRecyclerView;
    private static RecyclerViewAdapter sRecyclerViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;

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

        sContext = getActivity().getApplicationContext();

        /** load data from JSON **/
        if (ListUtil.isConnected(sContext)) {
            loadOrRefresh();
        }
        /** get data from database **/
        sRealm = Realm.getInstance(sContext);
        sRealmResults = sRealm.where(Weather.class).findAll();

        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        sRecyclerViewAdapter = new RecyclerViewAdapter(sContext, sRealmResults);
        mRecyclerView.setAdapter(sRecyclerViewAdapter);

        sRecyclerViewAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                onItemSelected(position);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sRealm.close();
    }

    private void onItemSelected(int position) {
        DetailsFragment mDetailsFragment = (DetailsFragment) getFragmentManager().
                findFragmentById(R.id.fragment_weather_details);
        if ((mDetailsFragment == null) || (!mDetailsFragment.isInLayout())) {
            Intent intent = new Intent(sContext, DetailsActivity.class);
            intent.putExtra("position", position);
            startActivity(intent);
        } else {
            mDetailsFragment.updateContent(sContext, position);
        }
    }

    public static void loadOrRefresh() {

        SharedPreferences mPreferences = PreferenceManager.getDefaultSharedPreferences(sContext);
        String mCity = mPreferences.getString("city", "");
        String mUrlPart1 = sContext.getResources().getString(R.string.urlPart1);
        String mUrlPart2 = sContext.getResources().getString(R.string.urlPart2);
        String url = String.format("%s%s%s", mUrlPart1, mCity, mUrlPart2);

        /** delete old data **/
        sRealm = Realm.getInstance(sContext);
        sRealmResults = sRealm.where(Weather.class).findAll();
        if (!sRealmResults.isEmpty()) {
            sRealm.beginTransaction();
            sRealmResults.clear();
            sRealm.commitTransaction();
        }
        try {
            /** get new data from JSON **/
            sWeatherList = new JSONLoader().execute(url).get();
            if (!sWeatherList.isEmpty()) {
                /** write new data into database **/
                new DatabaseWriter(sContext, sWeatherList).execute();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

}