package com.pplus.prnumberbiz.apps.signup.ui;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pple.pplus.utils.part.format.FormatUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

public class VerificationMeFragment extends BaseFragment<VerificationMeActivity>{

    public VerificationMeFragment(){

    }

    public static VerificationMeFragment newInstance(){

        VerificationMeFragment fragment = new VerificationMeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        if(getArguments() != null) {
            //            mParam1 = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public int getLayoutResourceId(){

        return R.layout.fragment_verification_me;
    }

    private EditText edit_name, edit_mobile_number, edit_birthday;
    private TextView text_mobile_corp;
    private View text_male, text_female, text_local, text_foreigner;
    private int mGender, mNation;
    private MobileCorp mMobileCorp = null;
    ArrayList<MobileCorp> mmMobileCorpList;

    public class MobileCorp{
        String value;
        String name;

        public MobileCorp(String value, String name){

            this.value = value;
            this.name = name;
        }

        public String getValue(){

            return value;
        }

        public void setValue(String value){

            this.value = value;
        }

        public String getName(){

            return name;
        }

        public void setName(String name){

            this.name = name;
        }
    }

    @Override
    public void initializeView(View container){

        mmMobileCorpList = new ArrayList<>();
        mmMobileCorpList.add(new MobileCorp("SKT", getString(R.string.word_skt)));
        mmMobileCorpList.add(new MobileCorp("KTF", getString(R.string.word_kt)));
        mmMobileCorpList.add(new MobileCorp("LGT", getString(R.string.word_lgt)));
        mmMobileCorpList.add(new MobileCorp("SKM", getString(R.string.word_skm)));
        mmMobileCorpList.add(new MobileCorp("KTM", getString(R.string.word_ktm)));
        mmMobileCorpList.add(new MobileCorp("LGM", getString(R.string.word_lgm)));

        edit_name = (EditText) container.findViewById(R.id.edit_verification_me_name);
        edit_mobile_number = (EditText) container.findViewById(R.id.edit_verification_me_mobileNumber);
        edit_birthday = (EditText) container.findViewById(R.id.edit_verification_me_birthday);
        edit_birthday.setSingleLine();

        edit_name.setSingleLine();
        edit_mobile_number.setSingleLine();
//        text_birthday.setOnClickListener(this);


        if(StringUtils.isNotEmpty(getParentActivity().getMobileNumber())) {
            edit_mobile_number.setText(getParentActivity().getMobileNumber());
        }

        text_male = container.findViewById(R.id.text_verification_me_male);
        text_female = container.findViewById(R.id.text_verification_me_female);
        text_male.setOnClickListener(this);
        text_female.setOnClickListener(this);
        setSelect(text_male, text_female);
        mGender = 0;

        text_local = container.findViewById(R.id.text_verification_me_local);
        text_foreigner = container.findViewById(R.id.text_verification_me_foreigner);
        text_local.setOnClickListener(this);
        text_foreigner.setOnClickListener(this);
        setSelect(text_local, text_foreigner);
        mNation = 0;

        text_mobile_corp = (TextView)container.findViewById(R.id.text_verification_me_mobile);
        text_mobile_corp.setOnClickListener(this);

        container.findViewById(R.id.text_verification_me_verification).setOnClickListener(this);
    }

    @Override
    public void onClick(View v){

        super.onClick(v);
        switch (v.getId()) {
            case R.id.text_verification_me_mobile:
                AlertBuilder.Builder alertBuilder = new AlertBuilder.Builder();
                alertBuilder.setContents(getString(R.string.word_skt), getString(R.string.word_kt), getString(R.string.word_lgt), getString(R.string.word_skm), getString(R.string.word_ktm), getString(R.string.word_lgm));
                alertBuilder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(new OnAlertResultListener(){

                    @Override
                    public void onCancel(){

                    }

                    @Override
                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                        switch (event_alert){
                            case LIST:
                                mMobileCorp = mmMobileCorpList.get(event_alert.getValue()-1);
                                text_mobile_corp.setText(mMobileCorp.name);
                                break;
                        }

                    }
                }).builder().show(getActivity());
                break;
//            case R.id.text_verification_me_birthday:
//                Intent intent = new Intent(getActivity(), DatePickerActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                getActivity().startActivityForResult(intent, Const.REQ_BIRTH);
//                break;
            case R.id.text_verification_me_male:
                setSelect(text_male, text_female);
                mGender = 0;
                break;
            case R.id.text_verification_me_female:
                setSelect(text_female, text_male);
                mGender = 1;
                break;
            case R.id.text_verification_me_local:
                setSelect(text_local, text_foreigner);
                mNation = 0;
                break;
            case R.id.text_verification_me_foreigner:
                setSelect(text_foreigner, text_local);
                mNation = 1;
                break;
            case R.id.text_verification_me_verification:
                String name = edit_name.getText().toString().trim();
                if(StringUtils.isEmpty(name)) {
                    showAlert(R.string.msg_input_name);
                    return;
                }

                String mobileNumber = edit_mobile_number.getText().toString().trim();
                if(StringUtils.isEmpty(mobileNumber)) {
                    showAlert(R.string.msg_input_mobile_number);
                    return;
                }
                if(!FormatUtil.isCellPhoneNumber(mobileNumber)) {
                    showAlert(R.string.msg_not_valid_mobile_number);
                    return;
                }
                String birthday = edit_birthday.getText().toString().trim();
                if(StringUtils.isEmpty(birthday)) {
                    showAlert(R.string.msg_input_birthday);
                    return;
                }

                if(birthday.length() < 8) {
                    showAlert(R.string.msg_invalid_birthday);
                    return;
                }

//                if(mMobileCorp == null){
//                    showAlert(R.string.msg_select_mobile_corp);
//                    return;
//                }

                StringBuilder builder = new StringBuilder();
                builder.append("name=").append(name).append("&");
                builder.append("phoneNo=").append(mobileNumber).append("&");
//                builder.append("phoneCorp=").append(mMobileCorp.value).append("&");
                builder.append("birthDay=").append(birthday).append("&");
                builder.append("gender=").append(mGender).append("&");
                builder.append("nation=").append(mNation);
                getParentActivity().verification(builder.toString());
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//            case Const.REQ_BIRTH:
//                if(resultCode == RESULT_OK) {
//                    if(data != null) {
//                        String birthDay = data.getStringExtra(Const.BIRTH_DAY);
//                        edit_birthday.setText(birthDay);
//                    }
//                }
//
//                break;
//        }
    }

    @Override
    public void init(){

    }

    private void setSelect(View view1, View view2){

        view1.setSelected(true);
        view2.setSelected(false);
    }

    @Override
    public String getPID(){

        return null;
    }

}
