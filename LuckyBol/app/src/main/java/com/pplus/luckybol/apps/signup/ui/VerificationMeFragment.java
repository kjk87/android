package com.pplus.luckybol.apps.signup.ui;//package com.pplus.luckybol.apps.signup.ui;
//
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//
//import com.pplus.utils.part.format.FormatUtil;
//import com.pplus.utils.part.utils.StringUtils;
//import com.pplus.luckybol.R;
//import com.pplus.luckybol.apps.common.ui.base.BaseFragment;
//
//public class VerificationMeFragment extends BaseFragment<VerificationMeActivity>{
//
//    public VerificationMeFragment(){
//
//    }
//
//    public static VerificationMeFragment newInstance(){
//
//        VerificationMeFragment fragment = new VerificationMeFragment();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//
//        super.onCreate(savedInstanceState);
//        if(getArguments() != null) {
//            //            mParam1 = getArguments().getString(ARG_PARAM1);
//        }
//    }
//
//    @Override
//    public int getLayoutResourceId(){
//
//        return R.layout.fragment_verification_me;
//    }
//
//    private EditText edit_name, edit_mobile_number, edit_birthday;
////    private MobileCorpAdapter mAdapter;
//    private View text_male, text_female, text_local, text_foreigner;
//    private int mGender, mNation;
//
//    @Override
//    public void initializeView(View container){
//
//        edit_name = (EditText) container.findViewById(R.id.edit_verification_me_name);
//        edit_mobile_number = (EditText) container.findViewById(R.id.edit_verification_me_mobileNumber);
//        edit_birthday = (EditText) container.findViewById(R.id.edit_verification_me_birthday);
//
//        edit_name.setSingleLine();
//        edit_mobile_number.setSingleLine();
//        edit_birthday.setSingleLine();
//        edit_birthday.setMaxEms(8);
//
//        if(StringUtils.isNotEmpty(getParentActivity().getMobileNumber())){
//            edit_mobile_number.setText(getParentActivity().getMobileNumber());
//        }
//
////        RecyclerView recyclerView = (RecyclerView) container.findViewById(R.id.recycler_verification_me_phoneCorp);
////        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
////        mAdapter = new MobileCorpAdapter(getActivity());
////        recyclerView.setAdapter(mAdapter);
//
//        text_male = container.findViewById(R.id.text_verification_me_male);
//        text_female = container.findViewById(R.id.text_verification_me_female);
//        text_male.setOnClickListener(this);
//        text_female.setOnClickListener(this);
//        setSelect(text_male, text_female);
//        mGender = 0;
//
//        text_local = container.findViewById(R.id.text_verification_me_local);
//        text_foreigner = container.findViewById(R.id.text_verification_me_foreigner);
//        text_local.setOnClickListener(this);
//        text_foreigner.setOnClickListener(this);
//        setSelect(text_local, text_foreigner);
//        mNation = 0;
//
//        container.findViewById(R.id.text_verification_me_verification).setOnClickListener(this);
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
//        super.onClick(v);
//        switch (v.getId()) {
//            case R.id.text_verification_me_male:
//                setSelect(text_male, text_female);
//                mGender = 0;
//                break;
//            case R.id.text_verification_me_female:
//                setSelect(text_female, text_male);
//                mGender = 1;
//                break;
//            case R.id.text_verification_me_local:
//                setSelect(text_local, text_foreigner);
//                mNation = 0;
//                break;
//            case R.id.text_verification_me_foreigner:
//                setSelect(text_foreigner, text_local);
//                mNation = 1;
//                break;
//            case R.id.text_verification_me_verification:
//                String name = edit_name.getText().toString().trim();
//                if(StringUtils.isEmpty(name)) {
//                    showAlert(R.string.msg_input_name);
//                    return;
//                }
//
//                String mobileNumber = edit_mobile_number.getText().toString().trim();
//                if(StringUtils.isEmpty(mobileNumber)) {
//                    showAlert(R.string.msg_input_mobile_number);
//                    return;
//                }
//                if(!FormatUtil.isCellPhoneNumber(mobileNumber)) {
//                    showAlert(R.string.msg_not_valid_mobile_number);
//                    return;
//                }
//                String birthday = edit_birthday.getText().toString().trim();
//                if(StringUtils.isEmpty(birthday)) {
//                    showAlert(R.string.msg_input_birthday);
//                    return;
//                }
//
//                if(birthday.length() < 8){
//                    showAlert(R.string.msg_invalid_birthday);
//                    return;
//                }
//
//                StringBuilder builder = new StringBuilder();
//                builder.append("name=").append(name).append("&");
//                builder.append("phoneNo=").append(mobileNumber).append("&");
////                builder.append("phoneCorp=").append(mAdapter.getMobileCrop()).append("&");
//                builder.append("birthDay=").append(birthday).append("&");
//                builder.append("gender=").append(mGender).append("&");
//                builder.append("nation=").append(mNation);
//                getParentActivity().verification(builder.toString());
//                break;
//        }
//    }
//
//    private void setSelect(View view1, View view2){
//
//        view1.setSelected(true);
//        view2.setSelected(false);
//    }
//
//    @Override
//    public String getPID(){
//
//        return null;
//    }
//
//}
