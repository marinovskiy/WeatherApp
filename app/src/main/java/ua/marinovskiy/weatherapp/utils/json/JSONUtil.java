package ua.marinovskiy.weatherapp.utils.json;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ua.marinovskiy.weatherapp.entities.Weather;
import ua.marinovskiy.weatherapp.utils.ListUtil;

// make list from json
public class JSONUtil {

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
                weather.setTime(ListUtil.formatTime(listJsonArray.getJSONObject(i).getString("dt_txt")));
                weather.setDate(ListUtil.formatDate(listJsonArray.getJSONObject(i).getString("dt_txt")));
                weather.setIcon(listJsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("icon"));
                weather.setTemperature(listJsonArray.getJSONObject(i).getJSONObject("main").getDouble("temp"));
                weather.setCondition(listJsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("main"));
                weather.setDescription(listJsonArray.getJSONObject(i).getJSONArray("weather").getJSONObject(0).getString("description"));
                weather.setWindDeg(ListUtil.getWindDeg(listJsonArray.getJSONObject(i).getJSONObject("wind").getDouble("deg")));
                weather.setWindSpeed(listJsonArray.getJSONObject(i).getJSONObject("wind").getString("speed"));
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
