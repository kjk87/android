package com.pplus.prnumberbiz.apps.mobilegift.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pple.pplus.utils.part.format.FormatUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.core.network.model.dto.MobileGift;
import com.pplus.prnumberbiz.core.network.model.dto.MobileGiftPurchase;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

public class MobileGiftDetailActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_mobile_gift_detail;
    }

    private MobileGift mMobileGift;
    private int mCount;
    private MobileGiftPurchase mPurchase;
    private TextView text_count;

    @Override
    public void initializeView(Bundle savedInstanceState){

        mMobileGift = getIntent().getParcelableExtra(Const.DATA);
        ImageView image = (ImageView) findViewById(R.id.image_mobile_gift_detail);
        TextView text_name = (TextView) findViewById(R.id.text_mobile_gift_detail_name);
        TextView text_price = (TextView) findViewById(R.id.text_mobile_gift_detail_price);
        TextView text_use_area = (TextView) findViewById(R.id.text_mobile_gift_use_area);
        text_count = (TextView) findViewById(R.id.texT_mobile_gift_detail_count);
        findViewById(R.id.image_mobile_gift_detail_minus).setOnClickListener(this);
        findViewById(R.id.image_mobile_gift_detail_plus).setOnClickListener(this);
        TextView text_use_note = (TextView) findViewById(R.id.text_mobile_gift_detail_use_note);
        TextView text_use_limit = (TextView) findViewById(R.id.text_mobile_gift_detail_use_limit);
        findViewById(R.id.text_mobile_gift_detail_use_guide).setOnClickListener(this);
        findViewById(R.id.text_mobile_gift_detail_purchase).setOnClickListener(this);

        Glide.with(this).load(mMobileGift.getViewImage1()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image);
        text_name.setText(mMobileGift.getName());
        text_price.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_money_unit, FormatUtil.getMoneyType(String.valueOf(mMobileGift.getSalesPrice())))));
        text_use_area.setText(mMobileGift.getUseArea());
        mCount = 1;
        setCount();

        if(StringUtils.isEmpty(mMobileGift.getUseNote())) {
            findViewById(R.id.layout_mobile_gift_detail_use_note).setVisibility(View.GONE);
        } else {
            findViewById(R.id.layout_mobile_gift_detail_use_note).setVisibility(View.VISIBLE);
            text_use_note.setText(mMobileGift.getUseNote());
        }

        if(StringUtils.isEmpty(mMobileGift.getUseLimit())) {
            findViewById(R.id.layout_mobile_gift_detail_use_limit).setVisibility(View.GONE);
        } else {
            findViewById(R.id.layout_mobile_gift_detail_use_limit).setVisibility(View.VISIBLE);
            text_use_limit.setText(mMobileGift.getUseLimit());
        }

        mPurchase = new MobileGiftPurchase();
        mPurchase.setMobileGift(mMobileGift);
    }

    private void setCount(){

        text_count.setText("" + mCount);
    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.image_mobile_gift_detail_minus:
                if(mCount > 1) {
                    mCount--;
                    setCount();
                }
                break;
            case R.id.image_mobile_gift_detail_plus:
                mCount++;
                setCount();
                break;
            case R.id.text_mobile_gift_detail_use_guide:
                Intent intent = new Intent(this, MobileGiftGuideActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.text_mobile_gift_detail_purchase:
                mPurchase.setCountPerTarget(mCount);
                intent = new Intent(this, MobileGiftPurchaseActivity.class);
                intent.putExtra(Const.DATA, mPurchase);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
        }
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_product_detail), ToolbarOption.ToolbarMenu.LEFT);
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
                }
            }
        };
    }
}
