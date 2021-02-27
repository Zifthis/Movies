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
import com.example.movies.model.Cast;

import org.w3c.dom.Text;

import java.util.List;

public class CastAdapter extends RecyclerView.Adapter<CastAdapter.CastViewHolder> {

    private List<Cast> castResultList;
    private Context context;

    public CastAdapter(List<Cast> castResultList, Context context) {
        this.castResultList = castResultList;
        this.context = context;
    }

    @Override
    public CastViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cast_items,parent,false);
        CastViewHolder castViewHolder = new CastViewHolder(view);
        return castViewHolder;
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

    public static class CastViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView textView;

        public CastViewHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.cast_img);
            textView = itemView.findViewById(R.id.cast_name);
        }
    }

}
