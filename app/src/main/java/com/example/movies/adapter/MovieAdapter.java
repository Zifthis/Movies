package com.example.movies.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.movies.R;
import com.example.movies.details.MovieDetails;
import com.example.movies.details.SimilarMovies;
import com.example.movies.model.PopularMovies;
import com.example.movies.model.Result;
import com.example.movies.model.Similar;
import com.example.movies.rest.APIClient;
import com.example.movies.rest.SimilarMoviesEndPoint;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private final Context context;
    private final ArrayList<Result> resultArrayListList;


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
                .placeholder(R.drawable.placeholder)
                .into(holder.movieImageView);


        holder.titleTextView.setText(resultArrayListList.get(position).getTitle());
        holder.ratingView.setText(resultArrayListList.get(position).getVoteAverage().toString());
        holder.releasedateView.setText(resultArrayListList.get(position).getReleaseDate());
        holder.originalTitleView.setText(resultArrayListList.get(position).getOriginalLanguage());

    }


    @Override
    public int getItemCount() {
        return resultArrayListList.size();
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private final ImageView movieImageView;
        private final TextView titleTextView;
        private final TextView ratingView;
        private final TextView releasedateView;
        private final TextView originalTitleView;



        public MovieViewHolder(View itemView) {
            super(itemView);

            movieImageView = itemView.findViewById(R.id.movieImage);
            titleTextView = itemView.findViewById(R.id.titleMovie);
            ratingView = itemView.findViewById(R.id.ratingMovieTxt);
            releasedateView = itemView.findViewById(R.id.releasedateTxt);
            originalTitleView = itemView.findViewById(R.id.originalTitleTxt);

            itemView.setOnClickListener(v -> {
                int adapterPosition = getAdapterPosition();

                if (adapterPosition != RecyclerView.NO_POSITION) {

                    Result resultPosition = resultArrayListList.get(adapterPosition);

                    Intent intent = new Intent(context, MovieDetails.class);
                    intent.putExtra("result", resultPosition);
                    context.startActivity(intent);

                }
            });

        }



    }



}
