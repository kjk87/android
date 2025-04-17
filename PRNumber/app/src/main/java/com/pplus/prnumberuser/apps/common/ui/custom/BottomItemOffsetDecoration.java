package com.pplus.prnumberuser.apps.common.ui.custom;

import android.content.Context;
import android.graphics.Rect;
import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

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
