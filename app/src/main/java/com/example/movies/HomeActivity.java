package com.example.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.movies.model.Result;
import com.example.movies.model.Upcoming;
import com.example.movies.rest.APIClient;
import com.example.movies.rest.UpcomingEndPoint;
import com.example.movies.adapter.SliderAdapter;
import com.example.movies.ui.popular.PopularFragment;
import com.example.movies.ui.toprated.TopRatedFragment;
import com.example.movies.ui.discover.DiscoverFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ViewPager2 viewPagerHolder;
    private final Handler slideHandler = new Handler();
    private TextView textView;
    private List<Result> discoverListResult = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Movie App");

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TopRatedFragment()).commit();


        textView = findViewById(R.id.nav_location_txt);


        viewPagerHolder = findViewById(R.id.viewpagerSlider);

        setupSlider();

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

    //slider
    private void setupSlider() {
        UpcomingEndPoint upcomingEndPoint = APIClient.getClient().create(UpcomingEndPoint.class);
        Call<Upcoming> call = upcomingEndPoint.getUpcoming(this.getString(R.string.api_key));
        call.enqueue(new Callback<Upcoming>() {
            @Override
            public void onResponse(@NotNull Call<Upcoming> call, @NotNull Response<Upcoming> response) {

                Upcoming upcoming = response.body();

                if (upcoming != null && upcoming.getResults() != null) {
                    discoverListResult = upcoming.getResults();
                    viewPagerHolder.setAdapter(new SliderAdapter(HomeActivity.this, discoverListResult, viewPagerHolder));
                    viewPagerHolder.setClipToPadding(false);
                    viewPagerHolder.setClipChildren(false);
                    viewPagerHolder.setOffscreenPageLimit(3);
                    viewPagerHolder.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);


                    CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
                    compositePageTransformer.addTransformer(new MarginPageTransformer(40));
                    compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
                        @Override
                        public void transformPage(@NonNull View page, float position) {
                            float r = 1 - Math.abs(position);
                            page.setScaleY(0.85f + r * 0.15f);
                        }
                    });
                    viewPagerHolder.setPageTransformer(compositePageTransformer);


                    viewPagerHolder.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                        @Override
                        public void onPageSelected(int position) {
                            super.onPageSelected(position);
                            slideHandler.removeCallbacks(sliderRunnable);
                            slideHandler.postDelayed(sliderRunnable, 6000);
                        }
                    });
                }
            }

            @Override
            public void onFailure(@NotNull Call<Upcoming> call, @NotNull Throwable t) {
                t.printStackTrace();
            }
        });

    }


    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPagerHolder.setCurrentItem(viewPagerHolder.getCurrentItem() + 1);
        }
    };

    //nav bar
    @SuppressLint("NonConstantResourceId")
    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {

        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_toprated:
                selectedFragment = new TopRatedFragment();
                textView.setText(R.string.top_nav);
                Vibrator topV = (Vibrator) HomeActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                topV.vibrate(50);
                break;
            case R.id.navigation_popular:
                selectedFragment = new PopularFragment();
                textView.setText(R.string.pop_nav);
                Vibrator popularV = (Vibrator) HomeActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                popularV.vibrate(50);
                break;
            case R.id.navigation_discover:
                selectedFragment = new DiscoverFragment();
                textView.setText(R.string.disc_nav);
                Vibrator discoverV = (Vibrator) HomeActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                discoverV.vibrate(50);
                break;
        }
        assert selectedFragment != null;
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();

        return true;
    };


    @Override
    protected void onPause() {
        super.onPause();
        slideHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        slideHandler.postDelayed(sliderRunnable, 2000);
    }

}