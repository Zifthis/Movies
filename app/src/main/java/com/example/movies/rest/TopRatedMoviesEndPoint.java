package com.example.movies.rest;

import com.example.movies.model.TopRated;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TopRatedMoviesEndPoint {


    @GET("movie/top_rated")
    Observable<TopRated> getTopRated(@Query("api_key")String apiKey);


}
