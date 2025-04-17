package com.pplus.prnumberbiz.apps.push.ui;

import android.content.Intent;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pple.pplus.utils.part.format.FormatUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pple.pplus.utils.part.utils.time.DateFormatUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.core.code.common.EnumData;
import com.pplus.prnumberbiz.core.code.common.MoveType2Code;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Msg;
import com.pplus.prnumberbiz.core.network.model.dto.Target;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class PushResultDetailActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_push_result_detail;
    }

    private Msg msg;
    private EnumData.MsgStatus mMsgStatus;
    private TextView text_receiver;

    @Override
    public void initializeView(Bundle savedInstanceState){

        msg = getIntent().getParcelableExtra(Const.DATA);
        mMsgStatus = (EnumData.MsgStatus) getIntent().getSerializableExtra(Const.TYPE);
        if(msg.getType().equals("push")) {
            setTitle(getString(R.string.word_push_send_result));
        }else{
            setTitle(getString(R.string.word_sms_send_result));
        }

        TextView text_total_count = (TextView) findViewById(R.id.text_push_result_detail_totalCount);
        TextView text_count = (TextView) findViewById(R.id.text_push_result_detail_count);
        TextView text_name = (TextView) findViewById(R.id.text_push_result_detail_name);
        TextView text_cash = (TextView) findViewById(R.id.text_push_result_detail_use_cash);
        TextView text_date = (TextView) findViewById(R.id.text_push_result_detail_send_date);
        text_receiver = (TextView) findViewById(R.id.text_push_result_detail_receiver);
        View text_cancel = findViewById(R.id.text_push_result_detail_cancel);
        View text_send = findViewById(R.id.text_push_result_detail_send);
        TextView text_contents_title = (TextView)findViewById(R.id.text_push_result_detail_contents_title);

        text_receiver.setOnClickListener(this);

        String date = null;

        switch (mMsgStatus) {

            case reserved:
                date = msg.getReserveDate();
                text_count.setVisibility(View.GONE);
                text_cancel.setVisibility(View.VISIBLE);
                text_send.setVisibility(View.VISIBLE);
                break;
            case finish:
                date = msg.getCompleteDate();
                text_cancel.setVisibility(View.GONE);
                text_send.setVisibility(View.GONE);
                break;
        }

        text_cancel.setOnClickListener(this);
        text_send.setOnClickListener(this);

        text_total_count.setText(getString(R.string.format_send_count, msg.getTargetCount()));
        if(msg.getType().equals("push")) {

            if(msg.getMoveType2().equals(MoveType2Code.adCoupon.name())){
                text_contents_title.setText(R.string.word_send_coupon);
            }else if(msg.getMoveType2().equals(MoveType2Code.adPost.name())){
                text_contents_title.setText(R.string.word_send_post);
            }

            text_count.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_push_result_detail, msg.getSuccessCount(), msg.getFailCount(), msg.getReadCount())));
        } else {
            text_contents_title.setText(R.string.word_contents_pr_message);
            text_count.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_sms_result_detail, msg.getSuccessCount(), msg.getFailCount())));
        }

        text_name.setText(msg.getContents());
        text_cash.setText(FormatUtil.getMoneyType("" + msg.getTotalPrice()) + getString(R.string.word_money_unit));

        if(StringUtils.isNotEmpty(date)) {
            try {
                Date d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(date);
                SimpleDateFormat outputDate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat outputTime = new SimpleDateFormat("HH:mm");
                text_date.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_reserved_date, outputDate.format(d), outputTime.format(d))));
            } catch (Exception e) {

            }
        }
        
        getTargetList();
    }

    private void getTargetList(){
        Map<String, String> params = new HashMap<>();
        params.put("no", "" + msg.getNo());
        params.put("pg", "1");
        if(msg.getType().equals("push")) {
            ApiBuilder.create().getPushTargetList(params).setCallback(new PplusCallback<NewResultResponse<Target>>(){

                @Override
                public void onResponse(Call<NewResultResponse<Target>> call, NewResultResponse<Target> response){

                    if(response.getDatas() != null && response.getDatas().size() > 0) {
                        if(msg.getTargetCount() > 1) {
                            text_receiver.setText(getString(R.string.format_other, response.getDatas().get(0).getUser().getDisplayName(), msg.getTargetCount() - 1));
                        } else {
                            text_receiver.setText(response.getDatas().get(0).getUser().getDisplayName());
                        }

                    }
                }

                @Override
                public void onFailure(Call<NewResultResponse<Target>> call, Throwable t, NewResultResponse<Target> response){

                }
            }).build().call();
        }else{
            ApiBuilder.create().getSmsTargetList(params).setCallback(new PplusCallback<NewResultResponse<Target>>(){

                @Override
                public void onResponse(Call<NewResultResponse<Target>> call, NewResultResponse<Target> response){

                    if(response.getDatas() != null && response.getDatas().size() > 0) {
                        if(msg.getTargetCount() > 1) {
                            text_receiver.setText(getString(R.string.format_other, response.getDatas().get(0).getName(), msg.getTargetCount() - 1));
                        } else {
                            text_receiver.setText(response.getDatas().get(0).getName());
                        }

                    }
                }

                @Override
                public void onFailure(Call<NewResultResponse<Target>> call, Throwable t, NewResultResponse<Target> response){

                }
            }).build().call();
        }

    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.text_push_result_detail_receiver:
                Intent intent = new Intent(this, PushReceiverActivity.class);
                intent.putExtra(Const.DATA, msg);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;
            case R.id.text_push_result_detail_cancel:
                AlertBuilder.Builder builder = new AlertBuilder.Builder();
                builder.setTitle(getString(R.string.word_notice_alert));
                builder.addContents(new AlertData.MessageData(getString(R.string.format_cancel_send_msg, msg.getTargetCount(), getString(R.string.word_push_en)), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
                builder.setOnAlertResultListener(new OnAlertResultListener(){

                    @Override
                    public void onCancel(){

                    }

                    @Override
                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                        switch (event_alert) {
                            case RIGHT:
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("no", ""+msg.getNo());
                                showProgress("");
                                ApiBuilder.create().cancelSend(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

                                    @Override
                                    public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
                                        hideProgress();
                                        showAlert(R.string.msg_cancel_msg);
                                        setResult(RESULT_OK);
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
                                        hideProgress();
                                    }
                                }).build().call();
                                break;
                        }
                    }
                }).builder().show(this);
                break;
            case R.id.text_push_result_detail_send:
                builder = new AlertBuilder.Builder();
                builder.setTitle(getString(R.string.word_notice_alert));
                builder.addContents(new AlertData.MessageData(getString(R.string.format_send_now_msg, msg.getTargetCount(), getString(R.string.word_push_en)), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
                builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
                builder.setOnAlertResultListener(new OnAlertResultListener(){

                    @Override
                    public void onCancel(){

                    }

                    @Override
                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                        switch (event_alert) {
                            case RIGHT:
                                Map<String, String> params = new HashMap<String, String>();
                                params.put("no", ""+msg.getNo());
                                showProgress("");
                                ApiBuilder.create().sendNow(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

                                    @Override
                                    public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
                                        hideProgress();
                                        if(msg.getType().equals("push")) {
                                            showAlert(R.string.msg_success_send_push);
                                        }else{
                                            showAlert(R.string.msg_success_send_sms);
                                        }
                                        finish();
                                    }

                                    @Override
                                    public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
                                        hideProgress();
                                    }
                                }).build().call();
                                break;
                        }
                    }
                }).builder().show(this);
                break;
        }
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_push_send_result), ToolbarOption.ToolbarMenu.LEFT);
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
