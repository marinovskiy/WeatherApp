package ua.marinovskiy.weatherapp.utils.json;

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

    private String mResultJson = "";

    @Override
    protected List<Weather> doInBackground(String... urls) {

        try {
            URL url = new URL(urls[0]);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            InputStream inputStream = httpURLConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }

            mResultJson = buffer.toString();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return JSONUtil.parse(mResultJson);
    }
}

