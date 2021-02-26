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

import com.example.movies.R;
import com.example.movies.adapter.MovieAdapter;
import com.example.movies.model.Discover;
import com.example.movies.model.Result;
import com.example.movies.model.Upcoming;
import com.example.movies.rest.APIClient;
import com.example.movies.rest.DiscoverMoviesEndPoint;
import com.example.movies.rest.UpcomingEndPoint;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscoverFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private ArrayList<Result> resultsUpcoming;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        root = inflater.inflate(R.layout.fragment_discover, container, false);

        swipeRefreshLayout = root.findViewById(R.id.swipe_discover);
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


        recyclerView = view.findViewById(R.id.discover_recylcer);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);


        getUpcomingMovies();
    }

    public void getUpcomingMovies() {
        DiscoverMoviesEndPoint discoverFragment = APIClient.getClient().create(DiscoverMoviesEndPoint.class);
        Call<Discover> call = discoverFragment.getDiscoverMovies(this.getString(R.string.api_key));
        call.enqueue(new Callback<Discover>() {
            @Override
            public void onResponse(Call<Discover> call, Response<Discover> response) {

                if (response.isSuccessful()) {
                    Discover discover = response.body();
                    resultsUpcoming = (ArrayList<Result>) discover.getResults();
                    recyclerView.setAdapter(new MovieAdapter(getActivity(), resultsUpcoming));
                }

            }

            @Override
            public void onFailure(Call<Discover> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public void onRefresh() {

        new Handler().postDelayed(() -> {
            getUpcomingMovies();
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(getContext(), "Refreshed", Toast.LENGTH_LONG).show();

        }, 1500);

    }


}
