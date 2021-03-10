package com.example.movies.ui.upcoming;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.movies.MyApp;
import com.example.movies.R;
import com.example.movies.adapter.MovieAdapter;
import com.example.movies.adapter.UpcomingAdapter;
import com.example.movies.model.Result;
import com.example.movies.model.TopRated;
import com.example.movies.model.Upcoming;
import com.example.movies.rest.APIClient;
import com.example.movies.rest.TopRatedMoviesEndPoint;
import com.example.movies.rest.UpcomingMovieEndPoint;

import java.util.ArrayList;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class UpcomingFragment extends Fragment {


    private ViewPager2 viewPager2;
    private ArrayList<Result> resultsUpcoming;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View root;
    private UpcomingAdapter adapter;
    private Observable<Upcoming> resultObservable;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_upcoming, container, false);
        return root;
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewPager2 = root.findViewById(R.id.movieViewPager);
        getUpcomingMovies();
    }

    public void getUpcomingMovies() {
        resultsUpcoming = new ArrayList<>();
        UpcomingMovieEndPoint upcomingMovieEndPoint = APIClient.getClient().create(UpcomingMovieEndPoint.class);
        resultObservable = upcomingMovieEndPoint.getUpcoming(this.getString(R.string.api_key));

        compositeDisposable.add(resultObservable.
                subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Upcoming, Observable<Result>>() {
                    @Override
                    public Observable<Result> apply(Upcoming upcoming) throws Throwable {
                        return Observable.fromArray(upcoming.getResults().toArray(new Result[0]));
                    }
                })
                .subscribeWith(new DisposableObserver<Result>() {
                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Result result) {
                        resultsUpcoming.add(result);

                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        adapter = new UpcomingAdapter(getContext(), resultsUpcoming);
                        viewPager2.setAdapter(adapter);
                    }
                }));
    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        compositeDisposable.clear();
    }

}