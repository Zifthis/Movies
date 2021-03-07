package com.example.movies;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.SearchView;
import android.widget.TextView;
import com.example.movies.model.Result;
import com.example.movies.ui.popular.PopularFragment;
import com.example.movies.ui.toprated.TopRatedFragment;
import com.example.movies.ui.discover.DiscoverFragment;
import com.example.movies.ui.upcoming.UpcomingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;


public class HomeActivity extends AppCompatActivity {

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Top Rated Movies");
        setSupportActionBar(toolbar);
        getWindow().setStatusBarColor(getResources().getColor(R.color.color_background));

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new UpcomingFragment()).commit();

    }

    //search
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        MenuItem.OnActionExpandListener onActionExpandListener = new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                return true;
            }
        };
        MenuItem searchItem = menu.findItem(R.id.search).setOnActionExpandListener(onActionExpandListener);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setQueryHint("Search Movie Title...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (MyApp.getInstance().getPopAdapter() != null) {
                    MyApp.getInstance().getPopAdapter().getFilter().filter(query);
                }
                if (MyApp.getInstance().getDiscAdapter() != null) {
                    MyApp.getInstance().getDiscAdapter().getFilter().filter(query);
                }
                if (MyApp.getInstance().getTopAdapter() != null) {
                    MyApp.getInstance().getTopAdapter().getFilter().filter(query);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //called when you type each letter in search
                if (MyApp.getInstance().getPopAdapter() != null) {
                    MyApp.getInstance().getPopAdapter().getFilter().filter(newText);
                }
                if (MyApp.getInstance().getDiscAdapter() != null) {
                    MyApp.getInstance().getDiscAdapter().getFilter().filter(newText);
                }
                if (MyApp.getInstance().getTopAdapter() != null) {
                    MyApp.getInstance().getTopAdapter().getFilter().filter(newText);
                }
                return false;
            }
        });

        return true;
    }

    //nav bar
    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {

        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_upcoming:
                selectedFragment = new UpcomingFragment();
                Vibrator upcomingV = (Vibrator) HomeActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                upcomingV.vibrate(50);
                toolbar.setTitle("Upcoming Movies");
                break;
            case R.id.navigation_toprated:
                selectedFragment = new TopRatedFragment();
                Vibrator topV = (Vibrator) HomeActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                topV.vibrate(50);
                toolbar.setTitle("Top Rated Movies");
                break;
            case R.id.navigation_popular:
                selectedFragment = new PopularFragment();
                Vibrator popularV = (Vibrator) HomeActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                popularV.vibrate(50);
                toolbar.setTitle("Popular Movies");
                break;
            case R.id.navigation_discover:
                selectedFragment = new DiscoverFragment();
                Vibrator discoverV = (Vibrator) HomeActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                discoverV.vibrate(50);
                toolbar.setTitle("Discover Movies");
                break;
        }
        assert selectedFragment != null;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        return true;
    };

}