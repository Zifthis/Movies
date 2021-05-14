package com.example.movies.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movies.R;
import com.example.movies.details.MovieDetails;
import com.example.movies.model.Result;
import com.flaviofaria.kenburnsview.KenBurnsView;

import java.util.List;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder> {

    private final Context context;
    private final List<Result> mItemObjectList;

    public UpcomingAdapter(Context context, List<Result> mItemObjectList) {
        this.context = context;
        this.mItemObjectList = mItemObjectList;
    }

    @NonNull
    @Override
    public UpcomingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UpcomingViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.upcoming_item_container,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingViewHolder holder, int position) {
        final Result result = mItemObjectList.get(position);
        if (result == null) {
            return;
        }
        String imagePath = "https://image.tmdb.org/t/p/w400" + result.getPosterPath();
        Glide.with(context)
                .load(imagePath)
                .placeholder(R.drawable.placeholder_black)
                .into(holder.kenBurnsView);
        holder.textTitle.setText(result.getTitle());
        holder.textRating.setText(result.getVoteAverage().toString());
        holder.textDate.setText(MovieAdapter.dateAndTimeFormat(result.getReleaseDate()));

    }

    @Override
    public int getItemCount() {
        return mItemObjectList.size();
    }

    public class UpcomingViewHolder extends RecyclerView.ViewHolder {

        private KenBurnsView kenBurnsView;
        private TextView textTitle, textRating, textDate;


        public UpcomingViewHolder(@NonNull View itemView) {
            super(itemView);
            kenBurnsView = itemView.findViewById(R.id.ken_burns_view);
            textTitle = itemView.findViewById(R.id.upcomingDate);
            textRating = itemView.findViewById(R.id.starRating);
            textDate = itemView.findViewById(R.id.upcomingTitle);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int adapterPosition = getAdapterPosition();
                    if (adapterPosition != RecyclerView.NO_POSITION) {

                        Result resultPosition = mItemObjectList.get(adapterPosition);

                        Intent intent = new Intent(context, MovieDetails.class);
                        intent.putExtra("result", resultPosition);
                        context.startActivity(intent);
                    }
                }
            });


        }

    }

}
