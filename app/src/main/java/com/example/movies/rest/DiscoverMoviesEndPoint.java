package com.example.movies.rest;

import com.example.movies.model.Discover;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface DiscoverMoviesEndPoint {

    @GET("discover/movie")
    Observable<Discover> getDiscoverMovies(@Query("api_key") String apiKey);

}
