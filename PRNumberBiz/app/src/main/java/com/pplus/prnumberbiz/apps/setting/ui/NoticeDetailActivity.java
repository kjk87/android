package com.pplus.prnumberbiz.apps.setting.ui;

import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Faq;
import com.pplus.prnumberbiz.core.network.model.dto.Notice;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import java.util.HashMap;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class NoticeDetailActivity extends BaseActivity implements ImplToolbar{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_notice_detail;
    }

    private Notice mNotice;
    private Faq mFaq;

    @Override
    public void initializeView(Bundle savedInstanceState){

        mNotice = getIntent().getParcelableExtra(Const.NOTICE);
        mFaq = getIntent().getParcelableExtra(Const.FAQ);


        final WebView webView = (WebView) this.findViewById(R.id.webview_notice_detail);
        WebSettings settings = webView.getSettings();
        settings.setDefaultTextEncodingName("utf-8");

        if(mNotice != null) {
            setTitle(mNotice.getSubject());
            Map<String, String> params = new HashMap<>();
            params.put("no", "" + mNotice.getNo());

            ApiBuilder.create().getNotice(params).setCallback(new PplusCallback<NewResultResponse<Notice>>(){

                @Override
                public void onResponse(Call<NewResultResponse<Notice>> call, NewResultResponse<Notice> response){

                    webView.loadDataWithBaseURL("",response.getData().getContents(), "text/html; charset=utf-8", "utf-8", "");
                }

                @Override
                public void onFailure(Call<NewResultResponse<Notice>> call, Throwable t, NewResultResponse<Notice> response){

                }
            }).build().call();
        } else if(mFaq != null) {
            setTitle(mFaq.getSubject());

            Map<String, String> params = new HashMap<>();
            params.put("no", "" + mFaq.getNo());

            ApiBuilder.create().getFaq(params).setCallback(new PplusCallback<NewResultResponse<Faq>>(){

                @Override
                public void onResponse(Call<NewResultResponse<Faq>> call, NewResultResponse<Faq> response){

                    webView.loadDataWithBaseURL("",response.getData().getContents(), "text/html; charset=utf-8", "utf-8", "");
                }

                @Override
                public void onFailure(Call<NewResultResponse<Faq>> call, Throwable t, NewResultResponse<Faq> response){

                }
            }).build().call();
        }
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_notice), ToolbarOption.ToolbarMenu.LEFT);
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
