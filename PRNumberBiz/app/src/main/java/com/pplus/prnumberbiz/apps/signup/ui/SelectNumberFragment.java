//package com.pplus.prnumberbiz.apps.signup.ui;
//
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.text.Html;
//import android.view.View;
//import android.widget.TextView;
//
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
//import com.pplus.prnumberbiz.core.code.common.EnumData;
//import com.pplus.prnumberbiz.core.network.model.dto.Franchise;
//import com.pplus.prnumberbiz.core.network.model.dto.No;
//import com.pplus.prnumberbiz.core.network.model.dto.Page;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//
//import static android.app.Activity.RESULT_OK;
//
//
///**
// * 회원가입 완료
// */
//public class SelectNumberFragment extends BaseFragment<RegPrNumberActivity>{
//
//
//    public static SelectNumberFragment newInstance(){
//
//        SelectNumberFragment fragment = new SelectNumberFragment();
//        Bundle args = new Bundle();
//        //        args.putString(Const.KEY, key);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState){
//
//        super.onCreate(savedInstanceState);
//        if(getArguments() != null) {
//            //            mKey = getArguments().getString(Const.KEY);
//        }
//    }
//
//    @Override
//    public String getPID(){
//
//        return "";
//    }
//
//    public SelectNumberFragment(){
//        // Required empty public constructor
//    }
//
//    @Override
//    public int getLayoutResourceId(){
//
//        return R.layout.fragment_select_number;
//    }
//
//    private View layout_biz, layout_person, layout_franchise;
//    private EnumData.PageTypeCode typeCode;
//
//    @Override
//    public void initializeView(View container){
//
//        layout_biz = container.findViewById(R.id.layout_select_biz);
//        layout_person = container.findViewById(R.id.layout_select_person);
//        layout_franchise = container.findViewById(R.id.layout_select_franchise);
//        layout_biz.setOnClickListener(this);
//        layout_person.setOnClickListener(this);
//        layout_franchise.setOnClickListener(this);
//        container.findViewById(R.id.text_select_number).setOnClickListener(this);
//
//        ((TextView) container.findViewById(R.id.text_select_number1)).setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_select_number2)));
//        ((TextView) container.findViewById(R.id.text_select_number2)).setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_select_number3)));
//        ((TextView) container.findViewById(R.id.text_select_number3)).setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_select_number6)));
//        ((TextView) container.findViewById(R.id.text_select_number4)).setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_select_number7)));
//
//        typeCode = EnumData.PageTypeCode.store;
//        layout_biz.setSelected(true);
//        layout_person.setSelected(false);
//    }
//    @Override
//    public void init(){
//
//    }
//    @Override
//    public void onClick(View v){
//
//        switch (v.getId()) {
//            case R.id.layout_select_biz:
//                typeCode = EnumData.PageTypeCode.store;
//                layout_biz.setSelected(true);
//                layout_person.setSelected(false);
//                layout_franchise.setSelected(false);
//                break;
//            case R.id.layout_select_person:
//                typeCode = EnumData.PageTypeCode.person;
//                layout_biz.setSelected(false);
//                layout_person.setSelected(true);
//                layout_franchise.setSelected(false);
//                break;
//            case R.id.layout_select_franchise:
//                typeCode = EnumData.PageTypeCode.franchise;
//                layout_biz.setSelected(false);
//                layout_person.setSelected(false);
//                layout_franchise.setSelected(true);
//                break;
//            case R.id.text_select_number:
//                if(typeCode.equals(EnumData.PageTypeCode.franchise)){
//                    Intent intent = new Intent(getActivity(), SearchFranchiseActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    getActivity().startActivityForResult(intent, Const.REQ_FRANCHISE);
//                }else {
//                    Page page = new Page();
//                    page.setType(typeCode.name());
//                    getParentActivity().getParamsJoin().setPage(page);
//                    getParentActivity().inputPrNumber(typeCode);
//                }
//
//                break;
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//            case Const.REQ_FRANCHISE:
//
//                if(resultCode == RESULT_OK && data != null){
//                    Franchise franchise = data.getParcelableExtra(Const.DATA);
//                    Page page = new Page();
//                    page.setCooperation(new No(franchise.getNo()));
//                    page.setType(typeCode.name());
//                    getParentActivity().getParamsJoin().setPage(page);
//                    getParentActivity().inputPrNumber(typeCode);
//                }
//                break;
//        }
//    }
//}
