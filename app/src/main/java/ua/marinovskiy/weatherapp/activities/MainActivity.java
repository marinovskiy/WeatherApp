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

import ua.marinovskiy.weatherapp.R;
import ua.marinovskiy.weatherapp.dialogs.FirstRunDialog;
import ua.marinovskiy.weatherapp.entities.Weather;
import ua.marinovskiy.weatherapp.fragments.MainFragment;
import ua.marinovskiy.weatherapp.services.UpdateService;

public class MainActivity extends AppCompatActivity
        implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences mFirstRun;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        setTitle();

        /** set toolbar's shadow if current version LOLLIPOP or above **/
        if (Build.VERSION.SDK_INT >= 21) {
            getSupportActionBar().setElevation(4);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFirstRun = getPreferences(MODE_PRIVATE);
        if (mFirstRun.getBoolean("no_city", true)) {
            FirstRunDialog first_run_dialog = new FirstRunDialog();
            first_run_dialog.setCancelable(false);
            first_run_dialog.show(getSupportFragmentManager(), "");
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
                Intent intentUpdate = new Intent(MainActivity.this, UpdateService.class);
                startService(intentUpdate);
                MainFragment.sRealmResults = MainFragment.sRealm.where(Weather.class).findAll();
                MainFragment.sRecyclerViewAdapter.notifyDataSetChanged();
                return true;
            case R.id.action_settings:
                Intent intentSettings = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intentSettings);
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
}
