package ua.marinovskiy.weatherapp.managers;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import ua.marinovskiy.weatherapp.activities.DetailsActivity;
import ua.marinovskiy.weatherapp.fragments.DetailsFragment;

public class ListManager {

    public static boolean checkInternetConnection(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public static Date getWeatherDate(String textDate) {
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            date = dateFormat.parse(textDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getTextTime(String txtDate) {
        String time;
        DateFormat dateFormat = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        time = dateFormat.format(getWeatherDate(txtDate));
        return time;
    }

    public static String getTextDate(String txtDate) {
        String date;
        DateFormat dateFormat = new SimpleDateFormat("d MMM, EEE", Locale.ENGLISH);
        date = dateFormat.format(getWeatherDate(txtDate));
        return date;
    }

    public static String formatTemp(Context context, double temperature) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String measure = preferences.getString("temp_measure", "k");
        String temp;
        switch (measure) {
            case "k":
                temp = String.format("%.2f K", temperature);
                break;
            case "c":
                double celsius_temp = temperature - 273;
                temp = String.format("%.2f° C", celsius_temp);
                break;
            case "f":
                double fahrenheit_temp = 1.8 * (temperature - 273) + 32;
                temp = String.format("%.2f° F", fahrenheit_temp);
                break;
            default:
                temp = String.format("%.2f K", temperature);
                break;
        }

        return temp;
    }

    public static void loadImg(Context context, ImageView imageView, String iconId) {
        String iconUrl = String.format("http://openweathermap.org/img/w/%s.png", iconId);
        Picasso.with(context)
                .load(iconUrl)
                .into(imageView);
    }

}
