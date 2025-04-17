package com.pplus.prnumberbiz.apps.guide.ui;

import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.pple.pplus.utils.part.apps.resource.ResourceUtil;
import com.pple.pplus.utils.part.pref.PreferenceUtil;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.common.ui.common.data.ViewPagerAdapter;
import com.pplus.prnumberbiz.apps.common.ui.custom.DirectionIndicator;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity{

    private ViewPager pager;
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

        return R.layout.activity_guide;
    }

    @Override
    public void initializeView(Bundle savedInstanceState){

//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) findViewById(R.id.pager).getLayoutParams();
//        params.setMargins(0, ResourceUtil.getStatusBarHeight(this), 0, 0);

        pager = (ViewPager) findViewById(R.id.guide_pager);

        indicator = (DirectionIndicator)findViewById(R.id.indicator_guide);
//        indicator.setImageResId(R.drawable.guide_indi);
        indicator.setVisibility(View.VISIBLE);

        final String key = getIntent().getStringExtra(Const.KEY);

        if(key.equals(Const.GUIDE_BOL)) {
            indicator.setVisibility(View.GONE);
            mImageResource.add(R.drawable.img_menu_popup_guide_1);
        }else if(key.equals(Const.GUIDE_PUSH)){
            indicator.setVisibility(View.GONE);
            mImageResource.add(R.drawable.img_plus_popup_guide_1);
        }else if(key.equals(Const.GUIDE_SMS)){
            indicator.setVisibility(View.GONE);
            mImageResource.add(R.drawable.img_main_popup_guide_5);
        }else if(key.equals(Const.GUIDE_CUSTOMER)){
            mImageResource.add(R.drawable.img_main_popup_guide_1);
            mImageResource.add(R.drawable.img_main_popup_guide_2);
        }else if(key.equals(Const.GUIDE_PLUS)){
            mImageResource.add(R.drawable.img_main_popup_guide_3);
            mImageResource.add(R.drawable.img_main_popup_guide_4);
        }else if(key.equals(Const.GUIDE_PRIVACY)){
            indicator.setVisibility(View.GONE);
            mImageResource.add(R.drawable.img_shop_popup_guide_1);
        }


        indicator.build(LinearLayout.HORIZONTAL, mImageResource.size());

        mTurorialAdapter = new ViewPagerAdapter(this, mImageResource);
        pager.setAdapter(mTurorialAdapter);
        pager.setOffscreenPageLimit(mImageResource.size());

        ImageView image_close = (ImageView)findViewById(R.id.image_guide_close);
        text_confirm = (TextView)findViewById(R.id.text_guide_confirm);
        text_confirm.setVisibility(View.VISIBLE);


        if(key.equals(Const.GUIDE_PRIVACY)){
            image_close.setImageResource(R.drawable.ic_top_close_white);
            findViewById(R.id.layout_guide).setBackgroundResource(R.color.black);
            text_confirm.setBackgroundResource(R.color.white);
            text_confirm.setTextColor(ResourceUtil.getColor(this, R.color.color_000000_a3a3a3));
        }else{
            image_close.setImageResource(R.drawable.ic_top_close);
            CheckBox check_guide = (CheckBox)findViewById(R.id.check_guide);
            check_guide.setVisibility(View.VISIBLE);
            check_guide.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b){
                    PreferenceUtil.getDefaultPreference(GuideActivity.this).put(key, b);
                }
            });
            findViewById(R.id.layout_guide).setBackgroundResource(R.color.white);
            text_confirm.setBackgroundResource(R.color.black);
            text_confirm.setTextColor(ResourceUtil.getColor(this, R.color.color_ffffff_a3a3a3));
        }

        text_confirm.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                finish();
            }
        });

        image_close.setOnClickListener(new View.OnClickListener(){

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
