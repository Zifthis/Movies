package com.example.movies.ui.popular;


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

import com.example.movies.HomeActivity;
import com.example.movies.MyApp;
import com.example.movies.R;
import com.example.movies.adapter.MovieAdapter;
import com.example.movies.model.PopularMovies;
import com.example.movies.model.Result;
import com.example.movies.rest.APIClient;
import com.example.movies.rest.PopularMoviesEndPoint;
import com.example.movies.ui.toprated.TopRatedFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PopularFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private ArrayList<Result> resultsPopular;
    private SwipeRefreshLayout swipeRefreshLayout;
    private MovieAdapter adapter;
    private View root;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        root = inflater.inflate(R.layout.fragment_popular, container, false);

        swipeRefreshLayout = root.findViewById(R.id.swipe_popular);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(android.R.color.darker_gray,
                android.R.color.black,
                android.R.color.holo_orange_light);


        return root;

    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle("Popular Movies");

        recyclerView = view.findViewById(R.id.popular_recycler);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);


        getPopularMovies();
    }

    public void getPopularMovies() {
        PopularMoviesEndPoint popularMoviesEndPoint = APIClient.getClient().create(PopularMoviesEndPoint.class);
        Call<PopularMovies> call = popularMoviesEndPoint.getPopularMovies(this.getString(R.string.api_key));
        call.enqueue(new Callback<PopularMovies>() {
            @Override
            public void onResponse(Call<PopularMovies> call, Response<PopularMovies> response) {

                if (response.isSuccessful()) {
                    PopularMovies popularMovies = response.body();
                    resultsPopular = (ArrayList<Result>) popularMovies.getResults();
                    MyApp.getInstance().setMoviePopular(resultsPopular);

                    adapter = new MovieAdapter(root.getContext(), MyApp.getInstance().getMoviePopular());
                    MyApp.getInstance().setPopAdapter(adapter);
                    recyclerView.setAdapter(adapter);


                    //recyclerView.setAdapter(new MovieAdapter(getActivity(), resultsPopular));
                }

            }

            @Override
            public void onFailure(Call<PopularMovies> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
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