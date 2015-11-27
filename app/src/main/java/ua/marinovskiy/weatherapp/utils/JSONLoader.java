package ua.marinovskiy.weatherapp.utils;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import ua.marinovskiy.weatherapp.entities.Weather;

// load json from server with API
public class JSONLoader extends AsyncTask<String, Void, List<Weather>> {

    JSONUtil jsonUtil;
    List<Weather> weatherList;

    URL url;
    String resultJson = "";
    BufferedReader reader = null;
    HttpURLConnection urlConnection = null;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        jsonUtil = new JSONUtil();
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

        weatherList = JSONUtil.parse(resultJson);

        return weatherList;
    }
}

