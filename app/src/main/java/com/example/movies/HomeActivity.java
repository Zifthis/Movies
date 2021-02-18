package com.example.movies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.example.movies.slider.SliderAdapter;
import com.example.movies.slider.SliderConstructor;
import com.example.movies.ui.popular.PopularFragment;
import com.example.movies.ui.toprated.TopRatedFragment;
import com.example.movies.ui.upcoming.UpcomingFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ViewPager2 viewPagerHolder;
    private final Handler slideHandler = new Handler();
    private LinearLayout layoutOnBoardingIndicator;

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


        viewPagerHolder = findViewById(R.id.viewpagerSlider);
        layoutOnBoardingIndicator = findViewById(R.id.layoutIndicators);


        setupSlider();
        setupIndicator();


    }

    private void setupSlider() {
        List<SliderConstructor> sliderConstructors = new ArrayList<>();

        sliderConstructors.add(new SliderConstructor(R.drawable.hbomax));
        sliderConstructors.add(new SliderConstructor(R.drawable.netflix));
        sliderConstructors.add(new SliderConstructor(R.drawable.disney));
        sliderConstructors.add(new SliderConstructor(R.drawable.curosity));
        sliderConstructors.add(new SliderConstructor(R.drawable.fubotv));
        sliderConstructors.add(new SliderConstructor(R.drawable.hulu));
        sliderConstructors.add(new SliderConstructor(R.drawable.vrv));
        sliderConstructors.add(new SliderConstructor(R.drawable.youtubetv));
        sliderConstructors.add(new SliderConstructor(R.drawable.primevideo));

        viewPagerHolder.setAdapter(new SliderAdapter(sliderConstructors, viewPagerHolder));


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

    private void setupIndicator() {
        RoundedImageView[] indicators = new RoundedImageView[viewPagerHolder.getAdapter().getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8, 0, 0, 0);
        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new RoundedImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            layoutOnBoardingIndicator.addView(indicators[i]);
        }
    }

    private void setCurrentIndicator(int index) {
        int childCount = layoutOnBoardingIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            RoundedImageView roundedImageView = (RoundedImageView) layoutOnBoardingIndicator.getChildAt(i);
            if (i == index) {
                roundedImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                                getApplicationContext(),
                                R.drawable.onboarding_indicator_active
                        )
                );
            } else {
                roundedImageView.setImageDrawable(
                        ContextCompat.getDrawable(
                                getApplicationContext(),
                                R.drawable.onboarding_indicator_inactive
                        )
                );
            }
        }
    }

    private Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            viewPagerHolder.setCurrentItem(viewPagerHolder.getCurrentItem() + 1);
            setCurrentIndicator(viewPagerHolder.getCurrentItem());
        }
    };


    private final BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = item -> {

        Fragment selectedFragment = null;
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
        setCurrentIndicator(viewPagerHolder.getCurrentItem());
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        slideHandler.postDelayed(sliderRunnable, 2000);
        setCurrentIndicator(viewPagerHolder.getCurrentItem());
    }


}