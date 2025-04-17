package com.pplus.prnumberbiz.apps.pages.data;

import android.content.Context;
import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.pplus.prnumberbiz.R;


/**
 * Created by Windows7-00 on 2016-12-23.
 */

public class ListMarginDecoration extends RecyclerView.ItemDecoration {

    int spac;

    public ListMarginDecoration(Context context){
        spac = context.getResources().getDimensionPixelSize(R.dimen.height_36);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){

        super.getItemOffsets(outRect, view, parent, state);

        outRect.bottom = spac;

    }
}
