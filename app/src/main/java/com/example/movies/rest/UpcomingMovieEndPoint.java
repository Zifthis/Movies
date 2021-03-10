package com.example.movies.rest;

import com.example.movies.model.TopRated;
import com.example.movies.model.Upcoming;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UpcomingMovieEndPoint {

    @GET("movie/upcoming")
    Observable<Upcoming> getUpcoming(@Query("api_key")String apiKey);
}
