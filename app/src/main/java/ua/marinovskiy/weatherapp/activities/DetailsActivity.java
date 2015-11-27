package ua.marinovskiy.weatherapp.activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import ua.marinovskiy.weatherapp.R;
import ua.marinovskiy.weatherapp.fragments.DetailsFragment;

public class DetailsActivity extends AppCompatActivity {

    private int mPosition;
    private Bundle mArgs;
    private DetailsFragment mDetailsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_details);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // remove toolbar's shadow if current version LOLLIPOP or above
        if (Build.VERSION.SDK_INT >= 21) {
            findViewById(R.id.view_toolbar_shadow).setVisibility(View.INVISIBLE);
        }

        mPosition = getIntent().getIntExtra("position", 0);
        mArgs = new Bundle();
        mArgs.putInt("position", mPosition);
        mDetailsFragment = new DetailsFragment();
        mDetailsFragment.setArguments(mArgs);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_weather_details, mDetailsFragment).commit();

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
