package ua.marinovskiy.weatherapp.activities;

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

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;
import ua.marinovskiy.weatherapp.R;
import ua.marinovskiy.weatherapp.dialogs.DialogFirstRun;
import ua.marinovskiy.weatherapp.fragments.MainFragment;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences mFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        // remove toolbar's shadow if current version LOLLIPOP or above
        if (Build.VERSION.SDK_INT >= 21) {
            findViewById(R.id.view_toolbar_shadow).setVisibility(View.INVISIBLE);
        }

        SharedPreferences mPrefSettings = PreferenceManager.getDefaultSharedPreferences(this);
        mPrefSettings.registerOnSharedPreferenceChangeListener(this);
        String mCity = mPrefSettings.getString("city", "");
        toolbar.setTitle(mCity);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirstRun = getPreferences(MODE_PRIVATE);
        if (mFirstRun.getBoolean("no_city", true)) {
            DialogFirstRun first_run_dialog = new DialogFirstRun();
            first_run_dialog.setCancelable(false);
            first_run_dialog.show(getSupportFragmentManager(), "");
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        recreate();
        if (!key.equals("")) {
            mFirstRun.edit().putBoolean("no_city", false).apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_refresh:
                MainFragment.loadOrRefresh();
                return true;
            case R.id.action_settings:
                Intent intent = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
