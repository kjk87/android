package com.pplus.prnumberbiz.apps.push.ui;

import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.core.network.model.dto.Msg;
import com.pplus.prnumberbiz.core.network.model.dto.Page;

public class PushPreviewActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_push_preview;
    }

    private View layout_androidTab, layout_iphoneTab, layout_iphone, layout_android, image_phone_top;

    @Override
    public void initializeView(Bundle savedInstanceState){

        layout_androidTab = findViewById(R.id.layout_push_preview_androidTab);
        layout_iphoneTab = findViewById(R.id.layout_push_preview_iphoneTab);
        layout_androidTab.setOnClickListener(this);
        layout_iphoneTab.setOnClickListener(this);

        layout_android = findViewById(R.id.layout_push_preview_android);
        layout_iphone = findViewById(R.id.layout_push_preview_iphone);

        layout_android.setVisibility(View.VISIBLE);
        layout_iphone.setVisibility(View.GONE);
        setSelect(layout_androidTab, layout_iphoneTab);
        image_phone_top = findViewById(R.id.image_push_review_phone_top);

        Msg msg = getIntent().getParcelableExtra(Const.DATA);

        TextView text_title = (TextView)findViewById(R.id.text_push_preview_android_title);
        text_title.setText(msg.getSubject());
        TextView text_android_contents = (TextView)findViewById(R.id.text_push_preview_android_contents);
        TextView text_ios_contents = (TextView)findViewById(R.id.text_push_preview_iphone_contents);
        text_android_contents.setText(msg.getContents());
        text_ios_contents.setText(msg.getContents());

        Page page = LoginInfoManager.getInstance().getUser().getPage();
        ImageView image_android = (ImageView)findViewById(R.id.image_push_preview_android);

        if(page.getProfileImage() != null && StringUtils.isNotEmpty(page.getProfileImage().getUrl())) {
            Glide.with(this).load(page.getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_android);
        }
    }

    private void setSelect(View view1, View view2){

        view1.setSelected(true);
        view2.setSelected(false);
    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.layout_push_preview_androidTab:
                layout_android.setVisibility(View.VISIBLE);
                layout_iphone.setVisibility(View.GONE);
                setSelect(layout_androidTab, layout_iphoneTab);
                image_phone_top.setBackgroundResource(R.drawable.img_mylist_android_bg_top);
                break;
            case R.id.layout_push_preview_iphoneTab:
                layout_android.setVisibility(View.GONE);
                layout_iphone.setVisibility(View.VISIBLE);
                setSelect(layout_iphoneTab, layout_androidTab);
                image_phone_top.setBackgroundResource(R.drawable.img_mylist_iphone_bg_top);
                break;
        }
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_push_send), ToolbarOption.ToolbarMenu.RIGHT);
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
