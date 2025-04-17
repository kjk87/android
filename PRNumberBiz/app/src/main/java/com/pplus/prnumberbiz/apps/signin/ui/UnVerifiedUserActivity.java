//package com.pplus.prnumberbiz.apps.signin.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.common.ui.common.WebViewActivity;
//import com.pplus.prnumberbiz.core.code.common.VerificationMediaCode;
//import com.pplus.prnumberbiz.core.code.custom.VerificationTypeCode;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.request.params.ParamsVerificationFranchise;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberbiz.core.network.model.dto.Terms;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//import com.pplus.prnumberbiz.core.util.PplusNumberUtil;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class UnVerifiedUserActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{
//
//    @Override
//    public String getPID(){
//
//        return "";
//    }
//
//    @Override
//    public String getSID(){
//
//        return getString(R.string.screen_log_un_verified_user);
//    }
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_un_verified_user;
//    }
//
//    private Map<Long, Boolean> mTermsAgreeMap;
//    private ArrayList<Terms> mTermsList;
//    private View mLayoutConfirmPhone, mLayoutAuthPhone;
//    private TextView mTextPhoneNumber;
//    private EditText mEditPhoneNumber, mEditAuthNumber;
//    private LinearLayout mLayoutTerms;
//
//    private String mToken, mVerificationNumber, mPhoneNumber;
//    private boolean mIsAuthPhone;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//
//        TextView textId = (TextView) findViewById(R.id.text_un_verified_id);
//        TextView textNickName = (TextView) findViewById(R.id.text_un_verified_nickName);
//        TextView textNumber = (TextView) findViewById(R.id.text_un_verified_prNumber);
//        if(LoginInfoManager.getInstance().getUser() != null) {
//            textId.setText(LoginInfoManager.getInstance().getUser().getLoginId());
//            textNickName.setText(LoginInfoManager.getInstance().getUser().getNickname());
//        }
//
//        if(LoginInfoManager.getInstance().getUser().getPage().getNumberList() != null && !LoginInfoManager.getInstance().getUser().getPage().getNumberList().isEmpty()) {
//            textNumber.setText(PplusNumberUtil.getPrNumberFormat(LoginInfoManager.getInstance().getUser().getPage().getNumberList().get(0).getNumber()));
//        }
//
//        mLayoutConfirmPhone = findViewById(R.id.layout_un_verified_confirmPhone);
//        mLayoutAuthPhone = findViewById(R.id.layout_un_verified_authPhone);
//        mTextPhoneNumber = (TextView) findViewById(R.id.text_un_verified_phoneNumber);
//        findViewById(R.id.text_un_verified_reAuth).setOnClickListener(this);
//        mEditPhoneNumber = (EditText) findViewById(R.id.edit_un_verified_phoneNumber);
//        mEditAuthNumber = (EditText) findViewById(R.id.text_un_verified_authNumber);
//        findViewById(R.id.text_un_verified_reqAuth).setOnClickListener(this);//인증 요청
//        findViewById(R.id.text_un_verified_confirmAuth).setOnClickListener(this);//인증 확인
//
//        mLayoutTerms = (LinearLayout) findViewById(R.id.layout_un_verified_terms);
//
//        ApiBuilder.create().getActiveTermsAll().setCallback(new PplusCallback<NewResultResponse<Terms>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Terms>> call, NewResultResponse<Terms> response){
//
//                mTermsList = (ArrayList<Terms>) response.getDatas();
//                mTermsAgreeMap = new HashMap<>();
//                mLayoutTerms.removeAllViews();
//                for(int i = 0; i < mTermsList.size(); i++) {
//                    final Terms terms = mTermsList.get(i);
//                    mTermsAgreeMap.put(terms.getNo(), false);
//                    View viewTerms = LayoutInflater.from(UnVerifiedUserActivity.this).inflate(R.layout.item_terms, new LinearLayout(UnVerifiedUserActivity.this));
//                    if(i != 0) {
//                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        layoutParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.height_50), 0, 0);
//                        viewTerms.setLayoutParams(layoutParams);
//
//                    }
//                    CheckBox checkTerms = (CheckBox) viewTerms.findViewById(R.id.check_terms_agree);
//                    checkTerms.setText(terms.getSubject());
//                    checkTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
//
//                        @Override
//                        public void onCheckedChanged(CompoundButton compoundButton, boolean b){
//
//                            mTermsAgreeMap.put(terms.getNo(), b);
//                        }
//                    });
//
//                    viewTerms.findViewById(R.id.image_terms_story).setOnClickListener(new View.OnClickListener(){
//
//                        @Override
//                        public void onClick(View view){
//
//                            Intent intent = new Intent(UnVerifiedUserActivity.this, WebViewActivity.class);
//                            intent.putExtra(Const.TITLE, terms.getSubject());
//                            intent.putExtra(Const.TOOLBAR_RIGHT_ARROW, true);
//                            intent.putExtra(Const.WEBVIEW_URL, terms.getUrl());
//                            intent.putExtra(Const.TERMS_LIST, mTermsList);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                            startActivity(intent);
//                        }
//                    });
//                    mLayoutTerms.addView(viewTerms);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Terms>> call, Throwable t, NewResultResponse<Terms> response){
//
//            }
//        }).build().call();
//        mIsAuthPhone = false;
//        findViewById(R.id.text_un_verified_complete).setOnClickListener(this);//완료 버튼
//    }
//
//    @Override
//    public void onClick(View view){
//
//        switch (view.getId()) {
//            case R.id.text_un_verified_reAuth:
//                mLayoutAuthPhone.setVisibility(View.VISIBLE);
//                mLayoutConfirmPhone.setVisibility(View.GONE);
//                mIsAuthPhone = false;
//                break;
//            case R.id.text_un_verified_reqAuth:
//                String phoneNumber = mEditPhoneNumber.getText().toString();
//                if(phoneNumber.trim().length() == 0) {
//                    Toast.makeText(this, R.string.msg_input_cellNumber, Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                PplusCommonUtil.hideKeyboard(this);
//                mPhoneNumber = phoneNumber;
//                Map<String, String> params = new HashMap<>();
//                params.put("media", VerificationMediaCode.sms.name());
//                params.put("type", VerificationTypeCode.profile.name());
//                params.put("mobile", phoneNumber);
//                showProgress("");
//                ApiBuilder.create().verification(params).setCallback(new PplusCallback<NewResultResponse<ResultVerification>>(){
//
//                    @Override
//                    public void onResponse(Call<NewResultResponse<ResultVerification>> call, NewResultResponse<ResultVerification> response){
//                        hideProgress();
//                        showAlert(R.string.msg_request_sms);
//                        mToken = response.getData().getToken();
//                    }
//
//                    @Override
//                    public void onFailure(Call<NewResultResponse<ResultVerification>> call, Throwable t, NewResultResponse<ResultVerification> response){
//                        hideProgress();
//                        mToken = null;
//                    }
//                }).build().call();
//                break;
//            case R.id.text_un_verified_confirmAuth:
//                if(StringUtils.isEmpty(mToken)) {
//                    Toast.makeText(this, R.string.msg_request_verification, Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                mVerificationNumber = mEditAuthNumber.getText().toString().trim();
//
//                if(mVerificationNumber.trim().length() == 0) {
//                    Toast.makeText(this, R.string.msg_input_verificationNumber, Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                PplusCommonUtil.hideKeyboard(this);
////                ParamsVerification paramsVerification = new ParamsVerification();
////                paramsVerification.setToken(mToken);
////                paramsVerification.setVerificationNumber(mVerificationNumber);
////                LogUtil.e(LOG_TAG, "param : {}", paramsVerification.toString());
////                ApiBuilder.create().verificationFranchiseNumber(paramsVerification).setCallback(new PplusCallback<ResultResponse<JsonObject>>(){
////
////                    @Override
////                    public void onResponse(Call<ResultResponse<JsonObject>> call, ResultResponse<JsonObject> response){
////
////                        showAlert(R.string.msg_success_verified);
////                        mIsAuthPhone = true;
////                        mTextPhoneNumber.setText(mPhoneNumber);
////                        mLayoutAuthPhone.setVisibility(View.GONE);
////                        mLayoutConfirmPhone.setVisibility(View.VISIBLE);
////                    }
////
////                    @Override
////                    public void onFailure(Call<ResultResponse<JsonObject>> call, Throwable t, ResultResponse<JsonObject> response){
////
////                    }
////                }).build().call();
//                break;
//            case R.id.text_un_verified_complete:
//
//                for(int i = 0; i < mTermsList.size(); i++) {
//                    if(mTermsList.get(i).isCompulsory() && !mTermsAgreeMap.get(mTermsList.get(i).getNo())) {
//                        Toast.makeText(this, R.string.msg_agree_terms, Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                }
//
//                ParamsVerificationFranchise paramsVerificationFranchise = new ParamsVerificationFranchise();
//                List<Long> termsList = new ArrayList<>();
//                for(Map.Entry<Long, Boolean> entry : mTermsAgreeMap.entrySet()) {
//                    if(entry.getValue()) {
//                        termsList.add(entry.getKey());
//                    }
//                }
//
//                break;
//        }
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_auth_sms), ToolbarOption.ToolbarMenu.RIGHT);
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
