package com.pplus.prnumberbiz.apps.guide.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pple.pplus.utils.part.pref.PreferenceUtil;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.common.ui.common.data.ViewPagerAdapter;
import com.pplus.prnumberbiz.apps.common.ui.custom.DirectionIndicator;
import com.pplus.prnumberbiz.apps.common.ui.custom.autoscrollviewpager.AutoScrollViewPager;
import com.pplus.prnumberbiz.apps.marketing.ui.SnsSyncActivity;

import java.util.ArrayList;
import java.util.List;

public class SnsGuideActivity extends BaseActivity{

    private AutoScrollViewPager pager;
    private List<Integer> mImageResource = new ArrayList<>();
    private ViewPagerAdapter mTurorialAdapter;
    private DirectionIndicator indicator;
    private TextView text_confirm;

    @Override
    public String getPID(){

        return "";
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_sns_guide;
    }

    @Override
    public void initializeView(Bundle savedInstanceState){

//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) findViewById(R.id.pager).getLayoutParams();
//        params.setMargins(0, ResourceUtil.getStatusBarHeight(this), 0, 0);

        pager = (AutoScrollViewPager) findViewById(R.id.page_sns_guide);

        indicator = (DirectionIndicator)findViewById(R.id.indicator_sns_guide);
//        indicator.setImageResId(R.drawable.guide_indi);
        indicator.setVisibility(View.VISIBLE);

        mImageResource.add(R.drawable.img_menu_popup_guide_2);
        mImageResource.add(R.drawable.img_menu_popup_guide_3);
        mImageResource.add(R.drawable.img_menu_popup_guide_4);
        mImageResource.add(R.drawable.img_menu_popup_guide_5);


        indicator.build(LinearLayout.HORIZONTAL, mImageResource.size());
        pager.setInterval(3000);
        pager.setCycle(false);
        pager.setOffscreenPageLimit(mImageResource.size());
        pager.startAutoScroll();

        mTurorialAdapter = new ViewPagerAdapter(this, mImageResource);
        pager.setAdapter(mTurorialAdapter);
        pager.setOffscreenPageLimit(mImageResource.size());

        ImageView image_close = (ImageView)findViewById(R.id.image_sns_guide_close);
        image_close.setImageResource(R.drawable.ic_top_close);

        image_close.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                AlertBuilder.Builder builder = new AlertBuilder.Builder();
                builder.setTitle(getString(R.string.word_notice_alert));
                builder.addContents(new AlertData.MessageData(getString(R.string.msg_sns_guide_close_description1), AlertBuilder.MESSAGE_TYPE.TEXT, 3));
                builder.addContents(new AlertData.MessageData(getString(R.string.msg_sns_guide_close_description2), AlertBuilder.MESSAGE_TYPE.TEXT, 3));
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
                builder.setOnAlertResultListener(new OnAlertResultListener(){

                    @Override
                    public void onCancel(){

                    }

                    @Override
                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){
                        switch (event_alert){
                            case RIGHT:
                                finish();
                                break;
                        }
                    }
                }).builder().show(SnsGuideActivity.this);
            }
        });

        text_confirm = (TextView)findViewById(R.id.text_sns_guide_confirm);
        text_confirm.setVisibility(View.GONE);
        text_confirm.setText(R.string.msg_free_trial);
        text_confirm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                PreferenceUtil.getDefaultPreference(SnsGuideActivity.this).put(Const.GUIDE_SNS, true);
                Intent intent = new Intent(SnsGuideActivity.this, SnsSyncActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){

            }

            @Override
            public void onPageSelected(int position){

                indicator.setCurrentItem(position);
                if(position < (mImageResource.size()-1)){
                    text_confirm.setVisibility(View.GONE);
                }else{
                    text_confirm.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state){

            }
        });


    }

}
