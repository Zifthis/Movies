package com.example.movies.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.movies.ui.toprated.TopRatedFragment;

public class LoadingView extends Animation {

    private Context context;
    private ProgressBar progressBar;
    private TextView textView;
    private float x;
    private float y;

    public LoadingView(Context context, ProgressBar progressBar, TextView textView, float x, float y) {
        this.context = context;
        this.progressBar = progressBar;
        this.textView = textView;
        this.x = x;
        this.y = y;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = x + (y - x) * interpolatedTime;
        progressBar.setProgress((int) value);
        textView.setText((int) value + " %");

        Intent i = new Intent(context, TopRatedFragment.class);

        if(value == y){
            context.startActivity(i);
        }
    }
}
