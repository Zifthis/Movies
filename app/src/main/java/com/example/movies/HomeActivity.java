package com.example.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.movies.adapter.MovieAdapter;
import com.example.movies.model.Similar;
import com.example.movies.rest.SimilarMoviesEndPoint;
import com.example.movies.slider.SliderAdapter;
import com.example.movies.model.Result;
import com.example.movies.model.Upcoming;
import com.example.movies.rest.APIClient;
import com.example.movies.rest.UpcomingEndPoint;
import com.example.movies.slider.SliderItem;
import com.example.movies.ui.popular.PopularFragment;
import com.example.movies.ui.toprated.TopRatedFragment;
import com.example.movies.ui.upcoming.UpcomingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private ViewPager2 viewPager2;
    private Handler slideHandler = new Handler();
    private Fragment selectedFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TopRatedFragment()).commit();

        viewPager2 = findViewById(R.id.viewpagerSlider);

        List<SliderItem> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItem(R.drawable.hbomax));
        sliderItems.add(new SliderItem(R.drawable.netflix));
        sliderItems.add(new SliderItem(R.drawable.disney));
        sliderItems.add(new SliderItem(R.drawable.curosity));
        sliderItems.add(new SliderItem(R.drawable.fubotv));
        sliderItems.add(new SliderItem(R.drawable.hulu));
        sliderItems.add(new SliderItem(R.drawable.vrv));
        sliderItems.add(new SliderItem(R.drawable.youtubetv));
        sliderItems.add(new SliderItem(R.drawable.primevideo));

        viewPager2.setAdapter(new SliderAdapter(sliderItems, viewPager2));

        viewPager2.setClipToPadding(false);
        viewPager2.setClipChildren(false);
        viewPager2.setOffscreenPageLimit(3);
        viewPager2.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);

        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float r = 1 - Math.abs(position);
                page.setScaleY(0.85f + r * 0.15f);
            }
        });
        viewPager2.setPageTransformer(compositePageTransformer);

        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                slideHandler.removeCallbacks(sliderRunnable);
                slideHandler.postDelayed(sliderRunnable, 2000);
            }
        });
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPager2.setCurrentItem(viewPager2.getCurrentItem() + 1);
        }
    };

    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {

        selectedFragment = null;
        switch (item.getItemId()) {
            case R.id.navigation_toprated:
                selectedFragment = new TopRatedFragment();
                break;
            case R.id.navigation_popular:
                selectedFragment = new PopularFragment();
                break;
            case R.id.navigation_upcoming:
                selectedFragment = new UpcomingFragment();
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