package com.example.movies.details;

import android.graphics.Rect;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CastDeco extends RecyclerView.ItemDecoration {

    private int space;

    public CastDeco(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, int itemPosition, @NonNull RecyclerView parent) {
        outRect.right = space;
        outRect.left = space;
    }
}
