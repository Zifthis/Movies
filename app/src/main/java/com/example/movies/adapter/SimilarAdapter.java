package com.example.movies.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movies.R;
import com.example.movies.model.Result;

import java.util.ArrayList;

public class SimilarAdapter extends RecyclerView.Adapter<SimilarAdapter.SimilarViewHolder> {


    private final Context context;
    private final ArrayList<Result> resultArrayList;

    public SimilarAdapter(Context context, ArrayList<Result> resultArrayList) {
        this.context = context;
        this.resultArrayList = resultArrayList;
    }

    @Override
    public SimilarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.similar_movie_list, parent, false);
        return new SimilarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimilarViewHolder holder, int position) {

        String imagePath = "https://image.tmdb.org/t/p/w400" + resultArrayList.get(position).getPosterPath();
        Glide.with(context)
                .load(imagePath)
                .placeholder(R.drawable.placeholder)
                .into(holder.movieImageView);
        holder.titleTextView.setText(resultArrayList.get(position).getTitle());
        holder.ratingView.setText(resultArrayList.get(position).getVoteAverage().toString());
        holder.releasedateView.setText(MovieAdapter.dateAndTimeFormat(resultArrayList.get(position).getReleaseDate()));
        holder.originalTitleView.setText(resultArrayList.get(position).getOriginalLanguage().toUpperCase());
    }


    @Override
    public int getItemCount() {
        return resultArrayList.size();
    }


    public class SimilarViewHolder extends RecyclerView.ViewHolder {

        private final ImageView movieImageView;
        private final TextView titleTextView;
        private final TextView ratingView;
        private final TextView releasedateView;
        private final TextView originalTitleView;

        public SimilarViewHolder(View itemView) {
            super(itemView);

            movieImageView = itemView.findViewById(R.id.similarMovieImage);
            titleTextView = itemView.findViewById(R.id.similarTitleMovie);
            ratingView = itemView.findViewById(R.id.similarRatingMovieTxt);
            releasedateView = itemView.findViewById(R.id.similarReleasedateTxt);
            originalTitleView = itemView.findViewById(R.id.similarOriginalTitleTxt);
        }
    }
}
