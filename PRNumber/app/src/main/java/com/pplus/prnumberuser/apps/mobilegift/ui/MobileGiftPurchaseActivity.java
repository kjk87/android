//package com.pplus.prnumberuser.apps.mobilegift.ui;
//
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.ContactsContract;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.text.Editable;
//import android.text.Html;
//import android.text.TextWatcher;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.pplus.utils.part.format.FormatUtil;
//import com.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberuser.Const;
//import com.pplus.prnumberuser.R;
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberuser.apps.common.builder.data.AlertData;
//import com.pplus.prnumberuser.apps.common.mgmt.CountryConfigManager;
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberuser.apps.mobilegift.data.MobileGiftTargetAdapter;
//import com.pplus.prnumberuser.core.code.common.EnumData;
//import com.pplus.prnumberuser.core.network.ApiBuilder;
//import com.pplus.prnumberuser.core.network.model.dto.Customer;
//import com.pplus.prnumberuser.core.network.model.dto.MobileGiftPurchase;
//import com.pplus.prnumberuser.core.network.model.dto.MsgTarget;
//import com.pplus.prnumberuser.core.network.model.dto.No;
//import com.pplus.prnumberuser.core.network.model.dto.PgApproval;
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberuser.core.util.PplusCommonUtil;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import com.pplus.networks.common.PplusCallback;
//import retrofit2.Call;
//
//public class MobileGiftPurchaseActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{
//
//    @Override
//    public String getPID(){
//
//        return "Main_bolpoint_pointshop_buy";
//    }
//
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_mobile_gift_purchase;
//    }
//
//    private MobileGiftPurchase mPurchase;
//    private MobileGiftTargetAdapter mAdapter;
//    private RecyclerView mRecyclerView;
//    private int mHeight = 0;
//    private EditText edit_msg;
//    private TextView msg_count;
//    private String mMaxText;
//    private TextView text_retention_bol, text_product_count, text_total_price;
//    private View layout_pg, text_card, text_depositless, text_pg_mobile;
//
//    private long totalPrice;
//
//
//    private EnumData.PGType mPgType;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        mPurchase = getIntent().getParcelableExtra(Const.DATA);
//        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_mobile_gift_target);
//        mAdapter = new MobileGiftTargetAdapter(this);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setAdapter(mAdapter);
//
//        findViewById(R.id.text_mobile_gift_purchase_add).setOnClickListener(this);
//        findViewById(R.id.text_mobile_gift_target_all_delete).setOnClickListener(this);
//
//        ImageView image = (ImageView) findViewById(R.id.image_mobile_gift_purchase);
//        TextView text_name = (TextView) findViewById(R.id.text_mobile_gift_purchase_name);
//        TextView text_price = (TextView) findViewById(R.id.text_mobile_gift_purchase_price);
//        TextView text_use_term = (TextView) findViewById(R.id.text_mobile_gift_purchase_use_term);
//
//        Glide.with(this).load(mPurchase.getMobileGift().getBaseImage()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image);
//        text_name.setText(mPurchase.getMobileGift().getName());
//
//        text_price.setText(getString(R.string.format_product_price, FormatUtil.getMoneyType("" + PplusCommonUtil.Companion.calculateBol(mPurchase.getMobileGift().getSalesPrice()))));
//        text_use_term.setText(getString(R.string.format_use_term, mPurchase.getMobileGift().getUseTerm()));
//
//        edit_msg = (EditText) findViewById(R.id.edit_mobile_gift_purchase_msg);
//        msg_count = (TextView) findViewById(R.id.text_mobile_gift_purchase_msg_count);
//        setBytes(0);
//        edit_msg.addTextChangedListener(new TextWatcher(){
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after){
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count){
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s){
//
//                String contents = s.toString();
//
//                int bytes = contents.getBytes().length;
//
//                if(bytes > 300) {
//                    edit_msg.setText(mMaxText);
//                } else {
//                    mMaxText = contents;
//                }
//
//                setBytes(bytes);
//            }
//        });
//
//        findViewById(R.id.text_mobile_gift_purchase_to_me).setOnClickListener(this);
//
//        text_retention_bol = (TextView) findViewById(R.id.text_mobile_gift_purchase_retention_bol);
//
//        TextView text_product_price = (TextView) findViewById(R.id.text_mobile_gift_purchase_product_price);
//        text_product_price.setText(getString(R.string.format_bol_unit, FormatUtil.getMoneyType("" + PplusCommonUtil.Companion.calculateBol(mPurchase.getMobileGift().getSalesPrice()))));
//        text_product_count = (TextView) findViewById(R.id.text_mobile_gift_purchase_product_count);
//        text_total_price = (TextView) findViewById(R.id.text_mobile_gift_purchase_total_price);
//        findViewById(R.id.text_mobile_gift_purchase_my_address_book).setOnClickListener(this);
//        findViewById(R.id.text_mobile_gift_purchase).setOnClickListener(this);
//
//        layout_pg = findViewById(R.id.layout_mobile_gift_purchase_pg);
//        text_card = findViewById(R.id.text_mobile_gift_purchase_card);
//        text_depositless = findViewById(R.id.text_mobile_gift_purchase_depositless);
//        text_pg_mobile = findViewById(R.id.text_mobile_gift_purchase_mobile);
//        text_card.setOnClickListener(this);
//        text_depositless.setOnClickListener(this);
//        text_pg_mobile.setOnClickListener(this);
//        mPgType = EnumData.PGType.wcard;
//        selectedView(text_card, text_depositless, text_pg_mobile);
//        //        edit_use_bol = (EditText) findViewById(R.id.edit_mobile_gift_purchase_use_bol);
//        //        edit_use_bol.addTextChangedListener(mBolTextWatcher);
//        //
//        //        check_total_use = (CheckBox) findViewById(R.id.check_mobile_gift_purchase_total_use);
//        //        check_total_use.setOnCheckedChangeListener(mCheckedChangeListener);
//
//        setCash();
//
//        MsgTarget target = new MsgTarget();
//        mAdapter.add(target);
//        setData();
//    }
//
//    //    private TextWatcher mBolTextWatcher = new TextWatcher(){
//    //
//    //        @Override
//    //        public void beforeTextChanged(CharSequence s, int start, int count, int after){
//    //
//    //        }
//    //
//    //        @Override
//    //        public void onTextChanged(CharSequence s, int start, int before, int count){
//    //
//    //            String inputValue = s.toString().trim();
//    //            if(inputValue.startsWith("0")) {
//    //                if(inputValue.length() > 0) {
//    //                    edit_use_bol.setText(inputValue.substring(1));
//    //                } else {
//    //                    edit_use_bol.setText("");
//    //                }
//    //                return;
//    //            }
//    //
//    //            if(inputValue.length() > 0) {
//    //
//    //                useBol = Integer.valueOf(s.toString());
//    //
//    //                if(totalPrice > LoginInfoManager.getInstance().getUser().getTotalBol()) {
//    //                    if(useBol > LoginInfoManager.getInstance().getUser().getTotalBol()) {
//    //                        edit_use_bol.setText("" + LoginInfoManager.getInstance().getUser().getTotalBol());
//    //                        edit_use_bol.setSelection(edit_use_bol.getText().length());
//    //                    }
//    //                } else {
//    //                    if(useBol > totalPrice) {
//    //                        edit_use_bol.setText("" + totalPrice);
//    //                        edit_use_bol.setSelection(edit_use_bol.getText().length());
//    //                    }
//    //                }
//    //
//    //                setData();
//    //            } else {
//    //                useBol = 0;
//    //                setData();
//    //            }
//    //        }
//    //
//    //        @Override
//    //        public void afterTextChanged(Editable s){
//    //
//    //        }
//    //    };
//
//    //    private CompoundButton.OnCheckedChangeListener mCheckedChangeListener = new CompoundButton.OnCheckedChangeListener(){
//    //
//    //        @Override
//    //        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
//    //
//    //            if(isChecked) {
//    //                if(LoginInfoManager.getInstance().getUser().getTotalBol() > 0) {
//    //                    edit_use_bol.requestFocus();
//    //                    if(totalPrice > LoginInfoManager.getInstance().getUser().getTotalBol()) {
//    //                        edit_use_bol.setText("" + LoginInfoManager.getInstance().getUser().getTotalBol());
//    //                    } else {
//    //                        edit_use_bol.setText("" + totalPrice);
//    //                    }
//    //                } else {
//    //                    check_total_use.setChecked(false);
//    //                }
//    //
//    //            } else {
//    //                edit_use_bol.setText("");
//    //            }
//    //        }
//    //    };
//
//    //    private void checkUseBol(){
//    //
//    //        if(LoginInfoManager.getInstance().getUser().getTotalBol() > 0) {
//    //            check_total_use.setOnCheckedChangeListener(null);
//    //            if(totalPrice > LoginInfoManager.getInstance().getUser().getTotalBol()) {
//    //                if(useBol == LoginInfoManager.getInstance().getUser().getTotalBol()) {
//    //                    check_total_use.setChecked(true);
//    //                } else {
//    //                    check_total_use.setChecked(false);
//    //                }
//    //            } else {
//    //                if(useBol == totalPrice) {
//    //                    check_total_use.setChecked(true);
//    //                } else {
//    //                    check_total_use.setChecked(false);
//    //                }
//    //            }
//    //            check_total_use.setOnCheckedChangeListener(mCheckedChangeListener);
//    //        }
//    //
//    //    }
//
//    private void setCash(){
//
//        PplusCommonUtil.Companion.reloadSession(new PplusCommonUtil.Companion.ReloadListener(){
//
//            @Override
//            public void reload(){
//
//                text_retention_bol.setText(getString(R.string.format_bol_unit, FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().getUser().getTotalBol())));
//            }
//        });
//    }
//
//    private void setBytes(int bytes){
//
//        msg_count.setText(getString(R.string.format_sms_byte, String.valueOf(bytes), "300"));
//    }
//
//    private void selectedView(View view1, View view2, View view3){
//
//        view1.setSelected(true);
//        view2.setSelected(false);
//        view3.setSelected(false);
//    }
//
//    @Override
//    public void onClick(View v){
//
//        switch (v.getId()) {
//            case R.id.text_mobile_gift_purchase_card:
//                mPgType = EnumData.PGType.wcard;
//                selectedView(text_card, text_depositless, text_pg_mobile);
//                break;
//            case R.id.text_mobile_gift_purchase_depositless:
//                mPgType = EnumData.PGType.vbank;
//                selectedView(text_depositless, text_card, text_pg_mobile);
//                break;
//            case R.id.text_mobile_gift_purchase_mobile:
//                mPgType = EnumData.PGType.mobile;
//                selectedView(text_pg_mobile, text_depositless, text_card);
//                break;
//            case R.id.text_mobile_gift_purchase_add:
//                MsgTarget target = new MsgTarget();
//                mAdapter.add(target);
//                setData();
//                break;
//            case R.id.text_mobile_gift_purchase_to_me:
//
//                if(mAdapter.getItemCount() == 1){
//                    target = mAdapter.getDataList().get(0);
//                    if(StringUtils.isEmpty(target.getName()) || StringUtils.isEmpty(target.getMobile())) {
//                        mAdapter.clear();
//                    }
//                }
//
//                target = new MsgTarget();
//                target.setMobile(LoginInfoManager.getInstance().getUser().getMobile());
//                target.setName(getString(R.string.word_me));
//                mAdapter.add(target);
//                setData();
//                break;
//            case R.id.text_mobile_gift_target_all_delete:
//                mAdapter.clear();
//                target = new MsgTarget();
//                mAdapter.add(target);
//                setData();
//                break;
//            case R.id.text_mobile_gift_purchase_my_address_book:
//                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
//                startActivityForResult(intent, Const.REQ_SEARCH);
//                break;
//            case R.id.text_mobile_gift_purchase:
//
//                List<MsgTarget> targetList = mAdapter.getDataList();
//                for(MsgTarget data : targetList) {
//                    if(StringUtils.isEmpty(data.getName()) || StringUtils.isEmpty(data.getMobile())) {
//                        showAlert(R.string.msg_input_mobile_gift_target);
//                        return;
//                    }
//                }
//
//                AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                builder.setTitle(getString(R.string.word_notice_alert));
////                builder.addContents(new AlertData.MessageData(getString(R.string.format_point_unit, FormatUtil.getMoneyType(String.valueOf(totalPrice))), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//                builder.addContents(new AlertData.MessageData(getString(R.string.msg_question_mobile_gift), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//                builder.addContents(new AlertData.MessageData(getString(R.string.html_msg_caution_mobile_gift), AlertBuilder.MESSAGE_TYPE.HTML, 2));
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
//                                if(totalPrice > LoginInfoManager.getInstance().getUser().getTotalBol()) {
//                                    showAlert(R.string.msg_not_enough_bol);
//                                    return;
//                                }
//
//                                prepareOrder();
//
//                                break;
//                        }
//                    }
//                }).builder().show(this);
//
//                break;
//        }
//    }
//
//    //    private int mTotalPurchasePrice = 0;
//
//    public void setData(){
//
//        int count = mPurchase.getCountPerTarget();
//        int senderCount = mAdapter.getItemCount();
//        long price = PplusCommonUtil.Companion.calculateBol(mPurchase.getMobileGift().getSalesPrice());
//        totalPrice = count * senderCount * price;
//        text_product_count.setText(getString(R.string.format_count_unit, String.valueOf(count * mAdapter.getItemCount())));
//
//        //        mTotalPurchasePrice = totalPrice - useBol;
//        text_total_price.setText(Html.fromHtml(getString(R.string.html_total_purchase_price, FormatUtil.getMoneyType("" + (totalPrice)))));
//        //        checkUseBol();
//        //        if(mTotalPurchasePrice == 0) {
//        //            layout_pg.setVisibility(View.GONE);
//        //        } else {
//        //            layout_pg.setVisibility(View.VISIBLE);
//        //        }
//        checkHeight();
//    }
//
//    private void checkHeight(){
//
//        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){
//
//            @Override
//            public void onGlobalLayout(){
//
//                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
//
//                if(mAdapter.getItemCount() == 5) {
//                    mHeight = mRecyclerView.getHeight();
//                }
//
//                if(mAdapter.getItemCount() < 5) {
//                    mRecyclerView.setLayoutParams(layoutParams);
//                } else {
//                    mRecyclerView.getLayoutParams().height = mHeight;
//                }
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//            case Const.REQ_SEARCH:
//                if(resultCode == RESULT_OK) {
//                    ArrayList<Customer> customerList = data.getParcelableArrayListExtra(Const.CUSTOMER);
//                    if(mAdapter.getItemCount() == 1) {
//                        MsgTarget target = mAdapter.getItem(0);
//                        if(StringUtils.isEmpty(target.getName()) && StringUtils.isEmpty(target.getMobile())) {
//                            mAdapter.clear();
//                        }
//                    }
//
//                    Uri contactData = data.getData();
//                    if(contactData != null) {
//                        Cursor cursor = getContentResolver().query(contactData, null, null, null, null);
//
//                        if(cursor != null && cursor.moveToFirst()) {
//                            String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));
//
//                            Cursor phoneCur = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
//
//                            if(phoneCur != null && phoneCur.moveToFirst()) {
//                                String name = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
//                                String number = phoneCur.getString(phoneCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
//                                MsgTarget target = new MsgTarget();
//                                target.setName(name);
//                                target.setMobile(number.replace("-", ""));
//                                mAdapter.add(target);
//                                phoneCur.close();
//                            }
//
//                            cursor.close();
//                        }
//                    }
//
//                    setData();
//                }
//
//                break;
//            case Const.REQ_CASH_CHANGE:
//                setCash();
//                break;
//            case Const.REQ_PG_PURCHASE:
//                if(resultCode == RESULT_OK) {
//                    if(data != null) {
//                        String no = data.getStringExtra(Const.NO);
//
//                        String tId = data.getStringExtra(Const.TID);
//                        PgApproval approval = new PgApproval();
//                        approval.setAuthTransactionId(tId);
//                        mPurchase.setApproval(approval);
//
//                        if(StringUtils.isNotEmpty(no)) {
//                            completeOrder(no, tId);
//                        } else {
//                            if(StringUtils.isNotEmpty(mPreOrderNo)) {
//                                cancelOrder(mPreOrderNo);
//                            }
//                        }
//
//                    } else {
//                        if(StringUtils.isNotEmpty(mPreOrderNo)) {
//                            cancelOrder(mPreOrderNo);
//                        }
//                    }
//                } else {
//                    if(StringUtils.isNotEmpty(mPreOrderNo)) {
//                        cancelOrder(mPreOrderNo);
//                    }
//                }
//
//                break;
//        }
//    }
//
//    private String mPreOrderNo;
//
//    private void prepareOrder(){
//
//        List<MsgTarget> targetList = mAdapter.getDataList();
//
//        String msg = edit_msg.getText().toString().trim();
//        if(StringUtils.isNotEmpty(msg)) {
//            mPurchase.setMsg(msg);
//        }
//
//        mPurchase.setTargetList(targetList);
//        mPurchase.setTotalCost(totalPrice * CountryConfigManager.getInstance().getConfig().getProperties().getBolRatio());
//        mPurchase.setPgCost(0l);
//
//        showProgress("");
//        ApiBuilder.create().prepareOrder(mPurchase).setCallback(new PplusCallback<NewResultResponse<No>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<No>> call, NewResultResponse<No> response){
//
//                hideProgress();
//                //                if(mTotalPurchasePrice > 0) {
//                //                    mPreOrderNo = String.valueOf(response.getData().getNo());
//                //                    Intent intent = new Intent(MobileGiftPurchaseActivity.this, PGActivity.class);
//                //                    intent.putExtra(Const.GOODS, mPurchase.getMobileGift().getName());
//                //                    intent.putExtra(Const.AMOUNT, String.valueOf(mTotalPurchasePrice));
//                //                    intent.putExtra(Const.PAYMETHOD, mPayMethod.name());
//                //                    intent.putExtra(Const.NO, String.valueOf(response.getData().getNo()));
//                //                    intent.putExtra(Const.PG_TYPE, "ORDER_MOBILEGIFT");
//                //                    startActivityForResult(intent, Const.REQ_PG_PURCHASE);
//                //                } else {
//                //                    completeOrder(String.valueOf(response.getData().getNo()), null);
//                //                }
//
//                completeOrder(String.valueOf(response.getData().getNo()), null);
//
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<No>> call, Throwable t, NewResultResponse<No> response){
//
//                hideProgress();
//                if(response.getResultCode() == 623) {
//                    showAlert(R.string.msg_not_enough_bol);
//                }
//            }
//        }).build().call();
//    }
//
//    private void completeOrder(final String no, String tId){
//
//        Map<String, String> params = new HashMap<>();
//        params.put("no", no);
//        if(StringUtils.isNotEmpty(tId)) {
//            params.put("approval.authTransactionId", tId);
//        }
//
//        showProgress("");
//        ApiBuilder.create().completeOrder(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//
//                hideProgress();
//                showAlert(R.string.msg_complete_mobile_gift);
//                finish();
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
//
//                hideProgress();
//                cancelOrder(no);
//            }
//        }).build().call();
//    }
//
//    private void cancelOrder(String no){
//
//        Map<String, String> params = new HashMap<>();
//        params.put("no", no);
//        showProgress("");
//        ApiBuilder.create().cancelPrepareOrder(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//
//                hideProgress();
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
//
//                hideProgress();
//            }
//        }).build().call();
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_change_mobile_gift), ToolbarOption.ToolbarMenu.LEFT);
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
//                }
//            }
//        };
//    }
//}
