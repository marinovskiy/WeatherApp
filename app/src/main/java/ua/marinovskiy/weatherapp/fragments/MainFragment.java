package ua.marinovskiy.weatherapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import io.realm.Realm;
import io.realm.RealmResults;
import ua.marinovskiy.weatherapp.R;
import ua.marinovskiy.weatherapp.activities.DetailsActivity;
import ua.marinovskiy.weatherapp.adapters.ListViewAdapter;
import ua.marinovskiy.weatherapp.entities.Weather;
import ua.marinovskiy.weatherapp.utils.ListUtil;
import ua.marinovskiy.weatherapp.utils.database.DatabaseWriter;
import ua.marinovskiy.weatherapp.utils.json.JSONLoader;

public class MainFragment extends ListFragment {

    protected static List<Weather> sWeatherList = new ArrayList<>();

    private static Realm sRealm;
    private static Context sContext;
    private static RealmResults<Weather> sRealmResults;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        sContext = getActivity().getApplicationContext();

        sRealm = Realm.getInstance(sContext);
        sRealmResults = sRealm.where(Weather.class).findAll();

        loadOrRefresh();

        ListViewAdapter mListViewAdapter = new ListViewAdapter(sContext, sRealmResults, true);
        setListAdapter(mListViewAdapter);
        mListViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sRealm.close();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        onItemSelected(position);
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
        String mUrlPart1 = sContext.getResources().getString(R.string.url_part_1);
        String mUrlPart2 = sContext.getResources().getString(R.string.url_part_2);
        String url = String.format("%s%s%s", mUrlPart1, mCity, mUrlPart2);

        if (!ListUtil.isConnected(sContext)) {
            Toast.makeText(sContext, "No internet connection", Toast.LENGTH_SHORT).show();
        } else {
            try {
                /** GET NEW DATA FROM JSON **/
                sWeatherList = new JSONLoader().execute(url).get();
                if (!sWeatherList.isEmpty()) {
                    /** WRITE NEW DATA INTO DATABASE **/
                    new DatabaseWriter(sContext, sWeatherList).execute();
                } else {
                    /** DELETE OLD DATA IF CURRENT NOT EXIST **/
                    sRealm.beginTransaction();
                    sRealmResults.clear();
                    sRealm.commitTransaction();
                }

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

}