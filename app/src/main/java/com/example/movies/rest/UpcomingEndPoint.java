package com.example.movies.rest;

import com.example.movies.model.Upcoming;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface UpcomingEndPoint {

    @GET("movie/upcoming")
    Call<Upcoming> getUpcoming(@Query("api_key") String apiKey);
}
