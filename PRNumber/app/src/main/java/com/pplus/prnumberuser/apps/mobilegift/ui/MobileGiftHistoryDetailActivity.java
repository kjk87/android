//package com.pplus.prnumberuser.apps.mobilegift.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.View;
//import android.widget.ImageView;
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
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberuser.apps.mobilegift.data.MobileGiftHistoryTargetAdapter;
//import com.pplus.prnumberuser.core.network.ApiBuilder;
//import com.pplus.prnumberuser.core.network.model.dto.MobileGiftHistory;
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberuser.core.util.PplusCommonUtil;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import com.pplus.networks.common.PplusCallback;
//import retrofit2.Call;
//
//public class MobileGiftHistoryDetailActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{
//
//    @Override
//    public String getPID(){
//
//        return null;
//    }
//
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_mobile_gift_history_detail;
//    }
//
//    private MobileGiftHistory mMobileGiftHistory;
//    private MobileGiftHistoryTargetAdapter mAdapter;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        mMobileGiftHistory = getIntent().getParcelableExtra(Const.DATA);
//
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_mobile_gift_history_detail_target);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mAdapter = new MobileGiftHistoryTargetAdapter(this);
//        recyclerView.setAdapter(mAdapter);
//
//        if(mMobileGiftHistory != null && mMobileGiftHistory.getNo() != null) {
//
//            ImageView image = (ImageView) findViewById(R.id.image_mobile_gift_history_detail);
//            TextView text_name = (TextView) findViewById(R.id.text_mobile_gift_history_detail_name);
//            TextView text_price = (TextView) findViewById(R.id.text_mobile_gift_history_detail_price);
//            TextView text_receiver = (TextView) findViewById(R.id.text_mobile_gift_history_detail_receiver);
//
//            Glide.with(this).load(mMobileGiftHistory.getMobileGift().getBaseImage()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image);
//            text_name.setText(mMobileGiftHistory.getMobileGift().getName());
//
//            text_price.setText(getString(R.string.format_product_price, FormatUtil.getMoneyType(String.valueOf(PplusCommonUtil.Companion.calculateBol(mMobileGiftHistory.getMobileGift().getSalesPrice())))));
//
//            if(mMobileGiftHistory.getTargetCount() > 1) {
//                text_receiver.setText(getString(R.string.format_send_target, getString(R.string.format_other, mMobileGiftHistory.getMainName(), mMobileGiftHistory.getTargetCount() - 1)));
//            } else {
//                text_receiver.setText(getString(R.string.format_send_target, mMobileGiftHistory.getMainName()));
//            }
//
//            TextView text_count = (TextView) findViewById(R.id.text_mobile_gift_history_detail_count);
//            TextView text_total_price = (TextView) findViewById(R.id.text_mobile_gift_history_detail_total_price);
//            TextView text_msg = (TextView) findViewById(R.id.text_mobile_gift_history_detail_send_msg);
//
//            text_count.setText(getString(R.string.format_count_unit, FormatUtil.getMoneyType("" + mMobileGiftHistory.getTargetCount() * mMobileGiftHistory.getCountPerTarget())));
//            text_total_price.setText(getString(R.string.format_bol_unit, FormatUtil.getMoneyType("" + mMobileGiftHistory.getTotalCost()/ CountryConfigManager.getInstance().getConfig().getProperties().getBolRatio())));
//            if(StringUtils.isEmpty(mMobileGiftHistory.getMsg())) {
//                text_msg.setVisibility(View.GONE);
//                findViewById(R.id.layout_mobile_gift_history_detail_send_msg_title).setVisibility(View.GONE);
//            } else {
//                text_msg.setVisibility(View.VISIBLE);
//                findViewById(R.id.layout_mobile_gift_history_detail_send_msg_title).setVisibility(View.VISIBLE);
//                text_msg.setText(mMobileGiftHistory.getMsg());
//            }
//
//            findViewById(R.id.image_mobile_gift_history_detail).setOnClickListener(this);
//            findViewById(R.id.text_mobile_gift_history_detail_cancel).setOnClickListener(this);
////            text_top_right.setVisibility(View.GONE);
//
//            getData();
//            getStatus();
//        }
//    }
//
//    @Override
//    public void onClick(View v){
//
//        switch (v.getId()) {
//            case R.id.image_mobile_gift_history_detail:
//                Intent intent = new Intent(this, MobileGiftDetailActivity.class);
//                intent.putExtra(Const.DATA, mMobileGiftHistory.getMobileGift());
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                setResult(RESULT_OK);
//                break;
//            case R.id.text_mobile_gift_history_detail_cancel:
//                AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                builder.setTitle(getString(R.string.word_guidance));
//                builder.addContents(new AlertData.MessageData(getString(R.string.msg_mobile_gift_cancel1), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//                builder.addContents(new AlertData.MessageData(getString(R.string.msg_mobile_gift_cancel2), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//                builder.addContents(new AlertData.MessageData(getString(R.string.msg_mobile_gift_cancel3), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//                builder.addContents(new AlertData.MessageData(getString(R.string.msg_mobile_gift_cancel4), AlertBuilder.MESSAGE_TYPE.TEXT, 4));
//                builder.setLeftText(getString(R.string.word_confirm));
//                builder.builder().show(this);
//                break;
//        }
//    }
//
//    private void getStatus(){
//
//        Map<String, String> params = new HashMap<>();
//        params.put("no", "" + mMobileGiftHistory.getNo());
//        ApiBuilder.create().getMobileGiftStatus(params).setCallback(new PplusCallback<NewResultResponse<MobileGiftHistory>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<MobileGiftHistory>> call, NewResultResponse<MobileGiftHistory> response){
//
//
//                if(response.getData().getStatus().equals("success")) {
////                    text_top_right.setVisibility(View.VISIBLE);
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<MobileGiftHistory>> call, Throwable t, NewResultResponse<MobileGiftHistory> response){
//
//            }
//        }).build().call();
//    }
//
//    private void getData(){
//
//        Map<String, String> params = new HashMap<>();
//        params.put("no", "" + mMobileGiftHistory.getNo());
//        showProgress("");
//        ApiBuilder.create().getMobileGiftPurchaseWithTargetAll(params).setCallback(new PplusCallback<NewResultResponse<MobileGiftHistory>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<MobileGiftHistory>> call, NewResultResponse<MobileGiftHistory> response){
//
//                hideProgress();
//                mAdapter.clear();
//                mAdapter.addAll(response.getData().getTargetList());
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<MobileGiftHistory>> call, Throwable t, NewResultResponse<MobileGiftHistory> response){
//
//                hideProgress();
//            }
//        }).build().call();
//    }
//
//    private void resend(){
//
//        Map<String, String> params = new HashMap<>();
//        params.put("no", "" + mMobileGiftHistory.getNo());
//        showProgress("");
//        ApiBuilder.create().mobileGiftResend(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//
//                hideProgress();
//                showAlert(R.string.msg_complete_mobile_gift_resend);
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
//
//                hideProgress();
//                if(response.getErrorExtra() != null && StringUtils.isNotEmpty(response.getErrorExtra().get("giftiel_resend"))){
//                    showAlert(response.getErrorExtra().get("giftiel_resend"));
//                }
//            }
//        }).build().call();
//    }
//
////    private TextView text_top_right;
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_change_history), ToolbarOption.ToolbarMenu.LEFT);
//
////        text_top_right = new TextView(new ContextThemeWrapper(this, R.style.buttonStyle));
////        text_top_right.setText(R.string.word_resend);
////        text_top_right.setClickable(true);
////        text_top_right.setGravity(Gravity.CENTER);
////        text_top_right.setPadding(0, 0, getResources().getDimensionPixelSize(R.dimen.width_66), 0);
////        text_top_right.setTextColor(ResourceUtil.getColorStateList(this, R.color.color_579ffb));
////        text_top_right.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimensionPixelSize(R.dimen.textSize_45pt));
////        text_top_right.setSingleLine();
////        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, text_top_right, 0);
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
//                            AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                            builder.setTitle(getString(R.string.word_notice_alert));
//                            builder.addContents(new AlertData.MessageData(getString(R.string.msg_question_mobile_gift_resend), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//                            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
//                            builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//                                @Override
//                                public void onCancel(){
//
//                                }
//
//                                @Override
//                                public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//                                    switch (event_alert){
//                                        case RIGHT:
//                                            resend();
//                                            break;
//                                    }
//                                }
//                            }).builder().show(MobileGiftHistoryDetailActivity.this);
//
//                        }
//                        break;
//                }
//            }
//        };
//    }
//}
