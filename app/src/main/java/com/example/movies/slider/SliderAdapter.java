package com.example.movies.slider;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;
import com.bumptech.glide.Glide;
import com.example.movies.R;
import com.example.movies.model.Result;
import com.makeramen.roundedimageview.RoundedImageView;
import java.util.List;




public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private Context context;
    private List<Result> sliderConstructorList;
    private ViewPager2 viewPager2;


    public SliderAdapter(Context context, List<Result> sliderConstructorList, ViewPager2 viewPager2) {
        this.context = context;
        this.sliderConstructorList = sliderConstructorList;
        this.viewPager2 = viewPager2;
    }

    @NonNull
    @Override
    public SliderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SliderViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item,
                        parent, false)
        );
    }

    @Override
    public void onBindViewHolder(@NonNull SliderViewHolder holder, int position) {


        String imagePath = "https://image.tmdb.org/t/p/w400" + sliderConstructorList.get(position).getBackdropPath();
        Glide.with(context)
                .load(imagePath)
                .placeholder(R.drawable.placeholder)
                .into(holder.imageView);
        holder.textView.setText(sliderConstructorList.get(position).getTitle());

        if (position == sliderConstructorList.size() - 2) {
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderConstructorList.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageView;
        private TextView textView;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
            textView = itemView.findViewById(R.id.slicer_title);
        }

    }

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            sliderConstructorList.addAll(sliderConstructorList);
            notifyDataSetChanged();
        }
    };

}
