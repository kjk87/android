package com.pplus.prnumberbiz.apps.setting.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pple.pplus.utils.part.format.FormatUtil;
import com.pple.pplus.utils.part.logs.LogUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
import com.pplus.prnumberbiz.apps.common.mgmt.CountryConfigManager;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.core.code.common.EnumData;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.No;
import com.pplus.prnumberbiz.core.network.model.dto.Post;
import com.pplus.prnumberbiz.core.network.model.dto.PostProperties;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import network.common.PplusCallback;
import retrofit2.Call;

/**
 * Created by ksh on 2016-08-30.
 * 문의하기
 */
public class InquiryActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{

    @Override
    public String getPID(){

        return "";
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_inquiry;
    }

    private TextView text_inquiry_type, text_inquiry_selection;
    private EditText edit_subject, edit_contents, edit_mailId, edit_inquiry_email;
    private View layout_inquiry_selectMail;
    private boolean mIsDirect = false;

    private EnumData.InquiryType mInquiryType;
    private String mKey;

    @Override
    public void initializeView(Bundle savedInstanceState){
        mKey = getIntent().getStringExtra(Const.KEY);

        text_inquiry_type = (TextView) findViewById(R.id.text_inquiry_type);
        edit_subject = (EditText) findViewById(R.id.edit_inquiry_subject);
        edit_subject.setSingleLine();
        edit_contents = (EditText) findViewById(R.id.edit_inquiry_contents);
        edit_mailId = (EditText) findViewById(R.id.edit_inquiry_mailId);
        edit_mailId.setSingleLine();
        text_inquiry_selection = (TextView) findViewById(R.id.text_inquiry_selection);
        text_inquiry_selection.setOnClickListener(this);
        edit_inquiry_email = (EditText) findViewById(R.id.edit_inquiry_email);
        layout_inquiry_selectMail = findViewById(R.id.layout_inquiry_selectMail);
        edit_inquiry_email.setVisibility(View.GONE);

        mInquiryType = EnumData.InquiryType.inquiry;
        text_inquiry_type.setText(R.string.msg_inquiry);
        text_inquiry_type.setOnClickListener(this);

        findViewById(R.id.text_inquiry_complete).setOnClickListener(this);

        if(StringUtils.isNotEmpty(mKey) && mKey.equals(Const.SALE_CAL)) {
            findViewById(R.id.text_inquiry_type_title).setVisibility(View.GONE);
            text_inquiry_type.setVisibility(View.GONE);
            findViewById(R.id.text_inquiry_subject).setVisibility(View.VISIBLE);
            edit_subject.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.text_inquiry_selection:
                showEmailPopup();
                break;
            case R.id.text_inquiry_type:
                showTypePopup();
                break;
            case R.id.text_inquiry_complete:
                insert();
                break;
        }
    }

    private void insert(){

        String subject = "";
        if(StringUtils.isNotEmpty(mKey) && mKey.equals(Const.SALE_CAL)) {
            subject = getString(R.string.msg_apply_sale_cal_service);
        } else {
            subject = edit_subject.getText().toString().trim();
            if(StringUtils.isEmpty(subject)) {
                showAlert(R.string.msg_input_title);
                return;
            }
        }


        String contents = edit_contents.getText().toString().trim();
        if(StringUtils.isEmpty(contents)) {
            showAlert(R.string.msg_input_contents);
            return;
        }

        String email = null;

        if(mIsDirect) {
            email = edit_inquiry_email.getText().toString().trim();

            if(email.trim().length() == 0) {
                showAlert(R.string.msg_input_email_id);
                return;
            }
        } else {
            email = edit_mailId.getText().toString();
            if(email.trim().length() == 0) {
                showAlert(R.string.msg_input_email_id);
                return;
            }

            email = email + "@" + text_inquiry_selection.getText().toString();
        }

        if(!FormatUtil.isEmailAddress(email)) {
            showAlert(R.string.msg_valid_email);
            return;
        }

        final Post params = new Post();
        switch (mInquiryType) {
            case inquiry:
                params.setBoard(new No(CountryConfigManager.getInstance().getConfig().getProperties().getInquiryBoard()));
                break;
            case suggest:
                params.setBoard(new No(CountryConfigManager.getInstance().getConfig().getProperties().getSuggestBoard()));
                break;
        }

        params.setSubject(subject);
        params.setContents(contents);
        params.setType(mInquiryType.name());
        PostProperties properties = new PostProperties();
        properties.setEmail(email);
        params.setProperties(properties);

        AlertBuilder.Builder builder = new AlertBuilder.Builder();
        builder.setTitle(getString(R.string.word_notice_alert));
        builder.addContents(new AlertData.MessageData(getString(R.string.msg_question_inquiry), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
        builder.setOnAlertResultListener(new OnAlertResultListener(){

            @Override
            public void onCancel(){

            }

            @Override
            public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                switch (event_alert) {
                    case RIGHT:

                        showProgress("");
                        ApiBuilder.create().insertPost(params).setCallback(new PplusCallback<NewResultResponse<Post>>(){

                            @Override
                            public void onResponse(Call<NewResultResponse<Post>> call, NewResultResponse<Post> response){

                                hideProgress();
                                showAlert(R.string.msg_complete_regist);
                                finish();
                            }

                            @Override
                            public void onFailure(Call<NewResultResponse<Post>> call, Throwable t, NewResultResponse<Post> response){

                                hideProgress();
                            }
                        }).build().call();
                        break;
                }
            }
        }).builder().show(this);
    }

    private void showTypePopup(){

        String[] contents = new String[]{getString(R.string.msg_inquiry), getString(R.string.msg_suggestion)};
        AlertBuilder.Builder builder = new AlertBuilder.Builder();
        builder.setTitle(getString(R.string.word_select));
        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER);
        builder.setContents(contents);
        builder.setLeftText(getString(R.string.word_cancel));
        builder.setOnAlertResultListener(new OnAlertResultListener(){

            @Override
            public void onCancel(){

            }

            @Override
            public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                switch (event_alert) {
                    case LIST:
                        switch (event_alert.getValue()) {
                            case 1:
                                mInquiryType = EnumData.InquiryType.inquiry;
                                text_inquiry_type.setText(R.string.msg_inquiry);
                                break;
                            case 2:
                                mInquiryType = EnumData.InquiryType.suggest;
                                text_inquiry_type.setText(R.string.msg_suggestion);
                                break;
                        }
                        break;
                }

            }
        }).builder().show(this);
    }

    /**
     * 이메일 선택 팝업
     */
    private void showEmailPopup(){

        final String[] mailAddress = getResources().getStringArray(R.array.mail_address);
        AlertBuilder.Builder builder = new AlertBuilder.Builder();
        builder.setTitle(getString(R.string.word_select));
        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER);
        builder.setContents(mailAddress);
        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
        builder.setOnAlertResultListener(new OnAlertResultListener(){

            @Override
            public void onCancel(){

            }

            @Override
            public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                switch (event_alert) {

                    case LIST:
                        if(event_alert == AlertBuilder.EVENT_ALERT.LIST) {
                            int value = event_alert.getValue();
                            LogUtil.e(LOG_TAG, "value = {}", event_alert.getValue());

                            if(event_alert.getValue() == mailAddress.length) {
                                // 직접 입력
                                mIsDirect = true;
                                layout_inquiry_selectMail.setVisibility(View.GONE);
                                edit_inquiry_email.setVisibility(View.VISIBLE);
                            } else {
                                // 이메일 선택 완료
                                layout_inquiry_selectMail.setVisibility(View.VISIBLE);
                                edit_inquiry_email.setVisibility(View.GONE);
                                text_inquiry_selection.setText(mailAddress[event_alert.getValue() - 1]);
                                mIsDirect = false;
                            }
                        }
                        break;
                }
            }
        }).builder().show(this);

    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_inquiry), ToolbarOption.ToolbarMenu.LEFT);
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

    @Override
    public void onBackPressed(){

        if(edit_contents.getText().toString().trim().length() > 0) {
            AlertBuilder.Builder builder = new AlertBuilder.Builder();
            builder.setTitle(getString(R.string.word_notice_alert));
            builder.addContents(new AlertData.MessageData(getString(R.string.msg_alert_back_button1), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
            builder.addContents(new AlertData.MessageData(getString(R.string.msg_alert_back_button2), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
            builder.setOnAlertResultListener(new OnAlertResultListener(){

                @Override
                public void onCancel(){

                }

                @Override
                public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                    switch (event_alert) {
                        case RIGHT:
                            finish();
                            break;
                    }
                }
            }).builder().show(this);
        } else {
            finish();
        }
    }
}
