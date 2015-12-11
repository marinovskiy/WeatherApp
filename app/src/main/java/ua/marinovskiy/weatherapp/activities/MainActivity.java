package ua.marinovskiy.weatherapp.activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import ua.marinovskiy.weatherapp.R;
import ua.marinovskiy.weatherapp.dialogs.DialogFirstRun;
import ua.marinovskiy.weatherapp.services.ReminderService;
import ua.marinovskiy.weatherapp.utils.DataUtil;
import ua.marinovskiy.weatherapp.utils.ListUtil;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences mFirstRun;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        setTitle();

        /** remove toolbar's shadow if current version LOLLIPOP or above **/
        if (Build.VERSION.SDK_INT >= 21) {
            findViewById(R.id.view_toolbar_shadow).setVisibility(View.INVISIBLE);
            getSupportActionBar().setElevation(4);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirstRun = getPreferences(MODE_PRIVATE);
        if (mFirstRun.getBoolean("no_city", true)) {
            DialogFirstRun first_run_dialog = new DialogFirstRun();
            first_run_dialog.setCancelable(false);
            first_run_dialog.show(getSupportFragmentManager(), "");
        } else {
            if (ListUtil.isConnected(mContext))
                startUpdateService();
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (!key.equals(""))
            mFirstRun.edit().putBoolean("no_city", false).apply();
        if (key.equals("city"))
            setTitle();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                /** load data from JSON **/
                if (ListUtil.isConnected(mContext)) {
                    DataUtil.uploadData(mContext);
                } else {
                    Toast.makeText(mContext, "No internet connection", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void setTitle() {
        SharedPreferences mPrefSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mPrefSettings.registerOnSharedPreferenceChangeListener(this);
        String city = mPrefSettings.getString("city", "");
        String weatherIn = getResources().getString(R.string.toolbar_titile_template);
        getSupportActionBar().setTitle(String.format("%s %s", weatherIn, city));
    }

    private void startUpdateService() {
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
        long time = System.currentTimeMillis();
        Intent intent = new Intent(this, ReminderService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC, time, 60000, pendingIntent);
    }
}
