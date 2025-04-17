package com.pplus.prnumberbiz.apps.pages.ui;

import android.graphics.Rect;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.pages.data.IntroImageDeleteAdapter;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Attachment;
import com.pplus.prnumberbiz.core.network.model.dto.ImgUrl;
import com.pplus.prnumberbiz.core.network.model.dto.Page;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsIntroImage;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import java.util.ArrayList;

import network.common.PplusCallback;
import retrofit2.Call;

public class IntroImageDeleteActivity extends BaseActivity implements ImplToolbar{

    @Override
    public String getPID(){

        return null;
    }


    @Override
    public int getLayoutRes(){

        return R.layout.activity_image_delete;
    }

    private ArrayList<Attachment> mAttachedImageList;
    private IntroImageDeleteAdapter mAdapter;

    @Override
    public void initializeView(Bundle savedInstanceState){

        mAttachedImageList = getIntent().getParcelableArrayListExtra(Const.DATA);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_image_delete);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.addItemDecoration(new SpacesItemDecoration(getResources().getDimensionPixelSize(R.dimen.width_40)));
        recyclerView.setLayoutManager(layoutManager);

        mAdapter = new IntroImageDeleteAdapter(this);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setDataList(mAttachedImageList);
    }

    private void updateIntroImage(){

        ArrayList<ImgUrl> imgList = new ArrayList<>();
        for(int i = 0; i < mAttachedImageList.size(); i++) {
            imgList.add(new ImgUrl(mAttachedImageList.get(i).getNo(), Const.IMAGE_UPLOAD_MAX_COUNT - i));
        }

        ParamsIntroImage params = new ParamsIntroImage();
        params.setNo(LoginInfoManager.getInstance().getUser().getPage().getNo());
        params.setIntroImageList(imgList);
        showProgress("");
        ApiBuilder.create().updateIntroImageList(params).setCallback(new PplusCallback<NewResultResponse<Page>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Page>> call, NewResultResponse<Page> response){

                showAlert(R.string.msg_delete_complete);
                hideProgress();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<NewResultResponse<Page>> call, Throwable t, NewResultResponse<Page> response){

                hideProgress();
            }
        }).build().call();
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

                            for(Attachment uri : mAdapter.getSelectGalleryList()) {
                                if(mAttachedImageList.contains(uri)) {
                                    mAttachedImageList.remove(uri);
                                }
                            }
                            updateIntroImage();
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
