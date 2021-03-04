package com.example.movies.rest;


import com.example.movies.model.Similar;
import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SimilarMoviesEndPoint {

    @GET("movie/{movie_id}/similar")
    Observable<Similar> getSimilar(@Path("movie_id") int movie_id, @Query("api_key") String apiKey);

}
