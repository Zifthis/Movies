package com.example.movies.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.movies.R;
import com.example.movies.details.MovieDetails;
import com.example.movies.model.Result;
import org.jetbrains.annotations.NotNull;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> implements Filterable {

    private final Context context;
    private final ArrayList<Result> resultArrayListList;
    private ArrayList<Result> resultArrayListListFiltered;

    public MovieAdapter(Context context, ArrayList<Result> resultArrayListList) {
        this.context = context;
        this.resultArrayListList = resultArrayListList;
        this.resultArrayListListFiltered = resultArrayListList;
    }

    @NotNull
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_list, parent, false);
        return new MovieViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {


        String imagePath = "https://image.tmdb.org/t/p/w400" + resultArrayListListFiltered.get(position).getPosterPath();
        Glide.with(context)
                .load(imagePath)
                .placeholder(R.drawable.placeholder)
                .into(holder.movieImageView);


        holder.titleTextView.setText(resultArrayListListFiltered.get(position).getTitle());
        holder.ratingView.setText(resultArrayListListFiltered.get(position).getVoteAverage().toString());
        holder.releasedateView.setText(dateAndTimeFormat(resultArrayListListFiltered.get(position).getReleaseDate()));
        holder.originalTitleView.setText(resultArrayListListFiltered.get(position).getOriginalLanguage().toUpperCase());


        //share button
        holder.shareBnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Vibrator topV = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
                topV.vibrate(50);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check This Movie Out: \n" +
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
            @SuppressLint("SimpleDateFormat") SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            @SuppressLint("SimpleDateFormat") SimpleDateFormat outDF = new SimpleDateFormat("dd.MM.yyyy");
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
        if (resultArrayListListFiltered != null) {
            return resultArrayListListFiltered.size();
        } else {
            return 0;
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String s = constraint.toString();
                if(s.isEmpty()){
                    resultArrayListListFiltered = resultArrayListList;
                }else {
                    ArrayList<Result> IsFiltered = new ArrayList<>();
                    for(Result row : resultArrayListList){
                        if(row.getTitle().toLowerCase().contains(s.toLowerCase())){
                            IsFiltered.add(row);
                        }
                    }

                    resultArrayListListFiltered = IsFiltered;

                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = resultArrayListListFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                resultArrayListListFiltered = (ArrayList<Result>) results.values;
                notifyDataSetChanged();
            }
        };
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

                    Result resultPosition = resultArrayListListFiltered.get(adapterPosition);

                    Intent intent = new Intent(context, MovieDetails.class);
                    intent.putExtra("result", resultPosition);
                    context.startActivity(intent);

                }
            });

        }

    }

}
