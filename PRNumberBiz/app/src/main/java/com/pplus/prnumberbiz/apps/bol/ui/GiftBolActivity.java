//package com.pplus.prnumberbiz.apps.bol.ui;
//
//import android.content.Intent;
//import android.graphics.Typeface;
//import android.support.annotation.NonNull;
//import android.os.Bundle;
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.google.gson.JsonObject;
//import com.pple.pplus.utils.part.apps.resource.ResourceUtil;
//import com.pple.pplus.utils.part.format.FormatUtil;
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.customer.ui.SelectBolUserActivity;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.Bol;
//import com.pplus.prnumberbiz.core.network.model.dto.BolProperties;
//import com.pplus.prnumberbiz.core.network.model.dto.User;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//
//import java.util.ArrayList;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class GiftBolActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{
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
//        return R.layout.activity_gift_bol;
//    }
//
//    private EditText edit_bol, edit_message;
//    private TextView text_retention_bol, text_down_bol;
//    private ArrayList<User> mTargetList;
//    private TextView text_gift_bol_search;
//    private View text_description2, layout_down_bol;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        mTargetList = new ArrayList<>();
//
//        edit_bol = (EditText) findViewById(R.id.edit_gift_bol_point);
//        text_retention_bol = (TextView) findViewById(R.id.text_gift_bol_retention_bol);
//        setRetentionBol();
//
//        edit_message = (EditText) findViewById(R.id.edit_gift_bol_msg);
//        text_gift_bol_search = (TextView) findViewById(R.id.text_gift_bol_search);
//
//        text_description2 = findViewById(R.id.text_gift_bol_description2);
//        layout_down_bol = findViewById(R.id.layout_gift_bol_down_bol);
//        text_down_bol = (TextView) findViewById(R.id.text_gift_bol_down_bol);
//        text_description2.setVisibility(View.VISIBLE);
//        layout_down_bol.setVisibility(View.GONE);
//
//        findViewById(R.id.layout_gift_bol_search).setOnClickListener(this);
//        findViewById(R.id.layout_gift_bol_myCustomer).setOnClickListener(this);
//        findViewById(R.id.text_gift_bol_gift).setOnClickListener(this);
//
//        edit_bol.addTextChangedListener(new TextWatcher(){
//
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable){
//
//                String amount = editable.toString();
//                if(StringUtils.isNotEmpty(amount) && (Integer.valueOf(amount) > 0)) {
//                    if(mTargetList != null && mTargetList.size() > 0) {
//                        text_description2.setVisibility(View.GONE);
//                        layout_down_bol.setVisibility(View.VISIBLE);
//                        int totalAmount = Integer.valueOf(amount) * mTargetList.size();
//                        text_down_bol.setText(FormatUtil.getMoneyType(String.valueOf(totalAmount)) + "P");
//                    }
//                } else {
//                    text_description2.setVisibility(View.VISIBLE);
//                    layout_down_bol.setVisibility(View.GONE);
//                }
//
//            }
//        });
//    }
//
//    private void setRetentionBol(){
//
//        PplusCommonUtil.Companion.reloadSession(new PplusCommonUtil.Companion.ReloadListener(){
//
//            @Override
//            public void reload(){
//
//                text_retention_bol.setText(FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().getUser().getTotalBol()) + "P");
//            }
//        });
//    }
//
//    @Override
//    public void onClick(View view){
//
//        Intent intent = null;
//        switch (view.getId()) {
//            case R.id.layout_gift_bol_search:
//                intent = new Intent(this, GiftSearchActivity.class);
//                if(mTargetList != null && mTargetList.size() > 0) {
//                    intent.putParcelableArrayListExtra(Const.DATA, mTargetList);
//                }
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivityForResult(intent, Const.REQ_SEARCH);
//                break;
//            case R.id.layout_gift_bol_myCustomer:
//                intent = new Intent(this, SelectBolUserActivity.class);
//                if(mTargetList != null && mTargetList.size() > 0) {
//                    intent.putParcelableArrayListExtra(Const.DATA, mTargetList);
//                }
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivityForResult(intent, Const.REQ_SEARCH);
//                break;
//            case R.id.text_gift_bol_gift:
//                final String amount = edit_bol.getText().toString().trim();
//                if(StringUtils.isEmpty(amount) || (Integer.valueOf(amount) == 0)) {
//                    showAlert(R.string.msg_input_gift_bol);
//                    return;
//                }
//
//                final Bol params = new Bol();
//                if(mTargetList == null || mTargetList.size() == 0) {
//                    showAlert(R.string.msg_select_receiver);
//                    return;
//                }
//
//                params.setAmount(amount);
//                params.setTargetList(mTargetList);
//                final int totalAmount = Integer.valueOf(amount) * mTargetList.size();
//
//                if(totalAmount > LoginInfoManager.getInstance().getUser().getTotalBol()) {
//                    AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                    builder.setTitle(getString(R.string.word_notice_alert));
//                    builder.addContents(new AlertData.MessageData(getString(R.string.msg_lack_retention_bol), AlertBuilder.MESSAGE_TYPE.TEXT, 3));
//                    builder.addContents(new AlertData.MessageData(getString(R.string.msg_question_charge_bol), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
//                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
//                    builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//                        @Override
//                        public void onCancel(){
//
//                        }
//
//                        @Override
//                        public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                            switch (event_alert) {
//                                case RIGHT:
//                                    Intent intent = new Intent(GiftBolActivity.this, BolBillingActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                    startActivityForResult(intent, Const.REQ_CASH_CHANGE);
//                                    break;
//                            }
//                        }
//                    }).builder().show(GiftBolActivity.this);
//                    return;
//                }
//
//                String message = edit_message.getText().toString().trim();
//                if(StringUtils.isNotEmpty(message)) {
//                    JsonObject properties = new JsonObject();
//                    properties.addProperty("message", message);
//                    params.setProperties(properties);
//                }
//
//                AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                builder.setTitle(getString(R.string.word_notice_alert));
//                builder.addContents(new AlertData.MessageData(getString(R.string.format_bol_down, FormatUtil.getMoneyType(String.valueOf(totalAmount))), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
//
//                String strAlert = null;
//                if(mTargetList.size() == 1) {
//                    strAlert = getString(R.string.format_msg_gift_bol_alert, mTargetList.get(0).getNickname());
//                } else {
//                    strAlert = getString(R.string.format_msg_gift_bol_alert, getString(R.string.format_other, "" + mTargetList.get(0).getNickname(), mTargetList.size() - 1));
//                }
//                builder.addContents(new AlertData.MessageData(strAlert, AlertBuilder.MESSAGE_TYPE.TEXT, 2));
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
//
//                        switch (event_alert) {
//                            case RIGHT:
//
//                                showProgress("");
//                                ApiBuilder.create().giftBols(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//                                    @Override
//                                    public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//
//                                        hideProgress();
//                                        showAlert(R.string.msg_gifted_bol);
//                                        setResult(RESULT_OK);
//                                        finish();
//                                    }
//
//                                    @Override
//                                    public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
//
//                                    }
//                                }).build().call();
//                                break;
//                        }
//                    }
//                }).builder().show(this);
//
//
//                break;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case Const.REQ_SEARCH:
//                if(resultCode == RESULT_OK) {
//                    if(data != null) {
//                        mTargetList = data.getParcelableArrayListExtra(Const.DATA);
//                        if(mTargetList != null && mTargetList.size() > 0) {
//                            if(mTargetList.size() == 1) {
//                                text_gift_bol_search.setText("" + mTargetList.get(0).getNickname());
//                            } else {
//                                text_gift_bol_search.setText(getString(R.string.format_other, "" + mTargetList.get(0).getNickname(), mTargetList.size() - 1));
//                            }
//                            String amount = edit_bol.getText().toString().trim();
//                            if(StringUtils.isNotEmpty(amount) && (Integer.valueOf(amount) > 0)) {
//                                text_description2.setVisibility(View.GONE);
//                                layout_down_bol.setVisibility(View.VISIBLE);
//                                int totalAmount = Integer.valueOf(amount) * mTargetList.size();
//                                text_down_bol.setText(FormatUtil.getMoneyType(String.valueOf(totalAmount)) + "P");
//                            } else {
//                                text_description2.setVisibility(View.VISIBLE);
//                                layout_down_bol.setVisibility(View.GONE);
//                            }
//                        } else {
//                            text_description2.setVisibility(View.VISIBLE);
//                            layout_down_bol.setVisibility(View.GONE);
//                        }
//
//                    }
//                }
//
//
//                break;
//            case Const.REQ_CASH_CHANGE:
//                setRetentionBol();
//                break;
//        }
//
//    }
//
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_gift_lucky_bol), ToolbarOption.ToolbarMenu.LEFT);
//
//        View view = getLayoutInflater().inflate(R.layout.item_top_right, null);
//        TextView textView = (TextView) view.findViewById(R.id.text_top_right);
//        textView.setText(R.string.word_gift_history);
//        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
//        textView.setTextColor(ResourceUtil.getColor(this, R.color.white));
//        textView.setBackgroundColor(ResourceUtil.getColor(this, R.color.color_8700ff));
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, view, 0);
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
//                    case RIGHT:
////                        Intent intent = new Intent(GiftBolActivity.this, GiftHistoryActivity.class);
////                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
////                        startActivity(intent);
//                        break;
//                }
//            }
//        };
//    }
//}
