package com.example.movies.details;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class MovieDetails extends AppCompatActivity {


    private Result result;
    private MovieAdapter adapter;
    private RecyclerView authorBar;
    private int movieId;
    private CastAdapter castAdapter;
    private List<Result> similarListResult;
    private List<Cast> castList;
    private Observable<CastMovie> resultObservableCast;
    private CompositeDisposable compositeDisposableCast = new CompositeDisposable();
    private Observable<Similar> resultObservableSimilar;
    private CompositeDisposable compositeDisposableSimilar = new CompositeDisposable();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        Button btn = findViewById(R.id.btn_similar);
        Button btnBack = findViewById(R.id.back_btn);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        RecyclerView castRecylcer = findViewById(R.id.cast_recylcer);

        if (intent.hasExtra("result")) {
            result = getIntent().getParcelableExtra("result");
            movieId = result.getId();
            getDetails();
            init(movieId);

        } else if (intent.hasExtra("clickedMovie")) {
            result = getIntent().getParcelableExtra("clickedMovie");
            getDetails();
            init(result.getId());
            btn.setVisibility(View.GONE);
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        MovieDetails.this, R.style.CustomBottomSheetDialogTheme
                );
                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.bottom_sheet,
                                findViewById(R.id.bottom_sheetContainer)
                        );
                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
                getApiSimilar();
                bottomSheetDialog.dismiss();
            }
        });

        castRecylcer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                        MovieDetails.this, R.style.CustomBottomSheetDialogTheme);

                View bottomSheetView = LayoutInflater.from(getApplicationContext())
                        .inflate(
                                R.layout.bottom_sheet_cast,
                                findViewById(R.id.bottom_const)
                        );

                bottomSheetDialog.setContentView(bottomSheetView);
                bottomSheetDialog.show();
                bottomSheetDialog.dismiss();
            }
        });

    }

    //details UI builder
    @SuppressLint("SetTextI18n")
    private void getDetails() {

        String imgPoster, imgCover;
        ImageView coverImg = findViewById(R.id.poster);
        ImageView posterImg = findViewById(R.id.cover);
        TextView titleTxt = findViewById(R.id.title);
        TextView releasedTxt = findViewById(R.id.datemovie);
        TextView overviewTxt = findViewById(R.id.overview);
        TextView score = findViewById(R.id.score_movie_details);


        imgPoster = result.getPosterPath();
        String pathPoster = "https://image.tmdb.org/t/p/w400" + imgPoster;
        Glide.with(this)
                .load(pathPoster)
                .placeholder(R.drawable.placeholder_black)
                .into(posterImg);


        imgCover = result.getBackdropPath();
        String pathCover = "https://image.tmdb.org/t/p/w400" + imgCover;
        Glide.with(this)
                .load(pathCover)
                .placeholder(R.drawable.placeholder_black)
                .into(coverImg);


        titleTxt.setText(result.getTitle());
        releasedTxt.setText(MovieAdapter.dateAndTimeFormat(result.getReleaseDate()));
        overviewTxt.setText(result.getOverview());
        score.setText(result.getVoteAverage().toString());
    }

    //api call for similar date movies
    private void getApiSimilar() {
        similarListResult = new ArrayList<>();
        SimilarMoviesEndPoint similarMoviesEndPoint = APIClient.getClient().create(SimilarMoviesEndPoint.class);
        resultObservableSimilar = similarMoviesEndPoint.getSimilar(movieId, this.getString(R.string.api_key));

        compositeDisposableSimilar.add(resultObservableSimilar.
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Similar, Observable<Result>>() {
                    @Override
                    public Observable<Result> apply(Similar similar) throws Throwable {
                        return Observable.fromArray(similar.getResults().toArray(new Result[0]));
                    }
                })
                .subscribeWith(new DisposableObserver<Result>() {
                    @Override
                    public void onNext(@NonNull Result result) {
                        similarListResult.add(result);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        SimilarMovies similarMovies = new SimilarMovies(similarListResult, new SimilarMoviesClicked() {
                            @Override
                            public void movieClicked(Result result) {

                                Intent intent = new Intent(getApplicationContext(), MovieDetails.class);
                                intent.putExtra("clickedMovie", result);
                                startActivity(intent);

                            }
                        });
                        similarMovies.show(getSupportFragmentManager(), similarMovies.getTag());
                    }
                }));

    }

    //passing current movie id
    private void init(int id) {
        authorBar = findViewById(R.id.cast_recylcer);
        getApiCast(id);
    }

    //api call for cast data
    private void getApiCast(int idMovie) {
        castList = new ArrayList<>();
        CastMoviesEndPoint castMoviesEndPoint = APIClient.getClient().create(CastMoviesEndPoint.class);
        resultObservableCast = castMoviesEndPoint.getCast(idMovie, this.getString(R.string.api_key));

        compositeDisposableCast.add(resultObservableCast
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<CastMovie, Observable<Cast>>() {
                    @Override
                    public Observable<Cast> apply(CastMovie castMovie) throws Throwable {
                        return Observable.fromArray(castMovie.getCast().toArray(new Cast[0]));
                    }
                })
                //filter only cast with avatar image
                .filter(new Predicate<Cast>() {
                    @Override
                    public boolean test(Cast cast) throws Throwable {
                        return cast.getProfilePath() != null;
                    }
                })
                .subscribeWith(new DisposableObserver<Cast>() {
                    @Override
                    public void onNext(@NonNull Cast cast) {
                        castList.add(cast);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        castAdapter = new CastAdapter(castList, MovieDetails.this);
                        authorBar.setAdapter(castAdapter);
                        authorBar.setLayoutManager(new LinearLayoutManager(MovieDetails.this, RecyclerView.HORIZONTAL, false));
                        authorBar.addItemDecoration(new CastDeco(12));
                    }
                })
        );


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposableCast.clear();
        compositeDisposableSimilar.clear();
    }

}
