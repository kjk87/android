package com.pplus.prnumberbiz.apps.post.ui;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.View;

import com.pple.pplus.utils.part.logs.LogUtil;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.post.data.PostImageEditPagerAdapter;

import java.util.ArrayList;

public class PostImageEditActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_post_image_edit;
    }

    private ArrayList<Uri> mCroppedImageList;
    private ViewPager pager;
    private PostImageEditPagerAdapter mAdapter;
    private int mSelectPosition;

    @Override
    public void initializeView(Bundle savedInstanceState){

        mCroppedImageList = getIntent().getParcelableArrayListExtra(Const.CROPPED_IMAGE);
        for(int i = 0; i < mCroppedImageList.size(); i++){
            LogUtil.e(LOG_TAG, mCroppedImageList.get(i).getPath());
        }

        pager = (ViewPager) findViewById(R.id.pager_image_edit);
        pager.setClipToPadding(false);
        mAdapter = new PostImageEditPagerAdapter(this, mCroppedImageList);
        pager.setAdapter(mAdapter);
        findViewById(R.id.text_post_image_edit_complete).setOnClickListener(this);
    }

    @Override
    protected void onNewIntent(Intent intent){

        super.onNewIntent(intent);
        mCroppedImageList = intent.getParcelableArrayListExtra(Const.CROPPED_IMAGE);
        mAdapter.setDataList(mCroppedImageList);

    }

    @Override
    public void onClick(View view){
        switch (view.getId()){
            case R.id.text_post_image_edit_complete:
                if(mCroppedImageList.size() > 0){
                    Intent data = new Intent();
                    data.putParcelableArrayListExtra(Const.CROPPED_IMAGE, mCroppedImageList);
                    setResult(RESULT_OK, data);
                    finish();
                }
                break;
        }
    }

    public void goFilter(int position, Uri uri){

        mSelectPosition = position;
        Intent intent = new Intent(this, ImageFilterActivity.class);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivityForResult(intent, Const.REQ_IMAGE_FILTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case Const.REQ_IMAGE_DELETE:
                    mCroppedImageList = data.getParcelableArrayListExtra(Const.CROPPED_IMAGE);
                    if(mCroppedImageList == null || mCroppedImageList.size() == 0){
                        finish();
                        return;
                    }
                    mAdapter = new PostImageEditPagerAdapter(this, mCroppedImageList);
                    pager.setAdapter(mAdapter);
                    break;
                case Const.REQ_IMAGE_FILTER:
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_select_photo), ToolbarOption.ToolbarMenu.LEFT);
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_delete));
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
                            Intent intent = new Intent(PostImageEditActivity.this, ImageDeleteActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.putParcelableArrayListExtra(Const.CROPPED_IMAGE, mAdapter.getDataList());
                            startActivityForResult(intent, Const.REQ_IMAGE_DELETE);
                        }
                        break;
                }
            }
        };

    }
}
