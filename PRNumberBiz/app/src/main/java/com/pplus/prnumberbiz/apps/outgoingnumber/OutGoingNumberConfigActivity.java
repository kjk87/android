package com.pplus.prnumberbiz.apps.outgoingnumber;

import android.content.Intent;
import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.telephony.PhoneNumberUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.pple.pplus.utils.part.format.FormatUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.outgoingnumber.data.OutgoingNumberAdapter;
import com.pplus.prnumberbiz.apps.signup.ui.VerificationMeActivity;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.OutgoingNumber;
import com.pplus.prnumberbiz.core.network.model.dto.Verification;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class OutGoingNumberConfigActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_out_going_number_config;
    }

    private OutgoingNumberAdapter mAdapter;
    private EditText edit_number, edit_verification_number;
    private View layout_verification_me, layout_verification_wireline;
    private String outgoingNumber, mAuthedNumber;
    private Verification mVerification;

    @Override
    public void initializeView(Bundle savedInstanceState){

        edit_number = (EditText) findViewById(R.id.edit_outgoing_number);
        edit_verification_number = (EditText) findViewById(R.id.edit_outgoing_number_wirline_verification_number);
        layout_verification_me = findViewById(R.id.layout_outgoing_number_verification_me);
        layout_verification_wireline = findViewById(R.id.layout_outgoing_number_verification_wireline);

        findViewById(R.id.text_outgoing_number_confirm).setOnClickListener(this);
        findViewById(R.id.text_outgoing_number_verification_me).setOnClickListener(this);
        findViewById(R.id.text_outgoing_number_wirline_verification).setOnClickListener(this);
        findViewById(R.id.text_outgoing_number_wirline_verification_confirm).setOnClickListener(this);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_outgoing_number);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new OutgoingNumberAdapter(this);
        recyclerView.setAdapter(mAdapter);
        init();
    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.text_outgoing_number_confirm:
                outgoingNumber = edit_number.getText().toString().trim();

                if(StringUtils.isEmpty(outgoingNumber)) {
                    showAlert(R.string.msg_input_outgoing_number);
                    return;
                }

                if(!FormatUtil.isPhoneNumber(outgoingNumber)) {
                    showAlert(R.string.msg_enable_reg_outgoing_number);
                    return;
                }

                mVerification = null;
                PplusCommonUtil.Companion.hideKeyboard(this);
                if(FormatUtil.isCellPhoneNumber(outgoingNumber)) {
                    ((TextView) findViewById(R.id.text_outgoing_number_mobile)).setText(PhoneNumberUtils.formatNumber(outgoingNumber, Locale.getDefault().getCountry()));
                    layout_verification_me.setVisibility(View.VISIBLE);
                    layout_verification_wireline.setVisibility(View.GONE);
                } else {
                    ((TextView) findViewById(R.id.text_outgoing_number_wireline)).setText(PhoneNumberUtils.formatNumber(outgoingNumber, Locale.getDefault().getCountry()));
                    layout_verification_me.setVisibility(View.GONE);
                    layout_verification_wireline.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.text_outgoing_number_verification_me:
                verificationMobile(outgoingNumber);
                break;
            case R.id.text_outgoing_number_wirline_verification:
                verification(outgoingNumber);
                break;
            case R.id.text_outgoing_number_wirline_verification_confirm:
                if(mVerification == null) {
                    showAlert(R.string.msg_request_verification);
                    return;
                }

                String verificationNumber = edit_verification_number.getText().toString().trim();
                if(StringUtils.isEmpty(verificationNumber)) {
                    showAlert(R.string.msg_input_verificationNumber);
                    return;
                }
                PplusCommonUtil.Companion.hideKeyboard(this);
                mVerification.setNumber(verificationNumber);

                confirm(mVerification, outgoingNumber);
                break;
        }
    }

    public void init(){
        layout_verification_me.setVisibility(View.GONE);
        layout_verification_wireline.setVisibility(View.GONE);
        mVerification = null;
        outgoingNumber = "";
        mAuthedNumber = "";
        getList();
    }

    private void getList(){

        showProgress("");
        ApiBuilder.create().getAuthedNumberAll().setCallback(new PplusCallback<NewResultResponse<OutgoingNumber>>(){

            @Override
            public void onResponse(Call<NewResultResponse<OutgoingNumber>> call, NewResultResponse<OutgoingNumber> response){

                hideProgress();
                mAdapter.clear();
                mAdapter.addAll(response.getDatas());
            }

            @Override
            public void onFailure(Call<NewResultResponse<OutgoingNumber>> call, Throwable t, NewResultResponse<OutgoingNumber> response){

            }
        }).build().call();
    }

    private void verification(String number){

        Map<String, String> params = new HashMap<>();
        params.put("loginId", LoginInfoManager.getInstance().getUser().getLoginId());
        params.put("media", "ars");
        params.put("mobile", number);
        showProgress("");
        ApiBuilder.create().verification(params).setCallback(new PplusCallback<NewResultResponse<Verification>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Verification>> call, NewResultResponse<Verification> response){

                hideProgress();
                mVerification = response.getData();
                edit_verification_number.requestFocus();
                showAlert(R.string.msg_requested_verification);
            }

            @Override
            public void onFailure(Call<NewResultResponse<Verification>> call, Throwable t, NewResultResponse<Verification> response){

                hideProgress();
                showAlert(R.string.msg_enable_reg_outgoing_number);
            }
        }).build().call();
    }

    private void verificationMobile(String number){

        Intent intent = new Intent(this, VerificationMeActivity.class);
        intent.putExtra(Const.KEY, Const.VERIFICATION);
        intent.putExtra(Const.MOBILE_NUMBER, number);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivityForResult(intent, Const.REQ_VERIFICATION);
    }

    private void confirm(Verification verification, final String number){

        Map<String, String> params = new HashMap<>();
        params.put("number", verification.getNumber());
        params.put("token", verification.getToken());
        showProgress("");
        ApiBuilder.create().confirmVerification(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
                hideProgress();
                mAuthedNumber = number;
                insertNumber(mAuthedNumber);
            }

            @Override
            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
                hideProgress();
            }
        }).build().call();
    }

    private void insertNumber(String number){
        Map<String, String> params = new HashMap<>();
        params.put("mobile", number);
        showProgress("");
        ApiBuilder.create().insertAuthedNumber(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
                hideProgress();
                init();
            }

            @Override
            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){

            }
        }).build().call();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK) {
            switch (requestCode) {
                case Const.REQ_VERIFICATION:
                    if(data != null) {
                        final String mobileNumber = data.getStringExtra(Const.MOBILE_NUMBER);
                        Verification verification = data.getParcelableExtra(Const.VERIFICATION);
                        confirm(verification, mobileNumber);
                    }
                    break;
            }
        }
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_outgoing_number_config), ToolbarOption.ToolbarMenu.RIGHT);

        return toolbarOption;
    }

    @Override
    public OnToolbarListener getOnToolbarClickListener(){

        return new OnToolbarListener(){

            @Override
            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag){

                switch (toolbarMenu) {
                    case RIGHT:
                        if(tag.equals(1)) {
                            onBackPressed();
                        }
                        break;
                }
            }
        };
    }
}
