package ua.marinovskiy.weatherapp.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        context = getActivity().getApplicationContext();

        if (!ListManager.checkInternetConnection(context)) {
            header.setVisibility(View.INVISIBLE);
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
        }

        try {
            myWeatherList = new JSONtask().
                    execute(context.getResources().getString(R.string.url)).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        listViewAdapter = new MyListViewAdapter(getActivity().getApplicationContext(), myWeatherList);
        setListAdapter(listViewAdapter);

        getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                onItemSelected(position);
            }
        });

    }

    private class JSONtask extends AsyncTask<String, Void, List<Weather>> {

        ProgressDialog progressDialog;
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

            /*progressDialog = new ProgressDialog(getActivity().getApplicationContext());
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.setMessage("Loading weather...");
            progressDialog.setCancelable(false);
            progressDialog.show();*/
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

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(List<Weather> weatherList) {
            super.onPostExecute(weatherList);

            /*if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }*/
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

}
