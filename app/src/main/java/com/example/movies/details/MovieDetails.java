package com.example.movies.details;

import android.content.Intent;
import android.graphics.Movie;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movies.R;
import com.example.movies.adapter.CastAdapter;
import com.example.movies.adapter.MovieAdapter;
import com.example.movies.model.Cast;
import com.example.movies.model.CastMovie;
import com.example.movies.model.Result;
import com.example.movies.model.Similar;
import com.example.movies.rest.APIClient;
import com.example.movies.rest.CastMoviesEndPoint;
import com.example.movies.rest.SimilarMoviesEndPoint;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.movies.R.color.txt_color;

public class MovieDetails extends AppCompatActivity {


    private Result result;
    private MovieAdapter adapter;
    private RecyclerView authorBar;
    int movieId;
    private List<Result> similarListResult;
    private List<Cast> castList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        getSupportActionBar().setTitle("Movie Details");


        String imgPoster, imgCover;
        ImageView coverImg = findViewById(R.id.poster);
        ImageView posterImg = findViewById(R.id.cover);
        TextView titelTxt = findViewById(R.id.title);
        TextView relasedateTxt = findViewById(R.id.releasedate);
        TextView genersTxt = findViewById(R.id.genres);
        TextView overviewTxt = findViewById(R.id.overview);
        Button btn = findViewById(R.id.btn_similar);


        Intent intent = getIntent();

        if (intent.hasExtra("result")) {

            result = getIntent().getParcelableExtra("result");

            movieId = result.getId();

            imgPoster = result.getPosterPath();
            String pathPoster = "https://image.tmdb.org/t/p/w400" + imgPoster;
            Glide.with(this)
                    .load(pathPoster)
                    .into(posterImg);


            imgCover = result.getBackdropPath();
            String pathCover = "https://image.tmdb.org/t/p/w400" + imgCover;
            Glide.with(this)
                    .load(pathCover)
                    .into(coverImg);


            titelTxt.setText(result.getTitle());
            relasedateTxt.setText(adapter.dateAndTimeFormat(result.getReleaseDate()));
            genersTxt.setText(result.getGenreIds().toString());
            overviewTxt.setText(result.getOverview());

        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getApiSimilar();

            }
        });

        init();
    }

    private void getApiSimilar() {
        SimilarMoviesEndPoint similarMoviesEndPoint = APIClient.getClient().create(SimilarMoviesEndPoint.class);
        Call<Similar> call = similarMoviesEndPoint.getSimilar(movieId, this.getString(R.string.api_key));
        call.enqueue(new Callback<Similar>() {
            @Override
            public void onResponse(Call<Similar> call, Response<Similar> response) {

                Similar similar = response.body();

                if (similar != null && similar.getResults() != null) {
                    similarListResult = similar.getResults();
                    SimilarMovies similarMovies = new SimilarMovies(similarListResult);
                    similarMovies.show(getSupportFragmentManager(), similarMovies.getTag());
                }
            }

            @Override
            public void onFailure(Call<Similar> call, Throwable t) {
                Toast.makeText(MovieDetails.this, "Something went wrong!\n" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void init() {
        authorBar = findViewById(R.id.cast_recylcer);
        getApiCast();
    }

    private void getApiCast() {
        CastMoviesEndPoint castMoviesEndPoint = APIClient.getClient().create(CastMoviesEndPoint.class);
        Call<CastMovie> call = castMoviesEndPoint.getCast(movieId, this.getString(R.string.api_key));
        call.enqueue(new Callback<CastMovie>() {
            @Override
            public void onResponse(Call<CastMovie> call, Response<CastMovie> response) {

                CastMovie castMovie = response.body();

                if (castMovie != null && castMovie.getCast() != null) {
                    castList = castMovie.getCast();
                    CastAdapter castAdapter = new CastAdapter(castList, MovieDetails.this);
                    authorBar.setAdapter(castAdapter);
                    authorBar.setLayoutManager(new LinearLayoutManager(MovieDetails.this, RecyclerView.HORIZONTAL,false));
                    authorBar.addItemDecoration(new CastDeco(12));
                }
            }

            @Override
            public void onFailure(Call<CastMovie> call, Throwable t) {
                Toast.makeText(MovieDetails.this, "Something went wrong!\n" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


}
