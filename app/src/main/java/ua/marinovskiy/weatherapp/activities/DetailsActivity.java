package ua.marinovskiy.weatherapp.activities;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import ua.marinovskiy.weatherapp.R;
import ua.marinovskiy.weatherapp.fragments.DetailsFragment;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_details);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            SharedPreferences mPrefSettings = PreferenceManager.getDefaultSharedPreferences(this);
            String city = mPrefSettings.getString("city", "");
            String weatherIn = getResources().getString(R.string.toolbar_titile_template);
            getSupportActionBar().setTitle(String.format("%s %s", weatherIn, city));
        }

        // remove toolbar's shadow if current version LOLLIPOP or above
        if (Build.VERSION.SDK_INT >= 21) {
            findViewById(R.id.view_toolbar_shadow).setVisibility(View.INVISIBLE);
            getSupportActionBar().setElevation(4);
        }

        int mPosition = getIntent().getIntExtra("position", 0);
        Bundle mArgs = new Bundle();
        mArgs.putInt("position", mPosition);
        DetailsFragment mDetailsFragment = new DetailsFragment();
        mDetailsFragment.setArguments(mArgs);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_weather_details,
                mDetailsFragment).commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
