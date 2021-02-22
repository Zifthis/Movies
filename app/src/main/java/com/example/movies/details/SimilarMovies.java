package com.example.movies.details;


import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.movies.R;
import com.example.movies.adapter.SimilarAdapter;
import com.example.movies.model.Result;
import com.example.movies.model.Similar;
import com.example.movies.rest.APIClient;
import com.example.movies.rest.SimilarMoviesEndPoint;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimilarMovies extends AppCompatActivity {

    private int idMovie;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView ;
    private SimilarAdapter similarAdapter;
    private ArrayList<Result> similarResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.similar_movie);
        getSupportActionBar().setTitle("Similar Movies");


        idMovie = getIntent().getIntExtra("movie_id", 0);


        recyclerView = findViewById(R.id.similar_recylcer);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(SimilarMovies.this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);


        swipeRefreshLayout = findViewById(R.id.swipe_similar);
        swipeRefreshLayout.setColorScheme(android.R.color.darker_gray,
                android.R.color.black,
                android.R.color.holo_orange_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(() -> {
                    getSimilarMovies();
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getApplicationContext(), "Refreshed", Toast.LENGTH_LONG).show();
                }, 1500);
            }
        });

        getSimilarMovies();
    }


    public void getSimilarMovies() {
        SimilarMoviesEndPoint similarMoviesEndPoint = APIClient.getClient().create(SimilarMoviesEndPoint.class);
        Call<Similar> call = similarMoviesEndPoint.getSimilar(idMovie, this.getString(R.string.api_key));
        call.enqueue(new Callback<Similar>() {
            @Override
            public void onResponse(Call<Similar> call, Response<Similar> response) {

                if (response.isSuccessful()) {
                    Similar similar = response.body();
                    similarResult = (ArrayList<Result>) similar.getResults();
                    similarAdapter = new SimilarAdapter(getApplicationContext(), similarResult);
                    recyclerView.setAdapter(similarAdapter);
                }
            }

            @Override
            public void onFailure(Call<Similar> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
