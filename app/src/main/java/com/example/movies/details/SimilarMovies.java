package com.example.movies.details;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.example.movies.R;
import com.example.movies.adapter.MovieAdapter;
import com.example.movies.model.Result;
import com.example.movies.model.Similar;
import com.example.movies.rest.APIClient;
import com.example.movies.rest.SimilarMoviesEndPoint;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimilarMovies extends AppCompatActivity {

    private int intKey = R.string.api_key;
    private String apiKey = Integer.toString(intKey);
    private String recivedMovieId;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private ArrayList<Result> similarResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.similar_movie);

        Bundle extras = getIntent().getExtras();
        recivedMovieId = extras.getString("resultSimilar");

        recyclerView = findViewById(R.id.similar_recycler);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        recyclerView.setLayoutManager(gridLayoutManager);

        getSimilarMovies();


        swipeRefreshLayout = findViewById(R.id.swipe_similar);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                getSimilarMovies();

            }
        });

    }

    public void getSimilarMovies() {
        SimilarMoviesEndPoint similarMoviesEndPoint = APIClient.getClient().create(SimilarMoviesEndPoint.class);
        Call<Similar> call = similarMoviesEndPoint.getSimilar(apiKey, recivedMovieId);
        call.enqueue(new Callback<Similar>() {
            @Override
            public void onResponse(Call<Similar> call, Response<Similar> response) {


                if (response.isSuccessful()) {
                    Similar similar = response.body();
                    similarResult = (ArrayList<Result>) similar.getResults();
                    recyclerView.setAdapter(new MovieAdapter(getApplicationContext(),similarResult));
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onFailure(Call<Similar> call, Throwable t) {
                Toast.makeText(getApplicationContext(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}
