package com.example.movies.rest;

import com.example.movies.model.PopularMovies;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PopularMoviesEndPoint {

    @GET("movie/popular")
    Observable<PopularMovies> getPopularMovies(@Query("api_key") String apiKey);

}
