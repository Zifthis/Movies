package com.example.movies.rest;

import com.example.movies.model.Cast;
import com.example.movies.model.CastMovie;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface CastMoviesEndPoint {

    @GET("movie/{movie_id}/credits")
    Call<CastMovie> getCast(@Path("movie_id") int movie_id, @Query("api_key") String apiKey);
}
