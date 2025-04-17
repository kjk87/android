//package com.pplus.prnumberbiz.apps.signup.ui;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.InputType;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ScrollView;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.pref.PreferenceUtil;
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
//import com.pplus.prnumberbiz.apps.common.ui.common.WebViewActivity;
//import com.pplus.prnumberbiz.core.code.common.SnsTypeCode;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.No;
//import com.pplus.prnumberbiz.core.network.model.dto.Terms;
//import com.pplus.prnumberbiz.core.network.model.dto.User;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberbiz.core.network.prnumber.IPRNumberRequest;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Pattern;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
///**
// * 회원가입 입력화면
// */
//public class SignUpInputFragment extends BaseFragment<SignUpActivity> implements CompoundButton.OnCheckedChangeListener{
//
//    @Override
//    public String getPID(){
//
//        return "";
//    }
//
//    private ArrayList<Terms> mTermsList;
//    private Map<Long, Boolean> mTermsAgreeMap;
//    private boolean mIsCheckId;
//    private String mSignedId, mSignedPw;
//
//    private EditText edit_id, edit_password, edit_recommend_code;
//    private View mComplete, layout_sign_up_input;
//    private TextView text_mobile_number;
//    private LinearLayout layout_terms;
//    private ScrollView mScrollView;
//    private List<CheckBox> mCheckBoxList;
//    private CheckBox mCheckAllAgree;
//    private User paramsJoin;
//    private boolean mIsSns = false;
//
//    public static SignUpInputFragment newInstance(User params){
//
//        SignUpInputFragment fragment = new SignUpInputFragment();
//        Bundle args = new Bundle();
//        args.putParcelable(Const.JOIN, params);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    public SignUpInputFragment(){
//        // Required empty public constructor
//    }
//
//    @Override
//    public int getLayoutResourceId(){
//
//        return R.layout.fragment_sign_up_input;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//
//        super.onCreate(savedInstanceState);
//        if(getArguments() != null) {
//            paramsJoin = getArguments().getParcelable(Const.JOIN);
//        }
//
//    }
//
//    @Override
//    public void initializeView(View container){
//
//        layout_sign_up_input = container.findViewById(R.id.layout_sign_up_input);
//
//
//        if(getParentActivity().getKey().equals(Const.VERIFICATION_MASTER)) {
//            layout_sign_up_input.setVisibility(View.GONE);
//        } else {
//            if(StringUtils.isNotEmpty(paramsJoin.getAccountType()) && !paramsJoin.getAccountType().equals(SnsTypeCode.pplus.name())) {
//                layout_sign_up_input.setVisibility(View.GONE);
//                mIsSns = true;
//            } else {
//                layout_sign_up_input.setVisibility(View.VISIBLE);
//                mIsSns = false;
//            }
//        }
//
//
//        edit_recommend_code = (EditText) container.findViewById(R.id.edit_signUp_recommend_code);
//        String recommend = PreferenceUtil.getDefaultPreference(getActivity()).getString(Const.RECOMMEND);
//                if(StringUtils.isNotEmpty(recommend)){
//                    edit_recommend_code.setText(recommend);
//                }
//
//        text_mobile_number = (TextView) container.findViewById(R.id.text_sign_up_mobile_number);
//        text_mobile_number.setText("" + paramsJoin.getMobile());
//
//        mScrollView = (ScrollView) container.findViewById(R.id.scroll_signUp);
//        edit_id = (EditText) container.findViewById(R.id.edit_signUp_id);
//        edit_id.setSingleLine();
//        container.findViewById(R.id.text_signUp_id_doubleCheck).setOnClickListener(this);//아이디 중복 체크
//        edit_password = (EditText) container.findViewById(R.id.edit_signUp_password);
//        edit_password.setSingleLine();
//        edit_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//
//        layout_terms = (LinearLayout) container.findViewById(R.id.layout_sign_up_terms);
//
//        IPRNumberRequest<Terms> request;
//
//        if(getParentActivity().getKey().equals(Const.VERIFICATION_MASTER)) {
//            request = ApiBuilder.create().getNotSignedActiveTermsAll(getActivity().getPackageName());
//        } else {
//            request = ApiBuilder.create().getActiveTermsAll(getActivity().getPackageName());
//        }
//
//        request.setCallback(new PplusCallback<NewResultResponse<Terms>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Terms>> call, NewResultResponse<Terms> response){
//
//                mTermsList = (ArrayList<Terms>) response.getDatas();
//                mTermsAgreeMap = new HashMap<>();
//                layout_terms.removeAllViews();
//                mCheckBoxList = new ArrayList<CheckBox>();
//                for(int i = 0; i < mTermsList.size(); i++) {
//                    final Terms terms = mTermsList.get(i);
//                    mTermsAgreeMap.put(terms.getNo(), false);
//                    View viewTerms = LayoutInflater.from(getActivity()).inflate(R.layout.item_terms, new LinearLayout(getActivity()));
//                    if(i != 0) {
//                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                        layoutParams.setMargins(0, getResources().getDimensionPixelSize(R.dimen.height_50), 0, 0);
//                        viewTerms.setLayoutParams(layoutParams);
//                    }
//                    CheckBox checkTerms = (CheckBox) viewTerms.findViewById(R.id.check_terms_agree);
//                    checkTerms.setText(terms.getSubject());
//                    checkTerms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
//
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
//
//                            boolean isAll = true;
//                            for(CheckBox checkBox : mCheckBoxList) {
//                                if(!checkBox.isChecked()) {
//                                    isAll = false;
//                                    break;
//                                }
//                            }
//                            mCheckAllAgree.setOnCheckedChangeListener(null);
//                            mCheckAllAgree.setChecked(isAll);
//                            mCheckAllAgree.setOnCheckedChangeListener(SignUpInputFragment.this);
//
//                            mTermsAgreeMap.put(terms.getNo(), isChecked);
//                        }
//                    });
//                    mCheckBoxList.add(checkTerms);
//
//                    viewTerms.findViewById(R.id.image_terms_story).setOnClickListener(new View.OnClickListener(){
//
//                        @Override
//                        public void onClick(View view){
//
//                            Intent intent = new Intent(getActivity(), WebViewActivity.class);
//                            intent.putExtra(Const.TITLE, terms.getSubject());
//                            intent.putExtra(Const.TOOLBAR_RIGHT_ARROW, true);
//                            intent.putExtra(Const.WEBVIEW_URL, terms.getUrl());
//                            intent.putExtra(Const.TERMS_LIST, mTermsList);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                            startActivity(intent);
//                        }
//                    });
//                    layout_terms.addView(viewTerms);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Terms>> call, Throwable t, NewResultResponse<Terms> response){
//
//            }
//        }).build().call();
//
//        mCheckAllAgree = (CheckBox) container.findViewById(R.id.check_signUp_totalAgree);
//        mCheckAllAgree.setOnCheckedChangeListener(this);
//
//        mComplete = container.findViewById(R.id.text_signUp_Complete);//가입완료
//        mComplete.setOnClickListener(this);
//    }
//
//    @Override
//    public void init(){
//
//    }
//
//    @Override
//    public void onCheckedChanged(CompoundButton compoundButton, boolean b){
//
//        if(mCheckBoxList != null) {
//            for(CheckBox checkBox : mCheckBoxList) {
//                checkBox.setChecked(b);
//            }
//        } else {
//            mCheckAllAgree.setChecked(!b);
//        }
//    }
//
//    private boolean isValidId(String id){
//
//        String regex = "^[a-zA-Z]{1}[a-zA-Z0-9,@,_,-,.]{2,19}$";
//        return Pattern.matches(regex, id);
//    }
//
//    @Override
//    public void onClick(View v){
//
//        Intent intent;
//        switch (v.getId()) {
//            case R.id.text_signUp_id_doubleCheck:
//                String id = edit_id.getText().toString().trim();
//                if(id.length() == 0) {
//                    showAlert(R.string.msg_input_id);
//                    return;
//                }
//
//                if(id.length() < 4) {
//                    showAlert(R.string.msg_input_over4);
//                    return;
//                }
//
//                if(id.matches(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*")) {
//                    showAlert(R.string.msg_input_valid_id);
//                    return;
//                }
//
//                Map<String, String> params = new HashMap<>();
//                params.put("loginId", id);
//                ApiBuilder.create().existsUser(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//                    @Override
//                    public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//
//                        hideProgress();
//                        mIsCheckId = false;
//                        showAlert(getString(R.string.msg_duplicate_id));
//                    }
//
//                    @Override
//                    public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
//
//                        hideProgress();
//                        mIsCheckId = true;
//                        showAlert(getString(R.string.msg_enable_use));
//                        mSignedId = edit_id.getText().toString().trim();
//                    }
//                }).build().call();
//
//                break;
//            case R.id.text_signUp_Complete:
//                if(getParentActivity().getKey().equals(Const.JOIN)) {
//                    if(!mIsSns) {
//                        String loginId = edit_id.getText().toString().trim();
//                        ;
//
//                        if(StringUtils.isEmpty(loginId)) {
//                            mScrollView.smoothScrollTo(0, edit_id.getTop());
//                            edit_id.requestFocus();
//                            showAlert(R.string.msg_input_id);
//                            return;
//                        }
//
//                        if(loginId.length() < 4) {
//                            mScrollView.smoothScrollTo(0, edit_id.getTop());
//                            edit_id.requestFocus();
//                            showAlert(R.string.msg_input_over4);
//                            return;
//                        }
//
//                        if(!mIsCheckId || !mSignedId.equals(loginId)) {
//                            mScrollView.smoothScrollTo(0, edit_id.getTop());
//                            edit_id.requestFocus();
//                            showAlert(R.string.msg_check_duplication_id);
//                            return;
//                        }
//
//                        mSignedPw = edit_password.getText().toString().trim();
//                        if(mSignedPw.length() == 0) {
//                            mScrollView.smoothScrollTo(0, edit_password.getTop());
//                            edit_password.requestFocus();
//                            showAlert(R.string.msg_input_password);
//                            return;
//                        }
//
//                        if(mSignedPw.length() < 4) {
//                            mScrollView.smoothScrollTo(0, edit_password.getTop());
//                            edit_password.requestFocus();
//                            showAlert(getString(R.string.to_password) + " " + getString(R.string.format_msg_input_over, 4));
//                            return;
//                        }
//                    }
//
//                    String recommendationCode = edit_recommend_code.getText().toString().trim();
//                    if(StringUtils.isNotEmpty(recommendationCode)) {
//                        paramsJoin.setRecommendationCode(recommendationCode);
//                    }
//                }
//
//                for(int i = 0; i < mTermsList.size(); i++) {
//                    if(mTermsList.get(i).isCompulsory() && !mTermsAgreeMap.get(mTermsList.get(i).getNo())) {
//                        showAlert(R.string.msg_agree_terms);
//                        return;
//                    }
//                }
//
//                List<No> termsList = new ArrayList<>();
//                for(Map.Entry<Long, Boolean> entry : mTermsAgreeMap.entrySet()) {
//                    if(entry.getValue()) {
//                        termsList.add(new No(entry.getKey()));
//                    }
//                }
//
//                if(!mIsSns) {
//                    paramsJoin.setLoginId(mSignedId);
//                    paramsJoin.setPassword(mSignedPw);
//                    paramsJoin.setAccountType(SnsTypeCode.pplus.name());
//                }
//
//                paramsJoin.setTermsList(termsList);
//
//                intent = new Intent(getActivity(), RegPrNumberActivity.class);
//                intent.putExtra(Const.KEY, getParentActivity().getKey());
//                intent.putExtra(Const.JOIN, paramsJoin);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                getActivity().startActivityForResult(intent, Const.REQ_JOIN);
//                break;
//        }
//
//    }
//}
