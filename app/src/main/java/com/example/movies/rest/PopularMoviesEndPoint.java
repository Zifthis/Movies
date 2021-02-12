package com.example.movies.rest;

import com.example.movies.model.PopularMovies;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PopularMoviesEndPoint {

    @GET("movie/popular")
    Call<PopularMovies> getPopularMovies(@Query("api_key") String apiKey);

}
