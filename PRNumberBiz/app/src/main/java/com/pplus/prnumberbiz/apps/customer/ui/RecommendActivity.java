package com.pplus.prnumberbiz.apps.customer.ui;

import android.content.Intent;
import android.net.Uri;
import android.provider.Telephony;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pple.pplus.utils.part.info.OsUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.mgmt.CountryConfigManager;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;

public class RecommendActivity extends BaseActivity implements ImplToolbar{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_recommend;
    }

    private String mMobileNumber;

    @Override
    public void initializeView(Bundle savedInstanceState){

        mMobileNumber = getIntent().getStringExtra(Const.MOBILE_NUMBER);
        TextView text_recommend = (TextView) findViewById(R.id.text_recommend_code);
        text_recommend.setText("" + LoginInfoManager.getInstance().getUser().getRecommendKey());

        TextView text_cash = (TextView)findViewById(R.id.text_recommend_cash);
        text_cash.setText(getString(R.string.format_msg_recommend_description2, CountryConfigManager.getInstance().getConfig().getProperties().getRecommendBol()));

        findViewById(R.id.layout_recommend_sms).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                String smsBody = getString(R.string.msg_invite_recommend);

                if(OsUtil.isKitkat()) //At least KitKat
                {
                    String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(RecommendActivity.this); //Need to change the build to API 19

                    Intent sendIntent = new Intent(Intent.ACTION_SEND);
                    sendIntent.setType("text/plain");
                    if(StringUtils.isNotEmpty(mMobileNumber)) {
                        sendIntent.putExtra("address", mMobileNumber);
                    }

                    sendIntent.putExtra(Intent.EXTRA_TEXT, smsBody);

                    if(defaultSmsPackageName != null)//Can be null in case that there is no default, then the user would be able to choose any app that support this intent.
                    {
                        sendIntent.setPackage(defaultSmsPackageName);
                    }
                    startActivity(sendIntent);

                } else //For early versions, do what worked for you before.
                {
                    Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                    sendIntent.setData(Uri.parse("sms:"));
                    if(StringUtils.isNotEmpty(mMobileNumber)) {
                        sendIntent.putExtra("address", mMobileNumber);
                    }
                    sendIntent.putExtra("sms_body", smsBody);
                    startActivity(sendIntent);
                }
            }
        });

        findViewById(R.id.layout_recommend_kakao).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

//                KakaoLinkUtil.getInstance(RecommendActivity.this).sendKakaoApp(getString(R.string.msg_invite_recommend), "", "");
            }
        });
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_recommend), ToolbarOption.ToolbarMenu.RIGHT);
        return toolbarOption;
    }

    @Override
    public OnToolbarListener getOnToolbarClickListener(){

        return new OnToolbarListener(){

            @Override
            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag){

                switch (toolbarMenu) {
                    case RIGHT:
                        if(tag.equals(1)) {
                            onBackPressed();
                        }
                        break;
                }
            }
        };

    }
}
