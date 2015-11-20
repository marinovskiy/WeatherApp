package ua.marinovskiy.weatherapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.ExecutionException;

import ua.marinovskiy.weatherapp.R;
import ua.marinovskiy.weatherapp.activities.DetailsActivity;
import ua.marinovskiy.weatherapp.adapters.MyListViewAdapter;
import ua.marinovskiy.weatherapp.entities.Weather;
import ua.marinovskiy.weatherapp.managers.JSONManager;
import ua.marinovskiy.weatherapp.managers.ListManager;

public class MainFragment extends ListFragment {

    TextView header;
    Context context;
    String url_part_1, url_part_2, city;
    SharedPreferences preferences;
    MyListViewAdapter listViewAdapter;
    public static List<Weather> myWeatherList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        header = (TextView) view.findViewById(R.id.header);
        header.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity().getApplicationContext();
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        city = preferences.getString("city", "");

        if (!ListManager.checkInternetConnection(context)) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        } else if (!city.equals("")) {
            url_part_1 = context.getResources().getString(R.string.url_part_1);
            url_part_2 = context.getResources().getString(R.string.url_part_2);
            try {
                myWeatherList = new JSONtask().
                        execute(String.format("%s%s%s", url_part_1, city, url_part_2)).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            header.setVisibility(View.VISIBLE);
            header.setText(String.format("Weather in %s", city));

            listViewAdapter = new MyListViewAdapter(getActivity().getApplicationContext(), myWeatherList);
            setListAdapter(listViewAdapter);

            getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    onItemSelected(position);
                }
            });
        }
    }

    void onItemSelected(int position) {
        DetailsFragment detailsFragment = (DetailsFragment) getFragmentManager().
                findFragmentById(R.id.fragment_weather_details);
        if (detailsFragment == null) {
            Intent intent = new Intent(context, DetailsActivity.class);
            intent.putExtra("position", position);
            startActivity(intent);
        } else {
            detailsFragment.updateContent(position);
        }
    }

    private class JSONtask extends AsyncTask<String, Void, List<Weather>> {

        List<Weather> weatherList;
        JSONManager jsonManager;

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String resultJson = "";
        URL url;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            jsonManager = new JSONManager();
        }

        @Override
        protected List<Weather> doInBackground(String... urls) {

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }

                resultJson = buffer.toString();
                inputStream.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            weatherList = JSONManager.parse(resultJson);

            return weatherList;
        }

    }

}
