package com.example.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.movies.model.Result;
import com.example.movies.model.Upcoming;
import com.example.movies.rest.APIClient;
import com.example.movies.rest.UpcomingEndPoint;
import com.example.movies.slider.SliderAdapter;
import com.example.movies.ui.popular.PopularFragment;
import com.example.movies.ui.toprated.TopRatedFragment;
import com.example.movies.ui.discover.DiscoverFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TopRatedFragment()).commit();


        textView = findViewById(R.id.nav_location_txt);


        viewPagerHolder = findViewById(R.id.viewpagerSlider);

        setupSlider();


    }

    private void setupSlider() {
        UpcomingEndPoint upcomingEndPoint = APIClient.getClient().create(UpcomingEndPoint.class);
        Call<Upcoming> call = upcomingEndPoint.getUpcoming(this.getString(R.string.api_key));
        call.enqueue(new Callback<Upcoming>() {
            @Override
            public void onResponse(Call<Upcoming> call, Response<Upcoming> response) {

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
            public void onFailure(Call<Upcoming> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }


    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPagerHolder.setCurrentItem(viewPagerHolder.getCurrentItem() + 1);
        }
    };


    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {

        Fragment selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_toprated:
                selectedFragment = new TopRatedFragment();
                textView.setText("Top Rated Movies");
                Vibrator topV = (Vibrator) HomeActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                topV.vibrate(50);
                break;
            case R.id.navigation_popular:
                selectedFragment = new PopularFragment();
                textView.setText("Popular Movies");
                Vibrator popularV = (Vibrator) HomeActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
                popularV.vibrate(50);
                break;
            case R.id.navigation_discover:
                selectedFragment = new DiscoverFragment();
                textView.setText("Discover Movies");
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