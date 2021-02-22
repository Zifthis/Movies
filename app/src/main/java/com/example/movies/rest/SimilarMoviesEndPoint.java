package com.example.movies.rest;

import com.example.movies.model.PopularMovies;
import com.example.movies.model.Similar;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SimilarMoviesEndPoint {

    @GET("movie/{movie_id}/similar")
    Call<Similar> getSimilar(@Path("movie_id") int idMovie, @Query("api_key") String apiKey);

}
