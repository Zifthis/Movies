package com.example.movies.ui.toprated;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.movies.R;
import com.example.movies.adapter.MovieAdapter;
import com.example.movies.model.PopularMovies;
import com.example.movies.model.Result;
import com.example.movies.model.TopRated;
import com.example.movies.rest.APIClient;
import com.example.movies.rest.PopularMoviesEndPoint;
import com.example.movies.rest.TopRatedMoviesEndPoint;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TopRatedFragment extends Fragment {

    RecyclerView recyclerView;
    private ArrayList<Result> resultsTopRated;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_toprated, container, false);

    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.toprated_recylcer);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        getPopularMovies();
    }

    public void getPopularMovies() {
        TopRatedMoviesEndPoint topRatedMoviesEndPoint = APIClient.getClient().create(TopRatedMoviesEndPoint.class);
        Call<TopRated> call = topRatedMoviesEndPoint.getTopRated(this.getString(R.string.api_key));
        call.enqueue(new Callback<TopRated>() {
            @Override
            public void onResponse(Call<TopRated> call, Response<TopRated> response) {

                if (response.isSuccessful()) {
                    TopRated topRated = response.body();
                    resultsTopRated = (ArrayList<Result>) topRated.getResults();

                    recyclerView.setAdapter(new MovieAdapter(getActivity(), resultsTopRated));
                }

            }

            @Override
            public void onFailure(Call<TopRated> call, Throwable t) {
                Toast.makeText(getActivity(), t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

}