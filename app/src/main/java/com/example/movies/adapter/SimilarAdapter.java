package com.example.movies.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movies.R;
import com.example.movies.details.SimilarMoviesClicked;
import com.example.movies.model.Result;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class SimilarAdapter extends RecyclerView.Adapter<SimilarAdapter.SimilarViewHolder> {

    private final Context context;
    private final List<Result> mItemObjectList;
    private final SimilarMoviesClicked similarMoviesClicked;


    public SimilarAdapter(Context context, List<Result> mItemObjectList, SimilarMoviesClicked similarMoviesClicked) {
        this.context = context;
        this.mItemObjectList = mItemObjectList;
        this.similarMoviesClicked = similarMoviesClicked;
    }

    @NotNull
    @Override
    public SimilarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.similar_movie_list, parent, false);
        return new SimilarViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NotNull SimilarViewHolder holder, int position) {

        final Result result = mItemObjectList.get(position);
        if(result == null){
            return;
        }
        String imagePath = "https://image.tmdb.org/t/p/w400" + result.getPosterPath();
        Glide.with(context)
                .load(imagePath)
                .placeholder(R.drawable.placeholder)
                .into(holder.movieImageView);
        holder.titleTextView.setText(result.getOriginalTitle());
        holder.ratingView.setText(result.getVoteAverage().toString());
        holder.releasedateView.setText(MovieAdapter.dateAndTimeFormat(result.getReleaseDate()));
        holder.originalTitleView.setText(result.getOriginalLanguage().toUpperCase());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                similarMoviesClicked.movieClicked(result);
            }
        });
    }


    @Override
    public int getItemCount() {
        if (mItemObjectList != null) {
            return mItemObjectList.size();
        }
        return 0;
    }


    public static class SimilarViewHolder extends RecyclerView.ViewHolder {

        private final ImageView movieImageView;
        private final TextView titleTextView;
        private final TextView ratingView;
        private final TextView releasedateView;
        private final TextView originalTitleView;
        private final CardView cardView;


        public SimilarViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.similarCard_view);
            movieImageView = itemView.findViewById(R.id.similarMovieImage);
            titleTextView = itemView.findViewById(R.id.similarTitleMovie);
            ratingView = itemView.findViewById(R.id.similarRatingMovieTxt);
            releasedateView = itemView.findViewById(R.id.similarReleasedateTxt);
            originalTitleView = itemView.findViewById(R.id.similarOriginalTitleTxt);
        }
    }
}
