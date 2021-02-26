package com.example.movies.rest;

import com.example.movies.model.Discover;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DiscoverMoviesEndPoint {

    @GET("discover/movie")
    Call<Discover> getDiscoverMovies(@Query("api_key") String apiKey);

}
