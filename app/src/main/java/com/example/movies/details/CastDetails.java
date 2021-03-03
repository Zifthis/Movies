package com.example.movies.details;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.movies.R;
import com.example.movies.model.Cast;

import java.text.DecimalFormat;
import java.util.List;

public class CastDetails extends AppCompatActivity {

    private Cast cast;
    private List<Cast> castList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cast_details);
        getSupportActionBar().hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        if (intent.hasExtra("cast")) {
            cast = getIntent().getParcelableExtra("cast");
            getCastDetails();
        }

    }

    private void getCastDetails() {

        String posterStringId;
        ImageView imageView = findViewById(R.id.detail_cast_id);
        TextView nametxt = findViewById(R.id.cast_id);
        TextView knownTxt = findViewById(R.id.know_for_id);
        TextView populatity = findViewById(R.id.popularity_id);
        TextView movieChar = findViewById(R.id.movie_char_id);


        posterStringId = (String) cast.getProfilePath();
        String pathPoster = "https://image.tmdb.org/t/p/w400" + posterStringId;
        Glide.with(this)
                .load(pathPoster)
                .placeholder(R.drawable.avatarholder)
                .into(imageView);

        nametxt.setText(cast.getName());
        knownTxt.setText(cast.getKnownForDepartment());
        DecimalFormat df = new DecimalFormat("#.0");
        populatity.setText(df.format(cast.getPopularity()));
        movieChar.setText(cast.getCharacter());

    }
}