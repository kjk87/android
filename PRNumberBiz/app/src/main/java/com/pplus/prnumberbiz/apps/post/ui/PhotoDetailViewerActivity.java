package com.pplus.prnumberbiz.apps.post.ui;

import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.common.ui.custom.ImageViewTouchViewPager;
import com.pplus.prnumberbiz.apps.post.data.ZoomablePageAdapter;
import com.pplus.prnumberbiz.core.network.model.dto.Attachment;

import java.util.List;

/**
 * Created by KIMSOONHO on 2017-02-01.
 */

public class PhotoDetailViewerActivity extends BaseActivity{

    private ImageViewTouchViewPager mViewPager;
    private ZoomablePageAdapter mPhotoDetailViewPageAdapter;
    private int mSelectedPosition;
    private List<Attachment> photoList = null;

    private TextView toolbar_title;
    private ImageView image_close;
    private RelativeLayout rl_top;

    @Override
    public String getPID() {
        return null;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.activity_photo_detail_viewer;
    }

    @Override
    public void initializeView(Bundle savedInstanceState) {
        mSelectedPosition = getIntent().getIntExtra(Const.POSITION, 0);

        photoList = getIntent().getParcelableArrayListExtra(Const.DATA);

        image_close = (ImageView)findViewById(R.id.image_photo_detail_close);
        image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar_title = (TextView)findViewById(R.id.toolbar_title);
        toolbar_title.setSingleLine();
        rl_top = (RelativeLayout) findViewById(R.id.rl_top);

        if(photoList != null && photoList.size() > 0) {
            toolbar_title.setText(mSelectedPosition+1 + "/" + photoList.size());

            mViewPager = (ImageViewTouchViewPager)findViewById(R.id.pager);
            mPhotoDetailViewPageAdapter = new ZoomablePageAdapter(this, photoList);
            mPhotoDetailViewPageAdapter.setOnSingleTapListener(new ZoomablePageAdapter.onSingleTapListener() {
                @Override
                public void onSingleTap() {
                    handleOnImageView(null);
                }
            });

            mViewPager.setAdapter(mPhotoDetailViewPageAdapter);
            mViewPager.setCurrentItem(mSelectedPosition, false);

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    toolbar_title.setText(position+1 + "/" + photoList.size());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }


    }

    public void handleOnImageView(View view){
        if(rl_top != null && rl_top.getVisibility() == View.VISIBLE) {
            rl_top.setVisibility(View.GONE);
        }
        else {
            rl_top.setVisibility(View.VISIBLE);
        }
    }
}
