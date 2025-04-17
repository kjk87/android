package com.pplus.prnumberbiz.apps.post.ui;

import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.post.data.ImageDeleteAdapter;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.util.ArrayList;

public class ImageDeleteActivity extends BaseActivity implements ImplToolbar{

    @Override
    public String getPID(){

        return null;
    }


    @Override
    public int getLayoutRes(){

        return R.layout.activity_image_delete;
    }

    private ArrayList<Uri> mCroppedImageList;
    private ImageDeleteAdapter mAdapter;

    @Override
    public void initializeView(Bundle savedInstanceState){

        mCroppedImageList = getIntent().getParcelableArrayListExtra(Const.CROPPED_IMAGE);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_image_delete);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.width_40)));
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new ImageDeleteAdapter(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setDataList(mCroppedImageList);
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_delete_image), ToolbarOption.ToolbarMenu.LEFT);
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_complete));
        return toolbarOption;
    }

    @Override
    public OnToolbarListener getOnToolbarClickListener(){

        return new OnToolbarListener(){

            @Override
            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag){

                switch (toolbarMenu) {
                    case LEFT:
                        if(tag.equals(1)) {
                            onBackPressed();
                        }
                        break;
                    case RIGHT:
                        if(tag.equals(1)) {

                            if(mAdapter.getSelectGalleryList() == null || mAdapter.getSelectGalleryList().size() == 0){
                                showAlert(R.string.msg_select_delete_image);
                                return;
                            }

                            for(Uri uri : mAdapter.getSelectGalleryList()){
                                if(mCroppedImageList.contains(uri)){
                                    mCroppedImageList.remove(uri);
                                    PplusCommonUtil.Companion.deleteFromMediaScanner(uri.getPath());
                                }
                            }
                            Intent data = new Intent();
                            data.putParcelableArrayListExtra(Const.CROPPED_IMAGE, mCroppedImageList);
                            setResult(RESULT_OK, data);
                            finish();
                        }
                        break;
                }
            }
        };

    }

    class SpacesItemDecoration extends RecyclerView.ItemDecoration{

        private int space;

        public SpacesItemDecoration(int space){

            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state){

            int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
            if(itemPosition % 2 == 1) {
                outRect.right = 0;
            } else {
                outRect.right = space;
            }

            outRect.bottom = space;
            // Add top margin only for the first item to avoid double space between items
        }
    }
}
