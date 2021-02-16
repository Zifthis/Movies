package com.example.movies.ui.upcoming;

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
import com.example.movies.model.Result;
import com.example.movies.model.Upcoming;
import com.example.movies.rest.APIClient;
import com.example.movies.rest.UpcomingEndPoint;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpcomingFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private ArrayList<Result> resultsUpcoming;
    private SwipeRefreshLayout swipeRefreshLayout;
    private View root;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        root = inflater.inflate(R.layout.fragment_upcoming, container, false);

        swipeRefreshLayout = root.findViewById(R.id.swipe_upcoming);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorScheme(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        return root;

    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        recyclerView = view.findViewById(R.id.upcoming_recylcer);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);


        getUpcomingMovies();
    }

    public void getUpcomingMovies() {
        UpcomingEndPoint upcomingEndPoint = APIClient.getClient().create(UpcomingEndPoint.class);
        Call<Upcoming> call = upcomingEndPoint.getUpcoming(this.getString(R.string.api_key));
        call.enqueue(new Callback<Upcoming>() {
            @Override
            public void onResponse(Call<Upcoming> call, Response<Upcoming> response) {

                if (response.isSuccessful()) {
                    Upcoming upcoming = response.body();
                    resultsUpcoming = (ArrayList<Result>) upcoming.getResults();
                    recyclerView.setAdapter(new MovieAdapter(getActivity(), resultsUpcoming));
                }

            }

            @Override
            public void onFailure(Call<Upcoming> call, Throwable t) {
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