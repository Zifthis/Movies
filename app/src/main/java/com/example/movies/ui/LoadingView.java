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

    private final Context context;
    private final ProgressBar progressBar;

    private final float x;
    private final float y;

    public LoadingView(Context context, ProgressBar progressBar, float x, float y) {
        this.context = context;
        this.progressBar = progressBar;
        this.x = x;
        this.y = y;
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        super.applyTransformation(interpolatedTime, t);
        float value = x + (y - x) * interpolatedTime;
        progressBar.setProgress((int) value);
        Intent i = new Intent(context, TopRatedFragment.class);
        if (value == y) {
            context.startActivity(i);
        }
    }
}
