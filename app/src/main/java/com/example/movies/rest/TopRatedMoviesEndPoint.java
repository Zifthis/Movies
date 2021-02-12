package com.example.movies.rest;

import com.example.movies.model.TopRated;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TopRatedMoviesEndPoint {

    @GET("movie/top_rated")
    Call<TopRated> getTopRated(@Query("api_key")String apiKey);

}
