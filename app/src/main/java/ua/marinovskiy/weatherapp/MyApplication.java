package ua.marinovskiy.weatherapp;

import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;

public class MyApplication extends Application {

    public static String sFontPathLight;
    public static Typeface sLightTypeface;

    @Override
    public void onCreate() {
        super.onCreate();
        Context mContext = getApplicationContext();
        sFontPathLight = mContext.getResources().getString(R.string.font_roboto_light);
        sLightTypeface = Typeface.createFromAsset(mContext.getAssets(), sFontPathLight);
    }

}
