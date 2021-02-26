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

import java.util.List;

public class SimilarAdapter extends RecyclerView.Adapter<SimilarAdapter.SimilarViewHolder> {

    private Context context;
    private List<Result> mItemObjectList;


    public SimilarAdapter(Context context, List<Result> mItemObjectList) {
        this.context = context;
        this.mItemObjectList = mItemObjectList;
    }

    @Override
    public SimilarViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.similar_movie_list, parent, false);
        return new SimilarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimilarViewHolder holder, int position) {

        Result result = mItemObjectList.get(position);
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
    }


    @Override
    public int getItemCount() {
        if (mItemObjectList != null) {
            return mItemObjectList.size();
        }
        return 0;
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
