//package com.pplus.prnumberbiz.apps.question;
//
//import android.support.annotation.NonNull;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.format.FormatUtil;
//import com.pple.pplus.utils.part.logs.LogUtil;
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
//import com.pplus.prnumberbiz.apps.common.mgmt.CountryConfigManager;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.core.code.common.EnumData;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.No;
//import com.pplus.prnumberbiz.core.network.model.dto.Post;
//import com.pplus.prnumberbiz.core.network.model.dto.PostProperties;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class AllianceActivity extends BaseActivity implements View.OnClickListener, ImplToolbar{
//
//    @Override
//    public String getPID(){
//
//        return null;
//    }
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_alliance;
//    }
//
//    private TextView text_selection;
//    private EditText edit_subject, edit_contents, edit_mailId, edit_email;
//    private View layout_selectMail;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        edit_subject = (EditText) findViewById(R.id.edit_alliance_subject);
//        edit_subject.setSingleLine();
//        edit_contents = (EditText) findViewById(R.id.edit_alliance_contents);
//        edit_mailId = (EditText) findViewById(R.id.edit_alliance_mailId);
//        edit_mailId.setSingleLine();
//        text_selection = (TextView) findViewById(R.id.text_alliance_selection);
//        text_selection.setOnClickListener(this);
//        edit_email = (EditText) findViewById(R.id.edit_alliance_email);
//        edit_email.setVisibility(View.GONE);
//
//        layout_selectMail = findViewById(R.id.layout_alliance_selectMail);
//
//        findViewById(R.id.text_alliance_complete).setOnClickListener(this);
//    }
//
//    @Override
//    public void onClick(View view){
//
//        switch (view.getId()) {
//            case R.id.text_alliance_selection:
//                showEmailPopup();
//                break;
//            case R.id.text_alliance_complete:
//                String subject = edit_subject.getText().toString().trim();
//                if(StringUtils.isEmpty(subject)) {
//                    showAlert(R.string.msg_input_title);
//                    return;
//                }
//
//                final String contents = edit_contents.getText().toString().trim();
//
//                if(StringUtils.isEmpty(contents)) {
//                    showAlert(R.string.msg_input_contents);
//                    return;
//                }
//
//                String email = null;
//
//                if(layout_selectMail.getVisibility() == View.VISIBLE) {
//                    String id = edit_mailId.getText().toString().trim();
//                    if(StringUtils.isEmpty(id)) {
//                        showAlert(R.string.msg_input_email_id);
//                        return;
//                    }
//
//                    String mailDomain = text_selection.getText().toString().trim();
//                    if(StringUtils.isEmpty(mailDomain)) {
//                        showAlert(R.string.msg_select_email_domain);
//                        return;
//                    }
//
//                    email = id + "@" + mailDomain;
//                } else {
//                    email = edit_email.getText().toString().trim();
//                    if(StringUtils.isEmpty(email)){
//                        showAlert(R.string.msg_input_email);
//                        return;
//                    }
//                }
//
//                if(!FormatUtil.isEmailAddress(email)){
//                    showAlert(R.string.msg_valid_email);
//                    return;
//                }
//
//                final Post params = new Post();
//                params.setBoard(new No(CountryConfigManager.getInstance().getConfig().getProperties().getCoopBoard()));
//                params.setSubject(subject);
//                params.setContents(contents);
//                params.setType(EnumData.InquiryType.inquirycoop.name());
//                PostProperties properties = new PostProperties();
//                properties.setEmail(email);
//                params.setProperties(properties);
//
//                AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                builder.setTitle(getString(R.string.word_notice_alert));
//                builder.addContents(new AlertData.MessageData(getString(R.string.msg_question_inquiry), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
//                builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//                    @Override
//                    public void onCancel(){
//
//                    }
//
//                    @Override
//                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//                        switch (event_alert){
//                            case RIGHT:
//
//                                showProgress("");
//                                ApiBuilder.create().insertPost(params).setCallback(new PplusCallback<NewResultResponse<Post>>(){
//
//                                    @Override
//                                    public void onResponse(Call<NewResultResponse<Post>> call, NewResultResponse<Post> response){
//
//                                        hideProgress();
//                                        showAlert(R.string.msg_complete_regist);
//                                        finish();
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<NewResultResponse<Post>> call, Throwable t, NewResultResponse<Post> response){
//
//                                        hideProgress();
//                                    }
//                                }).build().call();
//                                break;
//                        }
//                    }
//                }).builder().show(this);
//
//                break;
//        }
//    }
//
//    /**
//     * 이메일 선택 팝업
//     */
//    private void showEmailPopup(){
//
//        final String[] mailAddress = getResources().getStringArray(R.array.mail_address);
//        AlertBuilder.Builder builder = new AlertBuilder.Builder();
//        builder.setTitle(getString(R.string.word_select));
//        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER);
//        builder.setContents(mailAddress);
//        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
//        builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//            @Override
//            public void onCancel(){
//
//            }
//
//            @Override
//            public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                switch (event_alert) {
//
//                    case LIST:
//                        if(event_alert == AlertBuilder.EVENT_ALERT.LIST) {
//                            int value = event_alert.getValue();
//                            LogUtil.e(LOG_TAG, "value = {}", event_alert.getValue());
//
//                            if(event_alert.getValue() == mailAddress.length) {
//                                // 직접 입력
//                                layout_selectMail.setVisibility(View.GONE);
//                                edit_email.setVisibility(View.VISIBLE);
//                            } else {
//                                // 이메일 선택 완료
//                                layout_selectMail.setVisibility(View.VISIBLE);
//                                edit_email.setVisibility(View.GONE);
//                                text_selection.setText(mailAddress[event_alert.getValue() - 1]);
//                            }
//                        }
//                        break;
//                }
//            }
//        }).builder().show(this);
//
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_alliance_inquiry), ToolbarOption.ToolbarMenu.LEFT);
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
//
//                switch (toolbarMenu) {
//                    case LEFT:
//                        if(tag.equals(1)) {
//                            onBackPressed();
//                        }
//                        break;
//                }
//            }
//        };
//    }
//}
