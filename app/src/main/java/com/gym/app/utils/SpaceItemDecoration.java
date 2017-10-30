package com.gym.app.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

/**
 * @author Paul
 * @since 2017.10.25
 */

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    @Override
    public void getItemOffsets(Rect outRect, final View view, final RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        if (spanIndex == 0) {
            outRect.left = 15;
            outRect.right = 7;
        } else {//if you just have 2 span . Or you can use (staggeredGridLayoutManager.getSpanCount()-1) as last span
            outRect.left = 7;
            outRect.right = 15;
        }
        outRect.bottom = 30;
        // Add top margin only for the first item to avoid double space between items
        int position = parent.getChildAdapterPosition(view);
        if (position == 0 || position == 1) {
            outRect.top = 15;
        }
    }
}