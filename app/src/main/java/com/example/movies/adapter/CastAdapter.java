package com.example.movies.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movies.R;
import com.example.movies.details.CastDetails;
import com.example.movies.details.MovieDetails;
import com.example.movies.model.Cast;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    private List<Cast> castResultList;
    private final Context context;
    private MovieDetails movieDetails;

    public CastAdapter(List<Cast> castResultList, Context context) {
        this.castResultList = castResultList;
        this.context = context;
    }

    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cast_items, parent, false);
        return new CastViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CastViewHolder holder, int position) {

        holder.textView.setText(castResultList.get(position).getName());

        String imagePath = "https://image.tmdb.org/t/p/w400" + castResultList.get(position).getProfilePath();
        Glide.with(context)
                .load(imagePath)
                .placeholder(R.drawable.avatarholder)
                .into(holder.imageView);

    }


    @Override
    public int getItemCount() {
        if (castResultList != null) {
            return castResultList.size();
        } else {
            return 0;
        }
    }

    public class CastViewHolder extends RecyclerView.ViewHolder {

        private final ImageView imageView;
        private final TextView textView;

        public CastViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.detail_cast_id);
            textView = itemView.findViewById(R.id.cast_name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(
                            ((MovieDetails)context).getApplicationContext(), R.style.BottomSheetDialogTheme);

                    View bottomSheetView = LayoutInflater.from(((MovieDetails)context).getApplicationContext())
                            .inflate(
                                    R.layout.bottom_sheet_cast,
                                    itemView.findViewById(R.id.bottom_const)
                            );
                    int positionCastAdapter = getAdapterPosition();

                    if (positionCastAdapter != RecyclerView.NO_POSITION) {

                        Cast castResult = castResultList.get(positionCastAdapter);

                        Intent intent = new Intent(context, CastDetails.class);
                        intent.putExtra("cast", castResult);
                        context.startActivity(intent);

                    }
                    bottomSheetDialog.setContentView(bottomSheetView);
                    bottomSheetDialog.show();
                    bottomSheetDialog.dismiss();

                }
            });
        }
    }

}
