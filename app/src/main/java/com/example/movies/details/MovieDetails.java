package com.example.movies.details;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.movies.R;
import com.example.movies.model.Result;

import java.util.List;

public class MovieDetails extends AppCompatActivity {

    private String imgPoster, imgCover;
    private List<Integer> idList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        ImageView coverImg = findViewById(R.id.poster);
        ImageView posterImg = findViewById(R.id.cover);
        TextView titelTxt = findViewById(R.id.title);
        TextView relasedateTxt = findViewById(R.id.releasedate);
        TextView genersTxt = findViewById(R.id.genres);
        TextView overviewTxt = findViewById(R.id.overview);

        Intent intent = getIntent();

        if (intent.hasExtra("result")) {

            Result result = getIntent().getParcelableExtra("result");

            Toast.makeText(getApplicationContext(), result.getOriginalTitle(), Toast.LENGTH_LONG).show();


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
            relasedateTxt.setText(result.getReleaseDate());
            // genersTxt.setText(result.getGenreIds());
            overviewTxt.setText(result.getOverview());



        }

    }
}
