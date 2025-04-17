//package com.pplus.prnumberbiz.apps.pages.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.core.code.common.SnsTypeCode;
//
//
//public class SnsLinkActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{
//
//    @Override
//    public String getPID(){
//
//        return "";
//    }
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_sns_link;
//    }
//
//    private EditText mEditUrl;
//    private View mTextSyncComplete;
//    private SnsTypeCode mSnsCode;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        TextView textDescription = (TextView) findViewById(R.id.text_sns_link_description);
//        mEditUrl = (EditText) findViewById(R.id.edit_sns_link_url);
//        mTextSyncComplete = findViewById(R.id.text_sns_link_syncComplete);
//        mTextSyncComplete.setOnClickListener(this);
//
//        mSnsCode = (SnsTypeCode) getIntent().getSerializableExtra(Const.KEY);
//        if(mSnsCode.equals(SnsTypeCode.naver)) {
//            textDescription.setText(R.string.msg_input_url_naverBlog);
//            setTitle(getString(R.string.word_sync_naverBlog));
//            mEditUrl.setText("http://naver.naver.com/");
//        } else if(mSnsCode.equals(SnsTypeCode.twitter)) {
//            textDescription.setText(R.string.msg_input_url_twitter);
//            setTitle(getString(R.string.word_sync_twitter));
//            mEditUrl.setText("http://www.twitter.com/");
//        } else if(mSnsCode.equals(SnsTypeCode.facebook)) {
//            textDescription.setText(R.string.msg_input_url_facebook);
//            setTitle(getString(R.string.word_sync_facebook));
//            mEditUrl.setText("http://www.facebook.com/");
//        } else if(mSnsCode.equals(SnsTypeCode.instagram)) {
//            textDescription.setText(R.string.msg_input_url_instagram);
//            setTitle(getString(R.string.word_sync_instagram));
//            mEditUrl.setText("http://www.instagram.com/");
//        } else if(mSnsCode.equals(SnsTypeCode.kakao)) {
//            textDescription.setText(R.string.msg_input_url_kakao);
//            setTitle(getString(R.string.word_sync_kakao));
//            mEditUrl.setText("http://story.kakao.com/");
//        } else if(mSnsCode.equals(SnsTypeCode.directLink)) {
//            textDescription.setText(R.string.msg_input_url_direct_link);
//            setTitle(getString(R.string.word_sync_directLink));
//            mEditUrl.setText("http://");
//        }
//        mEditUrl.setSelection(mEditUrl.getText().length());
//
//    }
//
//    @Override
//    public void onClick(View view){
//
//        switch (view.getId()) {
//            case R.id.text_sns_link_syncComplete:
//                String url = mEditUrl.getText().toString();
//                if(url.trim().length() == 0) {
//                    showAlert(R.string.msg_input_url);
//                    return;
//                }
//                Intent data = new Intent();
//                data.putExtra(Const.SNS, mSnsCode);
//                data.putExtra(Const.SNS_URL, url);
//                setResult(RESULT_OK, data);
//                finish();
//                break;
//        }
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_sync_naverBlog), ToolbarOption.ToolbarMenu.RIGHT);
//        return toolbarOption;
//    }
//
//    @Override
//    public OnToolbarListener getOnToolbarClickListener(){
//
//        return new OnToolbarListener(){
//
//            @Override
//            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag){
//                switch (toolbarMenu) {
//                    case RIGHT:
//                        if(tag.equals(1)) {
//                            onBackPressed();
//                        }
//                        break;
//                }
//            }
//        };
//    }
//}
