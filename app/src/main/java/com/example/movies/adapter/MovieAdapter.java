package com.example.movies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movies.R;
import com.example.movies.model.Result;


import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context context;
    private ArrayList<Result> resultArrayListList;


    public MovieAdapter(Context context, ArrayList<Result> resultArrayListList) {
        this.context = context;
        this.resultArrayListList = resultArrayListList;
    }


    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_list, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {

        String imagePath = "https://image.tmdb.org/t/p/w400" + resultArrayListList.get(position).getPosterPath();
        Glide.with(context)
                .load(imagePath)
                .into(holder.movieImageView);


        holder.titleTextView.setText(resultArrayListList.get(position).getTitle());

    }


    @Override
    public int getItemCount() {
        return resultArrayListList.size();
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private ImageView movieImageView;
        private TextView titleTextView;


        //ONCLICK METHOD FOR DETAIL PAGE

        public MovieViewHolder(View itemView) {
            super(itemView);

            movieImageView = itemView.findViewById(R.id.movieImage);
            titleTextView = itemView.findViewById(R.id.titleMovie);

        }
    }

}
