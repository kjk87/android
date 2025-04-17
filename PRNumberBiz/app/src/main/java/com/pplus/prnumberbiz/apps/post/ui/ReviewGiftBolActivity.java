//package com.pplus.prnumberbiz.apps.post.ui;
//
//import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.format.FormatUtil;
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.billing.ui.BolBillingActivity;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.core.code.common.EnumData;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.Bol;
//import com.pplus.prnumberbiz.core.network.model.dto.No;
//import com.pplus.prnumberbiz.core.network.model.dto.User;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class ReviewGiftBolActivity extends BaseActivity implements ImplToolbar{
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
//        return R.layout.activity_review_gift_bol;
//    }
//
//    private EditText edit_bol;
//    private View text_gift;
//    private EnumData.PostType mType;
//    private User mAuthor;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        mType = EnumData.PostType.valueOf(getIntent().getStringExtra(Const.TYPE));
//        mAuthor = getIntent().getParcelableExtra(Const.DATA);
//
//        edit_bol = (EditText)findViewById(R.id.edit_gift_review_bol);
//        text_gift = findViewById(R.id.text_gift_review);
//
//        TextView text_description = (TextView)findViewById(R.id.text_gift_review_description);
//
//        switch (mType){
//            case review:
//                text_description.setText(R.string.msg_gift_lucky_bol_description1);
//                break;
//            case sns:
//                text_description.setText(R.string.msg_gift_reply_lucky_bol_description1);
//                break;
//        }
//
//        text_gift.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//
//                final String amount = edit_bol.getText().toString().trim();
//                if(StringUtils.isEmpty(amount) || (Integer.valueOf(amount) == 0)) {
//                    showAlert(R.string.msg_input_gift_bol);
//                    return;
//                }
//                AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                builder.setTitle(getString(R.string.word_notice_alert));
//                builder.addContents(new AlertData.MessageData(getString(R.string.format_bol_down, FormatUtil.getMoneyType(String.valueOf(amount))), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
//                builder.addContents(new AlertData.MessageData(getString(R.string.format_msg_gift_bol_alert, mAuthor.getNickname()), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
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
//                        switch (event_alert) {
//                            case RIGHT:
//                                if(Integer.valueOf(amount) > LoginInfoManager.getInstance().getUser().getTotalBol()) {
//                                    AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                                    builder.setTitle(getString(R.string.word_notice_alert));
//                                    builder.addContents(new AlertData.MessageData(getString(R.string.msg_lack_retention_bol), AlertBuilder.MESSAGE_TYPE.TEXT, 3));
//                                    builder.addContents(new AlertData.MessageData(getString(R.string.msg_question_charge_bol), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
//                                    builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
//                                    builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//                                        @Override
//                                        public void onCancel(){
//
//                                        }
//
//                                        @Override
//                                        public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                                            switch (event_alert) {
//                                                case RIGHT:
//                                                    Intent intent = new Intent(ReviewGiftBolActivity.this, BolBillingActivity.class);
//                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                                    startActivityForResult(intent, Const.REQ_CASH_CHANGE);
//                                                    break;
//                                            }
//                                        }
//                                    }).builder().show(ReviewGiftBolActivity.this);
//                                } else {
//                                    showProgress("");
//                                    Bol bol = new Bol();
//                                    bol.setAmount(amount);
//                                    bol.setTarget(new No(mAuthor.getNo()));
//                                    switch (mType){
//                                        case review:
//                                            ApiBuilder.create().reviewReward(bol).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//                                                @Override
//                                                public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//                                                    hideProgress();
//                                                    showAlert(R.string.msg_gifted_bol);
//                                                    PplusCommonUtil.Companion.reloadSession(null);
//                                                    setResult(RESULT_OK);
//                                                    finish();
//                                                }
//
//                                                @Override
//                                                public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
//
//                                                }
//                                            }).build().call();
//                                            break;
//                                        case sns:
//                                            ApiBuilder.create().commentReward(bol).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//                                                @Override
//                                                public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//                                                    hideProgress();
//                                                    showAlert(R.string.msg_gifted_bol);
//                                                    PplusCommonUtil.Companion.reloadSession(null);
//                                                    setResult(RESULT_OK);
//                                                    finish();
//                                                }
//
//                                                @Override
//                                                public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
//
//                                                }
//                                            }).build().call();
//                                            break;
//                                    }
//
//                                }
//                                break;
//                        }
//                    }
//                }).builder().show(ReviewGiftBolActivity.this);
//
//
//            }
//        });
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_gift_lucky_bol), ToolbarOption.ToolbarMenu.LEFT);
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
