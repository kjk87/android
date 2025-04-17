package com.pplus.prnumberbiz.apps.guide.ui;

import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.pple.pplus.utils.part.pref.PreferenceUtil;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.common.ui.common.data.ViewPagerAdapter;
import com.pplus.prnumberbiz.apps.common.ui.custom.DirectionIndicator;

import java.util.ArrayList;
import java.util.List;

public class SignUpGuideActivity extends BaseActivity{

    private ViewPager pager;
    private List<Integer> mImageResource = new ArrayList<>();
    private ViewPagerAdapter mTurorialAdapter;
    private String key;
    private DirectionIndicator indicator;

    @Override
    public String getPID(){

        return "";
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_guide;
    }

    @Override
    public void initializeView(Bundle savedInstanceState){

//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) findViewById(R.id.pager).getLayoutParams();
//        params.setMargins(0, ResourceUtil.getStatusBarHeight(this), 0, 0);

        key = getIntent().getStringExtra(Const.KEY);
        pager = (ViewPager) findViewById(R.id.guide_pager);

        indicator = (DirectionIndicator)findViewById(R.id.indicator_guide);
        indicator.setImageResId(R.drawable.guide_indi);
        indicator.setVisibility(View.VISIBLE);

        if(key.equals(Const.GUIDE1)) {
            indicator.setVisibility(View.GONE);
            mImageResource.add(R.drawable.img_prshop_guide_1);
        } else if(key.equals(Const.GUIDE_PERSON_ADDRESS)) {
            indicator.setVisibility(View.GONE);
            mImageResource.add(R.drawable.img_prshop_guide_2_2);
        }else if(key.equals(Const.GUIDE_STORE_ADDRESS)){
            indicator.setVisibility(View.GONE);
            mImageResource.add(R.drawable.img_prshop_guide_2_1);
        } else if(key.equals(Const.GUIDE3)) {
            indicator.setVisibility(View.GONE);
            mImageResource.add(R.drawable.img_prshop_guide_3);
        } else if(key.equals(Const.GUIDE4)) {
            mImageResource.add(R.drawable.img_prshop_guide_4_1);
            mImageResource.add(R.drawable.img_prshop_guide_4_2);
        } else if(key.equals(Const.GUIDE5)) {
            indicator.setVisibility(View.GONE);
            mImageResource.add(R.drawable.img_prshop_guide_5);
        } else if(key.equals(Const.GUIDE6)) {
            indicator.setVisibility(View.GONE);
            mImageResource.add(R.drawable.img_prshop_guide_6);
        }else if(key.equals(Const.GUIDE_PERSON_ALL)) {
            mImageResource.add(R.drawable.img_prshop_guide_1);
            mImageResource.add(R.drawable.img_prshop_guide_2_2);
            mImageResource.add(R.drawable.img_prshop_guide_3);
            mImageResource.add(R.drawable.img_prshop_guide_4_2);
            mImageResource.add(R.drawable.img_prshop_guide_5);
            mImageResource.add(R.drawable.img_prshop_guide_6);
        }else if(key.equals(Const.GUIDE_STORE_ALL)) {
            mImageResource.add(R.drawable.img_prshop_guide_1);
            mImageResource.add(R.drawable.img_prshop_guide_2_1);
            mImageResource.add(R.drawable.img_prshop_guide_3);
            mImageResource.add(R.drawable.img_prshop_guide_4_1);
            mImageResource.add(R.drawable.img_prshop_guide_5);
            mImageResource.add(R.drawable.img_prshop_guide_6);
        }

        indicator.build(LinearLayout.HORIZONTAL, mImageResource.size());

        mTurorialAdapter = new ViewPagerAdapter(this, mImageResource);
        pager.setAdapter(mTurorialAdapter);
        pager.setOffscreenPageLimit(mImageResource.size());

        if(!key.equals(Const.GUIDE_PERSON_ALL)) {
            PreferenceUtil.getDefaultPreference(SignUpGuideActivity.this).put(key, true);
            findViewById(R.id.image_guide_close).setVisibility(View.GONE);
            mTurorialAdapter.setOnItemClickListener(new ViewPagerAdapter.OnItemClickListener(){

                @Override
                public void onItemClick(int position){
                    onBackPressed();
                }
            });
        }

        ImageView image_close = (ImageView)findViewById(R.id.image_guide_close);
        image_close.setImageResource(R.drawable.ic_top_close_white);
        findViewById(R.id.layout_guide).setBackgroundResource(R.color.color_e5000000);

        image_close.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                onBackPressed();
            }
        });

        findViewById(R.id.text_guide_confirm).setVisibility(View.GONE);

        findViewById(R.id.image_guide_close).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                onBackPressed();
            }
        });

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){

            }

            @Override
            public void onPageSelected(int position){

                indicator.setCurrentItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state){

            }
        });


    }

}
