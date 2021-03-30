package com.example.movies.ui.popular;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.movies.MyApp;
import com.example.movies.R;
import com.example.movies.adapter.MovieAdapter;
import com.example.movies.model.PopularMovies;
import com.example.movies.model.Result;
import com.example.movies.rest.APIClient;
import com.example.movies.rest.PopularMoviesEndPoint;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PopularFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private ArrayList<Result> resultsPopular;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MovieAdapter adapter;
    private View root;
    private Observable<PopularMovies> resultObservable;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_popular, container, false);


        return root;
    }


    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        recyclerView = view.findViewById(R.id.popular_recycler);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);

        swipeRefreshLayout = root.findViewById(R.id.swipe_popular);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(
                android.R.color.holo_orange_light);
        int myColor = Color.parseColor("#000000");
        swipeRefreshLayout.setProgressBackgroundColorSchemeColor(myColor);

        getPopularMovies();
    }

    public void getPopularMovies() {
        resultsPopular = new ArrayList<>();
        PopularMoviesEndPoint popularMoviesEndPoint = APIClient.getClient().create(PopularMoviesEndPoint.class);
        resultObservable = popularMoviesEndPoint.getPopularMovies(this.getString(R.string.api_key));

        compositeDisposable.add(resultObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<PopularMovies, Observable<Result>>() {
                    @Override
                    public Observable<Result> apply(PopularMovies popularMovies) throws Throwable {
                        return Observable.fromArray(popularMovies.getResults().toArray(new Result[0]));
                    }
                })
                .subscribeWith(new DisposableObserver<Result>() {
                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Result result) {
                        resultsPopular.add(result);
                        MyApp.getInstance().setMoviePopular(resultsPopular);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        adapter = new MovieAdapter(root.getContext(), MyApp.getInstance().getMoviePopular());
                        MyApp.getInstance().setPopAdapter(adapter);
                        recyclerView.setAdapter(adapter);
                    }
                })
        );

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

    @Override
    public void onRefresh() {

        new Handler().postDelayed(() -> {
            getPopularMovies();
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getContext(), "Refreshed", Toast.LENGTH_LONG).show();

        }, 1500);

    }
}