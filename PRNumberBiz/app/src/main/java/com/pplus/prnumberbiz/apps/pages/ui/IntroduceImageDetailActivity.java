package com.pplus.prnumberbiz.apps.pages.ui;

import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.pple.pplus.utils.part.logs.LogUtil;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.common.ui.custom.ImageViewTouchViewPager;
import com.pplus.prnumberbiz.apps.post.data.ZoomablePageAdapter;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Attachment;
import com.pplus.prnumberbiz.core.network.model.dto.ImgUrl;
import com.pplus.prnumberbiz.core.network.model.dto.Page;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsIntroImage;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

/**
 * Created by KIMSOONHO on 2017-02-01.
 */

public class IntroduceImageDetailActivity extends BaseActivity{

    private ImageViewTouchViewPager mViewPager;
    private ZoomablePageAdapter mGalleyViewPageAdapter;
    private int mSelectedPosition;
    private List<Attachment> mImageList = null;
    List<ImgUrl> introImageList;

    private TextView text_paging;
    private ImageView image_guide_close;
    private RelativeLayout rl_top;

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_introduce_image_detail;
    }

    @Override
    public void initializeView(Bundle savedInstanceState){

        mSelectedPosition = getIntent().getIntExtra(Const.POSITION, 0);

        mImageList = getIntent().getParcelableArrayListExtra(Const.DATA);
        LogUtil.e(LOG_TAG, "size {}", mImageList.size());

        image_guide_close = (ImageView) findViewById(R.id.image_introduce_image_detail_close);
        image_guide_close.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                finish();
            }
        });

        text_paging = (TextView) findViewById(R.id.text_introduce_image_detail_paging);

        rl_top = (RelativeLayout) findViewById(R.id.layout_introduce_image_top);

        if(mImageList != null && mImageList.size() > 0) {
            text_paging.setText(mSelectedPosition + 1 + "/" + mImageList.size());

            mViewPager = (ImageViewTouchViewPager) findViewById(R.id.pager_introduce_image_detail);
            mGalleyViewPageAdapter = new ZoomablePageAdapter(this, mImageList);
            mGalleyViewPageAdapter.setOnSingleTapListener(new ZoomablePageAdapter.onSingleTapListener(){

                @Override
                public void onSingleTap(){

                    handleOnImageView(null);
                }
            });

            mViewPager.setAdapter(mGalleyViewPageAdapter);
            mViewPager.setCurrentItem(mSelectedPosition, false);

            mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){

                }

                @Override
                public void onPageSelected(int position){

                    text_paging.setText(position + 1 + "/" + mImageList.size());
                }

                @Override
                public void onPageScrollStateChanged(int state){

                }
            });

        }

        findViewById(R.id.text_introduce_image_represent).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                Attachment attachment = mGalleyViewPageAdapter.getData(mViewPager.getCurrentItem());
                mImageList.remove(mViewPager.getCurrentItem());
                introImageList = new ArrayList<ImgUrl>();
                introImageList.add(new ImgUrl(attachment.getNo(), Const.IMAGE_UPLOAD_MAX_COUNT));
                copyAttachment(attachment.getNo());
            }
        });

    }

    private void copyAttachment(Long no){

        Map<String, String> params = new HashMap<String, String>();
        params.put("no", "" + no);
        showProgress("");
        ApiBuilder.create().copyAttachment(params).setCallback(new PplusCallback<NewResultResponse<Attachment>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Attachment>> call, NewResultResponse<Attachment> response){

                hideProgress();
                updateProfile(response.getData().getNo());
            }

            @Override
            public void onFailure(Call<NewResultResponse<Attachment>> call, Throwable t, NewResultResponse<Attachment> response){

                hideProgress();
            }
        }).build().call();
    }

    private void updateProfile(Long no){

        Map<String, String> params = new HashMap<>();
        params.put("no", "" + LoginInfoManager.getInstance().getUser().getPage().getNo());
        params.put("profileImage.no", "" + no);
        showProgress("");
        ApiBuilder.create().updatePageProfileImage(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){

                setPriority();
                hideProgress();
            }

            @Override
            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){

                hideProgress();
            }
        }).build().call();
    }

    private void setPriority(){

        for(int i = 0; i < mImageList.size(); i++) {
            int priority = Const.IMAGE_UPLOAD_MAX_COUNT - (i+1);
            introImageList.add(new ImgUrl(mImageList.get(i).getNo(), priority));
        }

        ParamsIntroImage params = new ParamsIntroImage();
        params.setNo(LoginInfoManager.getInstance().getUser().getPage().getNo());
        params.setIntroImageList(introImageList);

        ApiBuilder.create().updateIntroImageList(params).setCallback(new PplusCallback<NewResultResponse<Page>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Page>> call, NewResultResponse<Page> response){

                showAlert(R.string.msg_complete_setting);
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

    public void handleOnImageView(View view){

        if(rl_top != null && rl_top.getVisibility() == View.VISIBLE) {
            rl_top.setVisibility(View.GONE);
        } else {
            rl_top.setVisibility(View.VISIBLE);
        }
    }
}
