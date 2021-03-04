package com.example.movies.ui.discover;

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
import com.example.movies.model.Discover;
import com.example.movies.model.Result;
import com.example.movies.rest.APIClient;
import com.example.movies.rest.DiscoverMoviesEndPoint;
import java.util.ArrayList;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.observers.DisposableObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class DiscoverFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private ArrayList<Result> resultsDiscover;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MovieAdapter adapter;
    private View root;
    private Observable<Discover> resultObservable;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_discover, container, false);


        return root;
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.discover_recycler);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);


        swipeRefreshLayout = root.findViewById(R.id.swipe_discover);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(
                android.R.color.black,
                android.R.color.holo_orange_light);

        getDiscoverMovies();
    }

    public void getDiscoverMovies() {
        resultsDiscover = new ArrayList<>();
        DiscoverMoviesEndPoint discoverFragment = APIClient.getClient().create(DiscoverMoviesEndPoint.class);
        resultObservable = discoverFragment.getDiscoverMovies(this.getString(R.string.api_key));

        compositeDisposable.add(resultObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(new Function<Discover, Observable<Result>>() {
                    @Override
                    public Observable<Result> apply(Discover discover) throws Throwable {
                        return Observable.fromArray(discover.getResults().toArray(new Result[0]));
                    }
                })
                .subscribeWith(new DisposableObserver<Result>() {
                    @Override
                    public void onNext(@io.reactivex.rxjava3.annotations.NonNull Result result) {
                        resultsDiscover.add(result);
                        MyApp.getInstance().setMovieDiscover(resultsDiscover);
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        adapter = new MovieAdapter(root.getContext(), MyApp.getInstance().getMovieDiscover());
                        MyApp.getInstance().setDiscAdapter(adapter);
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
            getDiscoverMovies();
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getContext(), "Refreshed", Toast.LENGTH_LONG).show();

        }, 1500);

    }


}
