//package com.pplus.prnumberbiz.apps.signup.ui;
//
//
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.Html;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
//import com.pplus.prnumberbiz.core.code.common.EnumData;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//import com.pplus.prnumberbiz.core.util.PplusNumberUtil;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
///**
// * 회원가입 완료
// */
//public class InputPrNumberFragment extends BaseFragment<RegPrNumberActivity>{
//
//    private EnumData.PageTypeCode mPageType;
//    private String mPrNumber;
//
//    public static InputPrNumberFragment newInstance(EnumData.PageTypeCode pageTypeCode){
//
//        InputPrNumberFragment fragment = new InputPrNumberFragment();
//        Bundle args = new Bundle();
//        //args.putParcelable(Const.PARAMS, params);
//        args.putString(Const.PARAMS, pageTypeCode.toString());
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//
//        super.onCreate(savedInstanceState);
//        if(getArguments() != null) {
//            String data = getArguments().getString(Const.PARAMS);
//            if(StringUtils.isNotEmpty(data)) {
//                mPageType = EnumData.PageTypeCode.valueOf(data);
//            }
//        }
//    }
//
//    @Override
//    public String getPID(){
//
//        return "";
//    }
//
//    public InputPrNumberFragment(){
//        // Required empty public constructor
//    }
//
//    @Override
//    public int getLayoutResourceId(){
//
//        return R.layout.fragment_input_prnumber;
//    }
//
//    enum NumberType{
//        wire, mobile, free
//    }
//
//    private View layout_mobile, layout_free, layout_biz_wire, layout_biz_mobile;
//    private TextView text_wiredNumber_prefix, text_tab1, text_tab2, text_biz_tab1, text_biz_tab2;
//    private EditText edit_wiredNumber, edit_freeNumber;
//    private String mWireNumber = "", mFreeNumberPrefix, mFreeNumber = "";
//    private NumberType mNumberType;
//
//    @Override
//    public void initializeView(View container){
//
//        ((TextView) container.findViewById(R.id.text_input_prnumber_mobileDescription)).setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_input_prnumber3)));
//        ((TextView) container.findViewById(R.id.text_input_prnumber_freeDescription)).setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_input_prnumber4)));
//        ((TextView) container.findViewById(R.id.text_input_prnumber_wireDescription)).setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_input_prnumber2)));
//        final TextView text_freeDescription2 = (TextView) container.findViewById(R.id.text_input_prnumber_freeDescriptio2);
//
//        if(mPageType == EnumData.PageTypeCode.person) {//개인
//            container.findViewById(R.id.layout_input_prnumber_normalTitle).setVisibility(View.VISIBLE);
//            container.findViewById(R.id.layout_input_prnumber_bizTitle).setVisibility(View.GONE);
//            container.findViewById(R.id.layout_input_prnumber_normal).setVisibility(View.VISIBLE);
//            container.findViewById(R.id.layout_input_prnumber_biz).setVisibility(View.GONE);
//
//            text_tab1 = (TextView) container.findViewById(R.id.text_input_prnumber_tab1);
//            text_tab2 = (TextView) container.findViewById(R.id.text_input_prnumber_tab2);
//            text_tab1.setOnClickListener(this);
//            text_tab2.setOnClickListener(this);
//
//            TextView textMobileNumber = (TextView) container.findViewById(R.id.text_input_prnumber_mobileNumber);
//            if(getParentActivity().getKey().equals(Const.JOIN)) {
//                textMobileNumber.setText(getParentActivity().getParamsJoin().getMobile());
//            } else {
//                textMobileNumber.setText(LoginInfoManager.getInstance().getUser().getMobile());
//            }
//
//
//            layout_mobile = container.findViewById(R.id.layout_input_prnumber_mobile);
//            layout_free = container.findViewById(R.id.layout_input_prnumber_free);
//
//            final TextView textFreeNumberPrefix = (TextView) container.findViewById(R.id.text_input_prnumber_freeNumberPrefix);
//
//            ApiBuilder.create().getPrefixNumber().setCallback(new PplusCallback<NewResultResponse<String>>(){
//
//                @Override
//                public void onResponse(Call<NewResultResponse<String>> call, NewResultResponse<String> response){
//
//                    mFreeNumberPrefix = response.getData();
//                    textFreeNumberPrefix.setText(mFreeNumberPrefix);
//                    text_freeDescription2.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_input_prnumber5, mFreeNumberPrefix)));
//                }
//
//                @Override
//                public void onFailure(Call<NewResultResponse<String>> call, Throwable t, NewResultResponse<String> response){
//
//                }
//            }).build().call();
//
//            edit_freeNumber = (EditText) container.findViewById(R.id.edit_input_prnumber_freeNumber);
//            edit_freeNumber.setSingleLine();
//            edit_freeNumber.addTextChangedListener(new TextWatcher(){
//
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable){
//
//                    String number = editable.toString();
//                    number = number.replace("-", "");
//                    if(!mFreeNumber.equals(number)) {
//                        mFreeNumber = number;
//                        edit_freeNumber.setText(PplusNumberUtil.getOnlyNumber(number));
//                        edit_freeNumber.setSelection(edit_freeNumber.getText().length());
//                    }
//                }
//            });
//
//            text_tab1.setSelected(true);
//            text_tab2.setSelected(false);
//            text_tab1.setTypeface(Typeface.DEFAULT_BOLD);
//            text_tab2.setTypeface(Typeface.DEFAULT);
//            layout_mobile.setVisibility(View.VISIBLE);
//            layout_free.setVisibility(View.GONE);
//            mNumberType = NumberType.mobile;
//
//        } else {//사업자
//            container.findViewById(R.id.layout_input_prnumber_normalTitle).setVisibility(View.GONE);
//            container.findViewById(R.id.layout_input_prnumber_bizTitle).setVisibility(View.VISIBLE);
//            container.findViewById(R.id.layout_input_prnumber_normal).setVisibility(View.GONE);
//            container.findViewById(R.id.layout_input_prnumber_biz).setVisibility(View.VISIBLE);
//
//            text_biz_tab1 = container.findViewById(R.id.text_input_prnumber_biz_tab1);
//            text_biz_tab2 = container.findViewById(R.id.text_input_prnumber_biz_tab2);
//            text_biz_tab1.setOnClickListener(this);
//            text_biz_tab2.setOnClickListener(this);
//
//            layout_biz_mobile = container.findViewById(R.id.layout_input_prnumber_biz_mobile);
//            layout_biz_wire = container.findViewById(R.id.layout_input_prnumber_biz_wired_number);
//
//            text_biz_tab1.setTypeface(Typeface.DEFAULT_BOLD);
//            text_biz_tab2.setTypeface(Typeface.DEFAULT);
//
//            text_biz_tab1.setSelected(true);
//            text_biz_tab2.setSelected(false);
//            mNumberType = NumberType.wire;
//
//            layout_biz_wire.setVisibility(View.VISIBLE);
//            layout_biz_mobile.setVisibility(View.GONE);
//
//            text_wiredNumber_prefix = (TextView) container.findViewById(R.id.text_input_prnumber_wiredNumberPrefix);
//            text_wiredNumber_prefix.setOnClickListener(this);
//            edit_wiredNumber = (EditText) container.findViewById(R.id.edit_input_prnumber_wiredNumber);
//            edit_wiredNumber.setSingleLine();
//            edit_wiredNumber.addTextChangedListener(new TextWatcher(){
//
//                @Override
//                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable editable){
//
//                    String number = editable.toString();
//                    number = number.replace("-", "");
//                    if(!mWireNumber.equals(number)) {
//                        mWireNumber = number;
//                        edit_wiredNumber.setText(PplusNumberUtil.getOnlyNumber(number));
//                        edit_wiredNumber.setSelection(edit_wiredNumber.getText().length());
//                    }
//
//                }
//            });
//            edit_wiredNumber.post(new Runnable(){
//
//                @Override
//                public void run(){
//
//                    edit_wiredNumber.requestFocus();
//                    PplusCommonUtil.Companion.showKeyboard(getActivity(), edit_wiredNumber);
//                }
//            });
//
//        }
//
//        container.findViewById(R.id.text_input_prnumber_signUp).setOnClickListener(this);
//    }
//
//    @Override
//    public void init(){
//
//    }
//
//    @Override
//    public void onClick(View v){
//
//        switch (v.getId()) {
//            case R.id.text_input_prnumber_signUp:
//                if(mPageType.equals(EnumData.PageTypeCode.person)) {
//                    if(mNumberType.equals(NumberType.mobile)) {
//                        if(getParentActivity().getKey().equals(Const.JOIN)) {
//                            mPrNumber = getParentActivity().getParamsJoin().getMobile();
//                        } else {
//                            mPrNumber = LoginInfoManager.getInstance().getUser().getMobile();
//                        }
//
//                    } else {
//                        if(mFreeNumber.length() == 0) {
//                            showAlert(R.string.msg_input_freeNumber);
//                            return;
//                        }
//                        mPrNumber = mFreeNumberPrefix + mFreeNumber;
//                        //mParams.setPrNumber(mFreeNumberPrefix + mFreeNumber);
//                    }
//                } else {
//                    if(mNumberType.equals(NumberType.wire)) {
//                        if(mWireNumber.length() == 0) {
//                            showAlert(R.string.msg_input_wireNumber);
//                            return;
//                        }
//                        String prefix = text_wiredNumber_prefix.getText().toString();
//                        //mParams.setPrNumber(prefix + mWireNumber);
//                        mPrNumber = prefix + mWireNumber;
//                    }
//                    if(mNumberType.equals(NumberType.mobile)) {
//                        if(getParentActivity().getKey().equals(Const.JOIN)) {
//                            mPrNumber = getParentActivity().getParamsJoin().getMobile();
//                        } else {
//                            mPrNumber = LoginInfoManager.getInstance().getUser().getMobile();
//                        }
//
//                    }
//
//                }
//                PplusCommonUtil.Companion.hideKeyboard(getActivity());
//                if(getParentActivity().getKey().equals(Const.JOIN)) {
//                    getParentActivity().signUp(mPrNumber);
//                } else if(getParentActivity().getKey().equals(Const.VERIFICATION_MASTER)) {
//                    getParentActivity().levelUp(mPrNumber);
//                } else {
//                    getParentActivity().allocateNumber(mPrNumber);
//                }
//
//                break;
//            case R.id.text_input_prnumber_tab1:
//                edit_freeNumber.requestFocus();
//                PplusCommonUtil.Companion.hideKeyboard(getActivity());
//                text_tab1.setTypeface(Typeface.DEFAULT_BOLD);
//                text_tab2.setTypeface(Typeface.DEFAULT);
//                text_tab1.setSelected(true);
//                text_tab2.setSelected(false);
//
//                layout_mobile.setVisibility(View.VISIBLE);
//                layout_free.setVisibility(View.GONE);
//                mNumberType = NumberType.mobile;
//
//                break;
//            case R.id.text_input_prnumber_tab2:
//                text_tab1.setTypeface(Typeface.DEFAULT);
//                text_tab2.setTypeface(Typeface.DEFAULT_BOLD);
//
//                text_tab1.setSelected(false);
//                text_tab2.setSelected(true);
//
//                layout_mobile.setVisibility(View.GONE);
//                layout_free.setVisibility(View.VISIBLE);
//                mNumberType = NumberType.free;
//                edit_freeNumber.requestFocus();
//                PplusCommonUtil.Companion.showKeyboard(getActivity(), edit_freeNumber);
//                break;
//            case R.id.text_input_prnumber_biz_tab1:
//                text_biz_tab1.setTypeface(Typeface.DEFAULT_BOLD);
//                text_biz_tab2.setTypeface(Typeface.DEFAULT);
//
//                text_biz_tab1.setSelected(true);
//                text_biz_tab2.setSelected(false);
//                layout_biz_mobile.setVisibility(View.VISIBLE);
//                layout_biz_wire.setVisibility(View.GONE);
//                mNumberType = NumberType.wire;
//                break;
//            case R.id.text_input_prnumber_biz_tab2:
//                edit_wiredNumber.requestApplyInsets();
//                PplusCommonUtil.Companion.hideKeyboard(getActivity());
//                text_biz_tab1.setTypeface(Typeface.DEFAULT);
//                text_biz_tab2.setTypeface(Typeface.DEFAULT_BOLD);
//
//                text_biz_tab1.setSelected(false);
//                text_biz_tab2.setSelected(true);
//
//                layout_biz_mobile.setVisibility(View.VISIBLE);
//                layout_biz_wire.setVisibility(View.GONE);
//                mNumberType = NumberType.mobile;
//                break;
//            case R.id.text_input_prnumber_wiredNumberPrefix:
//
//                final String[] areaCode = getResources().getStringArray(R.array.location_code);
//                AlertBuilder.Builder areaCodeBuilder = new AlertBuilder.Builder();
//                areaCodeBuilder.setTitle(null).setContents(areaCode);
//                areaCodeBuilder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER);
//                areaCodeBuilder.setOnAlertResultListener(new OnAlertResultListener(){
//
//                    @Override
//                    public void onCancel(){
//
//                    }
//
//                    @Override
//                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                        switch (event_alert) {
//                            case LIST:
//                                text_wiredNumber_prefix.setText(areaCode[event_alert.getValue() - 1]);
//                                break;
//                        }
//                    }
//                }).builder().show(getActivity());
//                break;
//        }
//    }
//
//}
