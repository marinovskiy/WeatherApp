package ua.marinovskiy.weatherapp.managers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ua.marinovskiy.weatherapp.entities.Weather;

public class JSONManager {

    public static List<Weather> parse(String json) {
        List<Weather> weatherList = new ArrayList<>();
        JSONObject mainJsonObject;
        JSONArray listJsonArray;

        try {
            weatherList = new ArrayList<>();
            mainJsonObject = new JSONObject(json);
            listJsonArray = mainJsonObject.getJSONArray("list");
            for (int i = 0; i < listJsonArray.length(); i++) {
                Weather weather = new Weather();
                weather.setDateTime(listJsonArray.getJSONObject(i).getString("dt_txt"));
                weather.setIcon(listJsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon"));
                weather.setTemperature(listJsonArray.getJSONObject(i).getJSONObject("main").getDouble("temp"));
                weather.setCondition(listJsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main"));
                weather.setDescription(listJsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description"));
                weather.setWindDeg(listJsonArray.getJSONObject(i).getJSONObject("wind").getString("speed"));
                weather.setWindSpeed(listJsonArray.getJSONObject(i).getJSONObject("wind").getString("deg"));
                weather.setHumidity(listJsonArray.getJSONObject(i).getJSONObject("main").getString("humidity"));
                weather.setPressure(listJsonArray.getJSONObject(i).getJSONObject("main").getString("pressure"));
                weatherList.add(weather);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weatherList;
    }

}
