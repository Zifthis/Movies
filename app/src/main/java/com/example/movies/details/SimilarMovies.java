package com.example.movies.details;


import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.movies.R;
import com.example.movies.adapter.SimilarAdapter;
import com.example.movies.model.Result;
import com.example.movies.model.Similar;
import com.example.movies.rest.APIClient;
import com.example.movies.rest.SimilarMoviesEndPoint;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SimilarMovies extends BottomSheetDialogFragment {


    private final List<Result> mItemObjects;
    private final SimilarMoviesClicked similarMoviesClicked;

    public SimilarMovies(List<Result> mItemObjects, SimilarMoviesClicked similarMoviesClicked) {
        this.mItemObjects = mItemObjects;
        this.similarMoviesClicked = similarMoviesClicked;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_sheet, null);
        bottomSheetDialog.setContentView(view);

        RecyclerView rvData = view.findViewById(R.id.rcv_data);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvData.setLayoutManager(linearLayoutManager);

        SimilarAdapter similarAdapter = new SimilarAdapter(getActivity(), mItemObjects, new SimilarMoviesClicked() {
            @Override
            public void movieClicked(Result result){
                similarMoviesClicked.movieClicked(result);
            }
        });
        rvData.setAdapter(similarAdapter);

        return bottomSheetDialog;
    }

}
