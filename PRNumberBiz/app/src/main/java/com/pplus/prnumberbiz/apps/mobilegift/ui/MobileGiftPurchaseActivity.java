package com.pplus.prnumberbiz.apps.mobilegift.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pple.pplus.utils.part.format.FormatUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.common.ui.common.PGActivity;
import com.pplus.prnumberbiz.apps.customer.ui.SelectCustomerActivity;
import com.pplus.prnumberbiz.apps.mobilegift.data.MobileGiftTargetAdapter;
import com.pplus.prnumberbiz.core.code.common.EnumData;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Customer;
import com.pplus.prnumberbiz.core.network.model.dto.MobileGiftPurchase;
import com.pplus.prnumberbiz.core.network.model.dto.MsgTarget;
import com.pplus.prnumberbiz.core.network.model.dto.No;
import com.pplus.prnumberbiz.core.network.model.dto.PgApproval;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class MobileGiftPurchaseActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_mobile_gift_purchase;
    }

    private MobileGiftPurchase mPurchase;
    private MobileGiftTargetAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private int mHeight = 0;
    private EditText edit_msg;
    private TextView msg_count;
    private String mMaxText;
    private TextView text_retention_cash, text_product_count, text_total_price;
    private int totalPrice;
    private EnumData.PGType mPgType;
    private View text_card, text_depositless;

    @Override
    public void initializeView(Bundle savedInstanceState){

        mPurchase = getIntent().getParcelableExtra(Const.DATA);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_mobile_gift_target);
        mAdapter = new MobileGiftTargetAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.text_mobile_gift_purchase_add).setOnClickListener(this);
        findViewById(R.id.text_mobile_gift_target_all_delete).setOnClickListener(this);

        ImageView image = (ImageView) findViewById(R.id.image_mobile_gift_purchase);
        TextView text_name = (TextView) findViewById(R.id.text_mobile_gift_purchase_name);
//        text_name.setSingleLine();
        TextView text_price = (TextView) findViewById(R.id.text_mobile_gift_purchase_price);
        TextView text_use_term = (TextView) findViewById(R.id.text_mobile_gift_purchase_use_term);

        Glide.with(this).load(mPurchase.getMobileGift().getViewImage1()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image);
        text_name.setText(mPurchase.getMobileGift().getName());
        text_price.setText(getString(R.string.format_product_price, FormatUtil.getMoneyType("" + mPurchase.getMobileGift().getSalesPrice())));
        text_use_term.setText(getString(R.string.format_use_term, mPurchase.getMobileGift().getUseTerm()));

        edit_msg = (EditText) findViewById(R.id.edit_mobile_gift_purchase_msg);
        msg_count = (TextView) findViewById(R.id.text_mobile_gift_purchase_msg_count);
        setBytes(0);
        edit_msg.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){

            }

            @Override
            public void afterTextChanged(Editable s){

                String contents = s.toString();

                int bytes = contents.getBytes().length;

                if(bytes > 300) {
                    edit_msg.setText(mMaxText);
                } else {
                    mMaxText = contents;
                }

                setBytes(bytes);
            }
        });

        text_card = findViewById(R.id.text_mobile_gift_purchase_card);
        text_depositless = findViewById(R.id.text_mobile_gift_purchase_depositless);
        text_card.setOnClickListener(this);
        text_depositless.setOnClickListener(this);
        mPgType = EnumData.PGType.wcard;
        selectedView(text_card, text_depositless);

        findViewById(R.id.text_mobile_gift_purchase_charge_cash).setOnClickListener(this);
        text_retention_cash = (TextView) findViewById(R.id.text_mobile_gift_purchase_retention_cash);

        TextView text_product_price = (TextView) findViewById(R.id.text_mobile_gift_purchase_product_price);
        text_product_price.setText(getString(R.string.format_money_unit, FormatUtil.getMoneyType("" + mPurchase.getMobileGift().getSalesPrice())));
        text_product_count = (TextView) findViewById(R.id.text_mobile_gift_purchase_product_count);
        text_total_price = (TextView) findViewById(R.id.text_mobile_gift_purchase_total_price);
        findViewById(R.id.text_mobile_gift_purchase_my_customer).setOnClickListener(this);
        findViewById(R.id.text_mobile_gift_purchase).setOnClickListener(this);
        setCash();

        MsgTarget target = new MsgTarget();
        mAdapter.add(target);
        setData();
    }

    private void selectedView(View view1, View view2){

        view1.setSelected(true);
        view2.setSelected(false);
    }

    private void setCash(){

        PplusCommonUtil.Companion.reloadSession(new PplusCommonUtil.Companion.ReloadListener(){

            @Override
            public void reload(){

                text_retention_cash.setText(getString(R.string.format_money_unit, FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().getUser().getTotalCash())));
            }
        });
    }

    private void setBytes(int bytes){

        msg_count.setText(getString(R.string.format_sms_byte, bytes, 300));

    }

    @Override
    public void onClick(View v){

        switch (v.getId()) {
            case R.id.text_mobile_gift_purchase_card:
                mPgType = EnumData.PGType.wcard;
                selectedView(text_card, text_depositless);
                break;
            case R.id.text_mobile_gift_purchase_depositless:
                mPgType = EnumData.PGType.vbank;
                selectedView(text_depositless, text_card);
                break;
            case R.id.text_mobile_gift_purchase_add:
                MsgTarget target = new MsgTarget();
                mAdapter.add(target);
                setData();
                break;
            case R.id.text_mobile_gift_target_all_delete:
                mAdapter.clear();
                target = new MsgTarget();
                mAdapter.add(target);
                setData();
                break;
            case R.id.text_mobile_gift_purchase_charge_cash:
//                Intent intent = new Intent(this, CashBillingActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivityForResult(intent, Const.REQ_CASH_CHANGE);
                break;
            case R.id.text_mobile_gift_purchase_my_customer:
                Intent intent = new Intent(this, SelectCustomerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, Const.REQ_SEARCH);
                break;
            case R.id.text_mobile_gift_purchase:

                List<MsgTarget> targetList = mAdapter.getDataList();
                for(MsgTarget data : targetList){
                    if(StringUtils.isEmpty(data.getName()) || StringUtils.isEmpty(data.getMobile())){
                        showAlert(R.string.msg_input_mobile_gift_target);
                        return;
                    }
                }

//                if(totalPrice > LoginInfoManager.getInstance().getUser().getTotalCash()){
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
//                                    Intent intent = new Intent(MobileGiftPurchaseActivity.this, CashChargeActivity.class);
//                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                    startActivityForResult(intent, Const.REQ_CASH_CHANGE);
//                                    break;
//                            }
//                        }
//                    }).builder().show(this);
//                    return;
//                }

                String msg = edit_msg.getText().toString().trim();
                if(StringUtils.isNotEmpty(msg)){
                    mPurchase.setMsg(msg);
                }

                mPurchase.setTargetList(targetList);
                mPurchase.setTotalCost(totalPrice);
                mPurchase.setPgCost(0);

                AlertBuilder.Builder builder = new AlertBuilder.Builder();
                builder.setTitle(getString(R.string.word_notice_alert));
                builder.addContents(new AlertData.MessageData(getString(R.string.format_money_unit, FormatUtil.getMoneyType(String.valueOf(totalPrice))), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
                builder.addContents(new AlertData.MessageData(getString(R.string.msg_question_mobile_gift), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
                builder.setOnAlertResultListener(new OnAlertResultListener(){

                    @Override
                    public void onCancel(){

                    }

                    @Override
                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){
                        switch (event_alert){
                            case RIGHT:
                                showProgress("");
                                if(totalPrice < 1000) {
                                    showAlert(R.string.msg_enable_pg_over_1000);
                                    return;
                                }
                                prepareOrder();
                                break;
                        }
                    }
                }).builder().show(this);

                break;
        }
    }

    private void prepareOrder(){

        List<MsgTarget> targetList = mAdapter.getDataList();

        String msg = edit_msg.getText().toString().trim();
        if(StringUtils.isNotEmpty(msg)) {
            mPurchase.setMsg(msg);
        }

        mPurchase.setTargetList(targetList);
        mPurchase.setTotalCost(totalPrice);
        mPurchase.setPgCost(totalPrice);

        showProgress("");
        ApiBuilder.create().prepareOrder(mPurchase).setCallback(new PplusCallback<NewResultResponse<No>>(){

            @Override
            public void onResponse(Call<NewResultResponse<No>> call, NewResultResponse<No> response){

                hideProgress();
                if(totalPrice > 0) {
                    Intent intent = new Intent(MobileGiftPurchaseActivity.this, PGActivity.class);
                    intent.putExtra(Const.GOODS, mPurchase.getMobileGift().getName());
                    intent.putExtra(Const.AMOUNT, String.valueOf(totalPrice));
                    intent.putExtra(Const.PAYMETHOD, mPgType.name());
                    intent.putExtra(Const.NO, String.valueOf(response.getData().getNo()));
                    intent.putExtra(Const.PG_TYPE, "ORDER_MOBILEGIFT");
                    startActivityForResult(intent, Const.REQ_PG_PURCHASE);
                } else {
                    completeOrder(String.valueOf(response.getData().getNo()), null);
                }

            }

            @Override
            public void onFailure(Call<NewResultResponse<No>> call, Throwable t, NewResultResponse<No> response){

                hideProgress();
            }
        }).build().call();
    }

    private void completeOrder(final String no, String tId){

        Map<String, String> params = new HashMap<>();
        params.put("no", no);
        if(StringUtils.isNotEmpty(tId)) {
            params.put("approval.authTransactionId", tId);
        }

        showProgress("");
        ApiBuilder.create().completeOrder(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){

                hideProgress();
                showAlert(R.string.msg_complete_mobile_gift);
                finish();
            }

            @Override
            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){

                hideProgress();
                cancelOrder(no);
            }
        }).build().call();
    }

    private void cancelOrder(String no){

        Map<String, String> params = new HashMap<>();
        params.put("no", no);
        showProgress("");
        ApiBuilder.create().cancelPrepareOrder(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){

                hideProgress();
            }

            @Override
            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){

                hideProgress();
            }
        }).build().call();
    }

    public void setData(){

        int count = mPurchase.getCountPerTarget();
        int senderCount = mAdapter.getItemCount();
        int price = mPurchase.getMobileGift().getSalesPrice();
        totalPrice = count * senderCount * price;
        text_product_count.setText(getString(R.string.format_count_unit, String.valueOf(count * mAdapter.getItemCount())));
        text_total_price.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_total_purchase_price, FormatUtil.getMoneyType("" + totalPrice))));

        checkHeight();
    }

    private void checkHeight(){

        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mRecyclerView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){

            @Override
            public void onGlobalLayout(){

                mRecyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                if(mAdapter.getItemCount() == 5) {
                    mHeight = mRecyclerView.getHeight();
                }

                if(mAdapter.getItemCount() < 5) {
                    mRecyclerView.setLayoutParams(layoutParams);
                } else {
                    mRecyclerView.getLayoutParams().height = mHeight;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){

        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case Const.REQ_SEARCH:
                if(resultCode == RESULT_OK){
                    ArrayList<Customer> customerList = data.getParcelableArrayListExtra(Const.CUSTOMER);
                    if(mAdapter.getItemCount() == 1){
                        MsgTarget target = mAdapter.getItem(0);
                        if(StringUtils.isEmpty(target.getName()) && StringUtils.isEmpty(target.getMobile())){
                            mAdapter.clear();
                        }
                    }

                    for(int i = 0; i < customerList.size(); i++){
                        MsgTarget target = new MsgTarget();
                        target.setName(customerList.get(i).getName());
                        target.setMobile(customerList.get(i).getMobile());
                        mAdapter.add(target);
                    }
                    setData();
                }

                break;
            case Const.REQ_CASH_CHANGE:
                setCash();
                break;
            case Const.REQ_PG_PURCHASE:

                if(data != null) {
                    String no = data.getStringExtra(Const.NO);

                    if(resultCode == RESULT_OK) {
                        String tId = data.getStringExtra(Const.TID);
                        PgApproval approval = new PgApproval();
                        approval.setAuthTransactionId(tId);
                        mPurchase.setApproval(approval);

                        if(StringUtils.isNotEmpty(no)) {
                            completeOrder(no, tId);
                        }

                    } else {
                        if(StringUtils.isNotEmpty(no)) {
                            cancelOrder(no);
                        }

                    }
                }


                break;
        }
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_gift), ToolbarOption.ToolbarMenu.LEFT);
        return toolbarOption;
    }

    @Override
    public OnToolbarListener getOnToolbarClickListener(){

        return new OnToolbarListener(){

            @Override
            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag){

                switch (toolbarMenu) {
                    case LEFT:
                        if(tag.equals(1)) {
                            onBackPressed();
                        }
                        break;
                }
            }
        };
    }
}
