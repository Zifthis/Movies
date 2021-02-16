package com.example.movies.slider;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.movies.R;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;


public class SliderAdapter extends RecyclerView.Adapter<SliderAdapter.SliderViewHolder> {

    private List<SliderConstructor> sliderConstructorList;
    private ViewPager2 viewPager2;

    public SliderAdapter(List<SliderConstructor> sliderConstructorList, ViewPager2 viewPager2) {
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
        holder.setImage(sliderConstructorList.get(position));
        if(position == sliderConstructorList.size()-2){
            viewPager2.post(runnable);
        }
    }

    @Override
    public int getItemCount() {
        return sliderConstructorList.size();
    }

    class SliderViewHolder extends RecyclerView.ViewHolder {
        private RoundedImageView imageView;

        SliderViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageSlide);
        }

        void setImage(SliderConstructor sliderConstructor) {


            imageView.setImageResource(sliderConstructor.getImage());
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
