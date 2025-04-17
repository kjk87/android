package com.pplus.prnumberbiz.apps.setting.ui;


import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.pple.pplus.utils.part.format.FormatUtil;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseFragment;
import com.pplus.prnumberbiz.apps.signup.ui.VerificationMeActivity;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Verification;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.util.HashMap;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;


/**
 * 회원 탈퇴 First - 진행방법 및 대기기간
 */
public class SecessionPreCautionFragment extends BaseFragment<SecessionActivity>{


    @Override
    public String getPID(){

        return "";
    }

    @Override
    public int getLayoutResourceId(){

        return R.layout.fragment_secession_pre_caution;
    }

    @Override
    public void initializeView(View container){

        ((TextView) container.findViewById(R.id.tv_secession_caution)).setText(PplusCommonUtil.Companion.fromHtml(getResources().getString(R.string.html_msg_leave_caution, FormatUtil.getMoneyType(""+LoginInfoManager.getInstance().getUser().getTotalCash()), FormatUtil.getMoneyType(""+LoginInfoManager.getInstance().getUser().getTotalBol()))));
        ((TextView) container.findViewById(R.id.tv_secession_pre_caution)).setText(PplusCommonUtil.Companion.fromHtml(getResources().getString(R.string.html_msg_leave_pre_caution)));

        container.findViewById(R.id.text_secession).setOnClickListener(this);
    }

    public SecessionPreCautionFragment(){
        // Required empty public constructor
    }

    @Override
    public void onClick(View v){

        switch (v.getId()) {
            case R.id.text_secession:

                if(PplusCommonUtil.Companion.getCountryCode().equalsIgnoreCase("kr")) {
                    AlertBuilder.Builder builder = new AlertBuilder.Builder();
                    builder.setTitle(getString(R.string.word_notice_alert));
                    builder.addContents(new AlertData.MessageData(getString(R.string.msg_verification_me_alert_secession), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
                    builder.addContents(new AlertData.MessageData(getString(R.string.msg_move_verification), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
                    builder.setOnAlertResultListener(new OnAlertResultListener(){

                        @Override
                        public void onCancel(){

                        }

                        @Override
                        public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                            switch (event_alert) {
                                case RIGHT:
                                    Intent intent = new Intent(getActivity(), VerificationMeActivity.class);
                                    intent.putExtra(Const.KEY, Const.VERIFICATION);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    getActivity().startActivityForResult(intent, Const.REQ_LEAVE);
                                    break;
                            }
                        }
                    }).builder().show(getActivity());

                } else {
                    getParentActivity().secessionAuth();
                }

                break;
        }
    }

    @Override
    public void init(){

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Const.REQ_LEAVE:
                    if(data != null) {
                        Verification verification = data.getParcelableExtra(Const.VERIFICATION);
                        if(verification == null) {
                            return;
                        }
                        Map<String, String> params = new HashMap<>();
                        params.put("number", verification.getNumber());
                        params.put("token", verification.getToken());
                        showProgress("");
                        ApiBuilder.create().leave(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

                            @Override
                            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){

                                hideProgress();
                                getParentActivity().setLeave(true);
                                getParentActivity().secessionResult();
                            }

                            @Override
                            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){

                                hideProgress();
                            }
                        }).build().call();
                    }

                    break;
            }
        }

    }
}
