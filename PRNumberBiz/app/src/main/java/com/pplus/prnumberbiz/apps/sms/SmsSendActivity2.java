//package com.pplus.prnumberbiz.apps.sms;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.telephony.PhoneNumberUtils;
//import android.text.Editable;
//import android.text.Html;
//import android.text.TextWatcher;
//import android.view.MotionEvent;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.format.FormatUtil;
//import com.pple.pplus.utils.part.pref.PreferenceUtil;
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.ads.ui.SelectAdvertiseActivity;
//import com.pplus.prnumberbiz.apps.billing.ui.CashBillingActivity;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
//import com.pplus.prnumberbiz.apps.common.mgmt.CountryConfigManager;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.coupon.ui.SelectCouponActivity;
//import com.pplus.prnumberbiz.apps.customer.ui.SelectCustomerActivity;
//import com.pplus.prnumberbiz.apps.customer.ui.SmsLockerActivity;
//import com.pplus.prnumberbiz.apps.guide.ui.GuideActivity;
//import com.pplus.prnumberbiz.apps.outgoingnumber.OutGoingNumberConfigActivity;
//import com.pplus.prnumberbiz.apps.push.ui.PushGuideActivity;
//import com.pplus.prnumberbiz.apps.push.ui.PushReservationActivity;
//import com.pplus.prnumberbiz.core.code.common.EnumData;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.Advertise;
//import com.pplus.prnumberbiz.core.network.model.dto.CouponTemplate;
//import com.pplus.prnumberbiz.core.network.model.dto.Customer;
//import com.pplus.prnumberbiz.core.network.model.dto.Msg;
//import com.pplus.prnumberbiz.core.network.model.dto.MsgProperties;
//import com.pplus.prnumberbiz.core.network.model.dto.MsgTarget;
//import com.pplus.prnumberbiz.core.network.model.dto.No;
//import com.pplus.prnumberbiz.core.network.model.dto.OutgoingNumber;
//import com.pplus.prnumberbiz.core.network.model.dto.Post;
//import com.pplus.prnumberbiz.core.network.model.dto.SavedMsg;
//import com.pplus.prnumberbiz.core.network.model.dto.SavedMsgProperties;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//import com.pplus.prnumberbiz.core.util.ToastUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Locale;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class SmsSendActivity2 extends BaseActivity implements ImplToolbar, View.OnClickListener{
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
//        return R.layout.activity_sms_send2;
//    }
//
//    private TextView text_select_contents, text_ads, text_deny_reception, text_sms_send_byte, text_select_customer, text_pre_use_cash, text_retention_cash, text_sms_send, text_sender;
//    private View text_check_ads, text_immediately_sent, text_reservation_sent;
//    private EditText edit_contents;
//
//    private ArrayList<Customer> mCustomerList;
//    private int mSmsCash;
//    private boolean isAds = true, mIsEditing = false;
//    //    private String webSendText, firstText, lastText;
//    private String firstText, lastText;
//    private EnumData.SendType mSendType;
//    private EnumData.SmsType mSmsType;
//    private String mSenderNo, mMaxText;
//    private EnumData.AdsType mAdsType;
//    private Advertise mAdvertise;
//
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        if(!PreferenceUtil.getDefaultPreference(this).getBoolean(Const.GUIDE_SMS)) {
//            Intent intent = new Intent(this, PushGuideActivity.class);
//            intent.putExtra(Const.KEY, Const.GUIDE_SMS);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivity(intent);
//        }
//
//        text_select_contents = (TextView) findViewById(R.id.text_sms_send_select_contents);
//        text_select_contents.setOnClickListener(this);
//
//        if(getIntent().getBooleanExtra(Const.COUPON, false)) {
//            text_select_contents.setVisibility(View.VISIBLE);
//        } else {
//            text_select_contents.setVisibility(View.GONE);
//        }
//
//        text_select_customer = (TextView) findViewById(R.id.text_sms_send_select_customer);
//        text_select_customer.setOnClickListener(this);
//
//        text_pre_use_cash = (TextView) findViewById(R.id.text_sms_send_pre_use_cash);
//        text_retention_cash = (TextView) findViewById(R.id.text_sms_send_retention_cash);
//
//        text_immediately_sent = findViewById(R.id.text_sms_send_immediately_sent);
//        text_reservation_sent = findViewById(R.id.text_sms_send_reservation_sent);
//        text_immediately_sent.setOnClickListener(this);
//        text_reservation_sent.setOnClickListener(this);
//
//        text_sms_send = (TextView) findViewById(R.id.text_sms_send);
//        text_sms_send.setText(R.string.word_send_sms);
//        text_sms_send.setOnClickListener(this);
//
//        text_check_ads = findViewById(R.id.text_sms_send_check_ads);
//        text_check_ads.setOnClickListener(this);
//        text_ads = (TextView) findViewById(R.id.text_sms_send_ads);
//        text_deny_reception = (TextView) findViewById(R.id.text_sms_send_deny_reception);
//        text_deny_reception.setText(getString(R.string.format_sms_free_deny_reception, String.valueOf(LoginInfoManager.getInstance().getUser().getPage().getNo())));
//        text_sms_send_byte = (TextView) findViewById(R.id.text_sms_send_byte);
//        edit_contents = (EditText) findViewById(R.id.edit_sms_send_contents);
//
//        edit_contents.setOnTouchListener(new View.OnTouchListener(){
//
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent){
//
//                if(edit_contents.length() > 0) {
//                    view.getParent().requestDisallowInterceptTouchEvent(true);
//                }
//
//                return false;
//            }
//        });
//
//        findViewById(R.id.text_sms_send_save).setOnClickListener(this);
//        findViewById(R.id.text_sms_send_locker).setOnClickListener(this);
//        findViewById(R.id.text_sms_send_charge_cash).setOnClickListener(this);
//
//        edit_contents.addTextChangedListener(new TextWatcher(){
//
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2){}
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2){}
//
//            @Override
//            public void afterTextChanged(Editable editable){
//
//                mIsEditing = true;
//                String contents = editable.toString();
//                int bytes = 0;
//                if(isAds) {
//                    bytes = firstText.getBytes().length + contents.getBytes().length + lastText.getBytes().length;
//                } else {
//                    bytes = contents.getBytes().length;
//                }
//
//                if(bytes > 2000) {
//                    edit_contents.setText(mMaxText);
//                } else {
//                    mMaxText = contents;
//                }
//
//                setBytes(bytes);
//            }
//        });
//
//        text_sender = (TextView) findViewById(R.id.text_sms_send_select_sender);
//        text_sender.setOnClickListener(this);
//
//        //        webSendText = getString(R.string.word_web_send) + "\n";
//        isAds = true;
//        text_ads.setText(getString(R.string.word_sms_ads2) + LoginInfoManager.getInstance().getUser().getPage().getName());
//        firstText = text_ads.getText().toString() + "\n";
//        lastText = "\n" + "\n" + text_deny_reception.getText().toString();
//        visibleAds(isAds);
//        mSmsCash = CountryConfigManager.getInstance().getConfig().getProperties().getSmsPrice();
//        mSmsType = EnumData.SmsType.SMS;
//        mSendType = EnumData.SendType.immediately;
//        mSenderNo = LoginInfoManager.getInstance().getUser().getMobile();
//        text_sender.setText(mSenderNo);
//        setSelect(text_immediately_sent, text_reservation_sent);
//
//        text_pre_use_cash.setText(getString(R.string.format_money_unit, "0"));
//        PplusCommonUtil.Companion.reloadSession(new PplusCommonUtil.Companion.ReloadListener(){
//
//            @Override
//            public void reload(){
//
//                text_retention_cash.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_retention_cash, FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().getUser().getTotalCash()))));
//            }
//        });
//    }
//
//    private void resetData(){
//
//        mIsEditing = false;
//        mCustomerList = null;
//        mSendType = EnumData.SendType.immediately;
//        setSelect(text_immediately_sent, text_reservation_sent);
//        text_sms_send.setText(R.string.word_sms_send);
//        text_pre_use_cash.setText(getString(R.string.format_money_unit, "0"));
//        text_select_customer.setText("");
//        edit_contents.setText("");
//        isAds = true;
//        firstText = text_ads.getText().toString() + LoginInfoManager.getInstance().getUser().getPage().getName() + "\n";
//        lastText = "\n" + text_deny_reception.getText().toString();
//        visibleAds(isAds);
//        mSmsCash = CountryConfigManager.getInstance().getConfig().getProperties().getSmsPrice();
//        mSmsType = EnumData.SmsType.SMS;
//        text_retention_cash.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_retention_cash, FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().getUser().getTotalCash()))));
//    }
//
//    private void setSelect(View view1, View view2){
//
//        view1.setSelected(true);
//        view2.setSelected(false);
//    }
//
//    private void setBytes(int bytes){
//
//        if(bytes <= 90) {
//            if(isAds) {
//                mSmsCash = CountryConfigManager.getInstance().getConfig().getProperties().getAdSmsPrice();
//            } else {
//                mSmsCash = CountryConfigManager.getInstance().getConfig().getProperties().getSmsPrice();
//            }
//
//            mSmsType = EnumData.SmsType.SMS;
//            text_sms_send_byte.setText(getString(R.string.format_sms_byte, bytes, 90));
//        } else {
//            if(isAds) {
//                mSmsCash = CountryConfigManager.getInstance().getConfig().getProperties().getAdLmsPrice();
//            } else {
//                mSmsCash = CountryConfigManager.getInstance().getConfig().getProperties().getLmsPrice();
//            }
//
//            mSmsType = EnumData.SmsType.LMS;
//            text_sms_send_byte.setText(getString(R.string.format_sms_byte, bytes, 2000));
//        }
//
//        setPreUseCash();
//    }
//
//    private void visibleAds(boolean visible){
//
//        text_check_ads.setSelected(visible);
//        String contents = edit_contents.getText().toString();
//        if(visible) {
//            text_ads.setVisibility(View.VISIBLE);
//            text_deny_reception.setVisibility(View.VISIBLE);
//
//            setBytes(firstText.getBytes().length + contents.getBytes().length + lastText.getBytes().length);
//        } else {
//            text_ads.setVisibility(View.GONE);
//            text_deny_reception.setVisibility(View.GONE);
//            setBytes(contents.getBytes().length);
//        }
//
//    }
//
//    @Override
//    public void onClick(View view){
//
//        Intent intent;
//        switch (view.getId()) {
//            case R.id.text_sms_send_check_ads:
//                mIsEditing = true;
//                if(isAds) {
//                    AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                    builder.setTitle(getString(R.string.word_notice_alert));
//                    builder.addContents(new AlertData.MessageData(getString(R.string.msg_alert_sms_ads1), AlertBuilder.MESSAGE_TYPE.TEXT, 3));
//                    builder.addContents(new AlertData.MessageData(getString(R.string.msg_alert_sms_ads2), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
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
//                                    isAds = false;
//                                    visibleAds(isAds);
//                                    break;
//                            }
//                        }
//                    }).builder().show(this);
//                } else {
//                    isAds = true;
//                    visibleAds(isAds);
//                }
//
//                break;
//            case R.id.text_sms_send_immediately_sent:
//                if(mSendType.equals(EnumData.SendType.reservation)) {
//                    setSelect(text_immediately_sent, text_reservation_sent);
//                    text_sms_send.setText(R.string.word_sms_send);
//                    mSendType = EnumData.SendType.immediately;
//                    mIsEditing = true;
//                }
//
//                break;
//            case R.id.text_sms_send_reservation_sent:
//                if(mSendType.equals(EnumData.SendType.immediately)) {
//                    mSendType = EnumData.SendType.reservation;
//                    setSelect(text_reservation_sent, text_immediately_sent);
//                    text_sms_send.setText(R.string.word_next);
//                    mIsEditing = true;
//                }
//
//                break;
//            case R.id.text_sms_send_charge_cash:
//                intent = new Intent(this, CashBillingActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivityForResult(intent, Const.REQ_CASH_CHANGE);
//                break;
//            case R.id.text_sms_send_select_contents:
//                intent = new Intent(this, SelectAdvertiseActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivityForResult(intent, Const.REQ_CONTENTS);
//                break;
//            case R.id.text_sms_send_select_customer:
//                intent = new Intent(this, SelectCustomerActivity.class);
//                intent.putExtra(Const.USER, EnumData.CustomerType.customer);
//                if(mCustomerList != null && mCustomerList.size() > 0) {
//                    intent.putParcelableArrayListExtra(Const.CUSTOMER, mCustomerList);
//                }
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivityForResult(intent, Const.REQ_SEARCH);
//                break;
//            case R.id.text_sms_send:
//                final Msg msg = new Msg();
//
//                msg.setType(mSmsType.name().toLowerCase());
//                msg.setReserved(false);
//
//                if(StringUtils.isEmpty(edit_contents.getText().toString().trim())) {
//                    showAlert(R.string.msg_input_contents);
//                    return;
//                }
//
//                StringBuilder contents = new StringBuilder();
//                //                contents.append(webSendText);
//                if(isAds) {
//                    contents.append(firstText);
//                }
//                contents.append(edit_contents.getText().toString().trim());
//                if(isAds) {
//                    contents.append(lastText);
//                }
//
//                msg.setContents(contents.toString());
//
//                if(mCustomerList == null || mCustomerList.size() == 0) {
//                    showAlert(R.string.msg_select_customer);
//                    return;
//                }
//
//                int useCash = mSmsCash * mCustomerList.size();
//                int retentionCash = LoginInfoManager.getInstance().getUser().getTotalCash();
//                if(useCash > retentionCash) {
//                    showAlert(R.string.msg_charge_cash);
//                    return;
//                }
//                msg.setTotalPrice(useCash);
//                List<MsgTarget> targetList = new ArrayList<>();
//                for(Customer customer : mCustomerList) {
//                    MsgTarget target = new MsgTarget();
//                    target.setMobile(customer.getMobile());
//                    target.setName(customer.getName());
//                    targetList.add(target);
//                }
//
//                msg.setTargetList(targetList);
//                MsgProperties properties = new MsgProperties();
//                properties.setSenderNo(mSenderNo);
//                properties.setAdvertise(isAds);
//                msg.setProperties(properties);
//
//                switch (mSendType) {
//
//                    case reservation:
//                        msg.setReserved(true);
//                        intent = new Intent(this, PushReservationActivity.class);
//                        intent.putExtra(Const.KEY, EnumData.MsgType.sms);
//                        intent.putExtra(Const.DATA, msg);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                        startActivityForResult(intent, Const.REQ_RESERVATION);
//                        break;
//                    case immediately:
//                        sendSms(msg);
//                        break;
//                }
//                break;
//            case R.id.text_sms_send_save:
//                SavedMsg savedMsg = new SavedMsg();
//                savedMsg.setUser(new No(LoginInfoManager.getInstance().getUser().getNo()));
//                if(StringUtils.isEmpty(edit_contents.getText().toString().trim())) {
//                    showAlert(R.string.msg_input_contents);
//                    return;
//                }
//
//                StringBuilder savedContents = new StringBuilder();
//                //                savedContents.append(webSendText);
//                //                if(isAds){
//                //                    savedContents.append(firstText);
//                //                }
//                savedContents.append(edit_contents.getText().toString().trim());
//                //                if(isAds){
//                //                    savedContents.append(lastText);
//                //                }
//                savedMsg.setPriority(1);
//                SavedMsgProperties savedMsgProperties = new SavedMsgProperties();
//                savedMsgProperties.setMsg(savedContents.toString());
//                savedMsgProperties.setAdvertise(isAds);
//
//                savedMsg.setProperties(savedMsgProperties);
//
//                showProgress("");
//                ApiBuilder.create().insertSavedMsg(savedMsg).setCallback(new PplusCallback<NewResultResponse<SavedMsg>>(){
//
//                    @Override
//                    public void onResponse(Call<NewResultResponse<SavedMsg>> call, NewResultResponse<SavedMsg> response){
//
//                        hideProgress();
//                        showAlert(R.string.msg_saved_msg);
//                    }
//
//                    @Override
//                    public void onFailure(Call<NewResultResponse<SavedMsg>> call, Throwable t, NewResultResponse<SavedMsg> response){
//
//                        hideProgress();
//                    }
//                }).build().call();
//
//                break;
//            case R.id.text_sms_send_locker:
//                intent = new Intent(this, SmsLockerActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivityForResult(intent, Const.REQ_SMS_LOCKER);
//                break;
//            case R.id.text_sms_send_select_sender:
//                getList();
//                break;
//        }
//    }
//
//    private void getList(){
//
//        showProgress("");
//        ApiBuilder.create().getAuthedNumberAll().setCallback(new PplusCallback<NewResultResponse<OutgoingNumber>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<OutgoingNumber>> call, NewResultResponse<OutgoingNumber> response){
//
//                hideProgress();
//
//                List<OutgoingNumber> dataList = response.getDatas();
//
//                if(dataList != null && dataList.size() > 0) {
//                    final String[] numberList = new String[dataList.size()];
//                    for(int i = 0; i < dataList.size(); i++) {
//                        numberList[i] = PhoneNumberUtils.formatNumber(dataList.get(i).getMobile(), Locale.getDefault().getCountry());
//                    }
//
//                    AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                    builder.setTitle(getString(R.string.word_select_outgoing_number));
//                    builder.setContents(numberList).setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm)).setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_RADIO);
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
//                            if(event_alert.equals(AlertBuilder.EVENT_ALERT.LEFT)) {
//                                return;
//                            }
//                            mSenderNo = numberList[event_alert.getValue() - 1].replace("-", "");
//                            text_sender.setText(mSenderNo);
//                        }
//                    }).builder().show(SmsSendActivity2.this);
//                } else {
//                    showAlert(R.string.msg_not_exist_outgoing_number_list);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<OutgoingNumber>> call, Throwable t, NewResultResponse<OutgoingNumber> response){
//
//            }
//        }).build().call();
//    }
//
//    private void setPreUseCash(){
//
//        if(mCustomerList != null && mCustomerList.size() > 0) {
//            text_pre_use_cash.setText(getString(R.string.format_money_unit, FormatUtil.getMoneyType(String.valueOf(mSmsCash * mCustomerList.size()))));
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case Const.REQ_CONTENTS:
//                    mIsEditing = true;
//                    mAdvertise = data.getParcelableExtra(Const.ADVERTISE);
//                    mAdsType = (EnumData.AdsType)data.getSerializableExtra(Const.TYPE);
//                    switch (mAdsType){
//
//                        case article:
//                            String url = getString(R.string.format_msg_invite_url, "pr_detail.php?pageNo="+LoginInfoManager.getInstance().getUser().getPage().getNo()+"&prDetailNo="+mAdvertise.getArticle().getNo());
//                            edit_contents.append("\n\n"+url);
//                            break;
//                        case coupon:
//                            url = getString(R.string.format_msg_invite_url, "coupon_detail.php?pageNo="+LoginInfoManager.getInstance().getUser().getPage().getNo()+"&couponNo="+mAdvertise.getTemplate().getNo());
//                            edit_contents.append("\n\n"+url);
//                            break;
//                    }
//                    break;
//                case Const.REQ_SEARCH:
//                    mIsEditing = true;
//                    mCustomerList = data.getParcelableArrayListExtra(Const.CUSTOMER);
//                    if(mCustomerList != null && mCustomerList.size() > 0) {
//                        if(mCustomerList.size() == 1) {
//                            text_select_customer.setText(mCustomerList.get(0).getName());
//                        } else {
//                            text_select_customer.setText(getString(R.string.format_other, mCustomerList.get(0).getName(), mCustomerList.size() - 1));
//                        }
//
//                        setPreUseCash();
//                    }
//                    break;
//                case Const.REQ_RESERVATION:
//                    resetData();
//                    break;
//                case Const.REQ_SMS_LOCKER:
//                    mIsEditing = true;
//                    if(data != null) {
//                        SavedMsg msg = data.getParcelableExtra(Const.MSG);
//                        edit_contents.setText(msg.getProperties().getMsg());
//                    }
//
//                    break;
//                case Const.REQ_CASH_CHANGE:
//                    PplusCommonUtil.Companion.reloadSession(new PplusCommonUtil.Companion.ReloadListener(){
//
//                        @Override
//                        public void reload(){
//
//                            text_retention_cash.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_retention_cash, FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().getUser().getTotalCash()))));
//                        }
//                    });
//                    break;
//            }
//        }
//    }
//
//    private void sendSms(final Msg msg){
//
//        AlertBuilder.Builder builder = new AlertBuilder.Builder();
//        builder.setTitle(getString(R.string.word_notice_alert));
//        builder.addContents(new AlertData.MessageData(getString(R.string.msg_sms_send_alert1), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//        builder.addContents(new AlertData.MessageData(getString(R.string.format_sms_send_alert2, mCustomerList.size()), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_sms_send));
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
//                    case RIGHT:
//                        showProgress("");
//                        ApiBuilder.create().insertSmsMsg(msg).setCallback(new PplusCallback<NewResultResponse<Msg>>(){
//
//                            @Override
//                            public void onResponse(final Call<NewResultResponse<Msg>> call, NewResultResponse<Msg> response){
//
//                                hideProgress();
//                                setResult(RESULT_OK);
//                                AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                                builder.setTitle(getString(R.string.word_notice_alert));
//                                if(msg.isReserved()) {
//                                    builder.addContents(new AlertData.MessageData(getString(R.string.msg_success_reserved_sms), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//                                } else {
//                                    builder.addContents(new AlertData.MessageData(getString(R.string.msg_success_send_sms), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//                                }
//
//                                builder.addContents(new AlertData.MessageData(getString(R.string.msg_success_send), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//                                builder.setLeftText(getString(R.string.word_confirm));
//                                builder.setAutoCancel(false);
//                                builder.setBackgroundClickable(false);
//                                builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//                                    @Override
//                                    public void onCancel(){
//
//                                    }
//
//                                    @Override
//                                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                                        switch (event_alert) {
//                                            case SINGLE:
//
//                                                PplusCommonUtil.Companion.reloadSession(new PplusCommonUtil.Companion.ReloadListener(){
//
//                                                    @Override
//                                                    public void reload(){
//
//                                                        resetData();
//                                                    }
//                                                });
//                                                break;
//                                        }
//                                    }
//                                }).builder().show(SmsSendActivity2.this);
//
//
//                            }
//
//                            @Override
//                            public void onFailure(Call<NewResultResponse<Msg>> call, Throwable t, NewResultResponse<Msg> response){
//
//                                hideProgress();
//                            }
//                        }).build().call();
//                        break;
//                }
//            }
//        }).builder().show(this);
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_sms_send), ToolbarOption.ToolbarMenu.LEFT);
//
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, R.drawable.ic_top_info);
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, R.drawable.ic_top_more);
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
//                        if(tag.equals(1)) {
//                            alertCaution();
//
//                        } else if(tag.equals(2)) {
//                            alertMore();
//
//                        }
//                        break;
//                }
//            }
//        };
//    }
//
//    private void alertMore(){
//
//        AlertBuilder.Builder builder = new AlertBuilder.Builder();
//        builder.setContents(getString(R.string.word_outgoing_number_new_reg), getString(R.string.word_send_result));
//        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(new OnAlertResultListener(){
//
//            @Override
//            public void onCancel(){
//
//            }
//
//            @Override
//            public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                switch (event_alert.getValue()) {
//                    case 1:
//                        newReg();
//                        break;
//                    case 2:
//                        history();
//                        break;
//                }
//            }
//        }).builder().show(this);
//    }
//
//    private void alertCaution(){
//
//        AlertBuilder.Builder builder = new AlertBuilder.Builder();
//        builder.setTitle(getString(R.string.word_send_pr_message));
//        builder.addContents(new AlertData.MessageData(getString(R.string.msg_sms_send_description2), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//        builder.addContents(new AlertData.MessageData(getString(R.string.msg_sms_send_description3), AlertBuilder.MESSAGE_TYPE.TEXT, 3));
//        builder.addContents(new AlertData.MessageData(getString(R.string.format_msg_sms_send_description4, CountryConfigManager.getInstance().getConfig().getProperties().getSmsPrice(), CountryConfigManager.getInstance().getConfig().getProperties().getLmsPrice(), CountryConfigManager.getInstance().getConfig().getProperties().getAdSmsPrice(), CountryConfigManager.getInstance().getConfig().getProperties().getAdLmsPrice()), AlertBuilder.MESSAGE_TYPE.TEXT, 3));
//        builder.setLeftText(getString(R.string.word_confirm));
//        builder.builder().show(this);
//    }
//
//    private void newReg(){
//
//        Intent intent = new Intent(this, OutGoingNumberConfigActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(intent);
//    }
//
//    private void history(){
//
//        Intent intent = new Intent(this, SmsResultActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//        startActivity(intent);
//    }
//
//    @Override
//    public void onBackPressed(){
//
//        if(mIsEditing) {
//            AlertBuilder.Builder builder = new AlertBuilder.Builder();
//            builder.setTitle(getString(R.string.word_notice_alert));
//            builder.addContents(new AlertData.MessageData(getString(R.string.msg_question_back), AlertBuilder.MESSAGE_TYPE.TEXT, 3));
//            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
//            builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//                @Override
//                public void onCancel(){
//
//                }
//
//                @Override
//                public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                    switch (event_alert) {
//                        case RIGHT:
//                            finish();
//                            break;
//                    }
//                }
//            }).builder().show(this);
//        } else {
//            super.onBackPressed();
//        }
//
//    }
//}
