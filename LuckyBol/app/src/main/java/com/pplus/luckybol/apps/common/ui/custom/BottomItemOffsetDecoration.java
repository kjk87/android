package com.pplus.luckybol.apps.common.ui.custom;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by imac on 2017. 7. 27..
 */

public class BottomItemOffsetDecoration extends RecyclerView.ItemDecoration{
    private int mItemOffset;

    public BottomItemOffsetDecoration(int itemOffset){

        mItemOffset = itemOffset;
    }

    public BottomItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId){

        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){

        super.getItemOffsets(outRect, view, parent, state);

        outRect.set(0, 0, 0, mItemOffset);

    }
}
