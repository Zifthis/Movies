package com.example.movies.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

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
        holder.releasedateView.setText(dateAndTimeFormat(resultArrayListList.get(position).getReleaseDate()));
        holder.originalTitleView.setText(resultArrayListList.get(position).getOriginalLanguage().toUpperCase());
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_animation));
        holder.titleTextView.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_animation));
        holder.ratingView.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_animation));
        holder.releasedateView.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_animation));
        holder.originalTitleView.setAnimation(AnimationUtils.loadAnimation(context,R.anim.fade_animation));

        //share button
        holder.shareBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT,"Check This Movie Out: \n" +
                        holder.titleTextView.getText() +
                        "\nRating: " +
                        holder.ratingView.getText() +
                        "\nReleased: " +
                        holder.releasedateView.getText());
                shareIntent.setType("text/plain");
                context.startActivity(Intent.createChooser(shareIntent, "Movie"));
            }
        });


    }

    public static String dateAndTimeFormat(String date) {
        String newDate = "";
        try {
            //timezone change
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat outDF = new SimpleDateFormat("dd.MM.yyyy");
            outDF.setTimeZone(TimeZone.getDefault());
            df.setTimeZone(TimeZone.getTimeZone("UTC"));
            String changed = outDF.format(df.parse(date));
            newDate = changed;
            Calendar cal = Calendar.getInstance();
            cal.setTime(outDF.parse(changed));

            return newDate;
        } catch (Exception e) {
            e.printStackTrace();
            return newDate;
        }
    }

    @Override
    public int getItemCount() {
        return resultArrayListList.size();
    }


    public class MovieViewHolder extends RecyclerView.ViewHolder {

        private final CardView cardView;
        private final ImageView movieImageView;
        private final TextView titleTextView;
        private final TextView ratingView;
        private final TextView releasedateView;
        private final TextView originalTitleView;
        private final ImageView shareBnt;


        public MovieViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_view);
            shareBnt = itemView.findViewById(R.id.share_btn);
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
