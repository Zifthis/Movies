package com.example.movies;

import android.app.Application;

import androidx.fragment.app.Fragment;

import com.example.movies.adapter.MovieAdapter;
import com.example.movies.model.Result;

import java.util.ArrayList;

public class MyApp extends Application {

    public static MyApp instance = null;
    private Fragment fragment;


    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;

    }

    public ArrayList<Result> movieDiscover = new ArrayList<>();
    public ArrayList<Result> moviePopular = new ArrayList<>();
    public ArrayList<Result> movieTopRated = new ArrayList<>();

    public MovieAdapter discAdapter;
    public MovieAdapter popAdapter;
    public MovieAdapter topAdapter;

    public static MyApp getInstance() {
        return instance;
    }

    public static void setInstance(MyApp instance) {
        MyApp.instance = instance;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public ArrayList<Result> getMovieDiscover() {
        return movieDiscover;
    }

    public void setMovieDiscover(ArrayList<Result> movieDiscover) {
        this.movieDiscover = movieDiscover;
    }

    public ArrayList<Result> getMoviePopular() {
        return moviePopular;
    }

    public void setMoviePopular(ArrayList<Result> moviePopular) {
        this.moviePopular = moviePopular;
    }

    public ArrayList<Result> getMovieTopRated() {
        return movieTopRated;
    }

    public void setMovieTopRated(ArrayList<Result> movieTopRated) {
        this.movieTopRated = movieTopRated;
    }

    public MovieAdapter getDiscAdapter() {
        return discAdapter;
    }

    public void setDiscAdapter(MovieAdapter discAdapter) {
        this.discAdapter = discAdapter;
    }

    public MovieAdapter getPopAdapter() {
        return popAdapter;
    }

    public void setPopAdapter(MovieAdapter popAdapter) {
        this.popAdapter = popAdapter;
    }

    public MovieAdapter getTopAdapter() {
        return topAdapter;
    }

    public void setTopAdapter(MovieAdapter topAdapter) {
        this.topAdapter = topAdapter;
    }
}
