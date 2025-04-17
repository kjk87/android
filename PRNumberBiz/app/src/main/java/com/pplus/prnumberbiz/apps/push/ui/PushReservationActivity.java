package com.pplus.prnumberbiz.apps.push.ui;

import android.app.DatePickerDialog;
import androidx.annotation.NonNull;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.pple.pplus.utils.part.utils.StringUtils;
import com.pple.pplus.utils.part.utils.time.DateFormatUtils;
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
import com.pplus.prnumberbiz.apps.push.data.PushListAdapter;
import com.pplus.prnumberbiz.core.code.common.EnumData;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.Msg;
import com.pplus.prnumberbiz.core.network.model.dto.User;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class PushReservationActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_push_reservation;
    }

    private TextView textTotalCount, text_reservation_date, textReservationTime;
    private PushListAdapter mAdapter;
    private Msg msg;
    private String date, time;
    private EnumData.MsgType mType;

    @Override
    public void initializeView(Bundle savedInstanceState){

        mType = (EnumData.MsgType) getIntent().getSerializableExtra(Const.KEY);

        msg = getIntent().getParcelableExtra(Const.DATA);
        msg.setReserved(true);

        textTotalCount = (TextView) findViewById(R.id.text_push_reservation_totalCount);

        text_reservation_date = (TextView) findViewById(R.id.text_push_reservationt_date);
        textReservationTime = (TextView) findViewById(R.id.text_push_reservation_time);
        text_reservation_date.setOnClickListener(this);
        textReservationTime.setOnClickListener(this);

        RecyclerView recycler = (RecyclerView) findViewById(R.id.recycler_push_reservation);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new PushListAdapter(this, EnumData.MsgStatus.reserved);
        recycler.setAdapter(mAdapter);
        TextView text_send = (TextView)findViewById(R.id.text_push_reservation_send);
        text_send.setOnClickListener(this);

        switch (mType){

            case push:
                text_send.setText(R.string.word_push_send);
                break;
            case sms:
                text_send.setText(R.string.word_send_pr_sms);
                break;
        }

        getListCall();
    }

    private void getListCall(){

        Map<String, String> params = new HashMap<>();
        params.put("type", mType.name());
        ApiBuilder.create().getReservedMsgAll(params).setCallback(new PplusCallback<NewResultResponse<Msg>>(){

            @Override
            public void onResponse(Call<NewResultResponse<Msg>> call, NewResultResponse<Msg> response){

                mAdapter.setDataList(response.getDatas());
                switch (mType) {

                    case push:
                        textTotalCount.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_reservation_push_total_count, response.getDatas().size())));
                        break;
                    case sms:
                        textTotalCount.setText(PplusCommonUtil.Companion.fromHtml(getString(R.string.html_reservation_sms_total_count, response.getDatas().size())));
                        break;
                }

            }

            @Override
            public void onFailure(Call<NewResultResponse<Msg>> call, Throwable t, NewResultResponse<Msg> response){

            }
        }).build().call();
    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.text_push_reservationt_date:
                Calendar c = Calendar.getInstance();
                int todayYear = c.get(Calendar.YEAR);
                int todayMonth = c.get(Calendar.MONTH);
                int todayDay = c.get(Calendar.DAY_OF_MONTH);
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener(){

                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2){

                        text_reservation_date.setText(getString(R.string.format_date, String.valueOf(i), String.valueOf(i1 + 1), String.valueOf(i2)));
                        date = String.valueOf(i) + "-" + String.valueOf(i1 + 1) + "-" + String.valueOf(i2);
                    }
                }, todayYear, todayMonth, todayDay).show();
                break;
            case R.id.text_push_reservation_time:
//                new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener(){
//
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute){
//
//                        String strMinute = "00";
//                        if(minute == 0) {
//                            strMinute = "00";
//                        } else if(minute < 10) {
//                            strMinute = "0" + minute;
//                        } else {
//                            strMinute = "" + minute;
//                        }
//
//                        String strHour = "";
//                        if(hourOfDay < 10) {
//                            strHour = "0" + hourOfDay;
//                        } else {
//                            strHour = "" + hourOfDay;
//                        }
//                        time = strHour + ":" + strMinute + ":00";
//                        textReservationTime.setText(strHour + ":" + strMinute);
//                    }
//                }, 0, 0, false).show();

                final String[] contents = getResources().getStringArray(R.array.times);
                AlertBuilder.Builder builder = new AlertBuilder.Builder();
                builder.setTitle(getString(R.string.word_select_time));
                builder.setContents(contents);
                builder.setRightText(getString(R.string.word_select));
                builder.setBackgroundClickable(false);
//                styleAlert.setValue(rootIndex);
                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_RADIO);
                builder.setOnAlertResultListener(new OnAlertResultListener(){

                    @Override
                    public void onCancel(){

                    }

                    @Override
                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                        if(event_alert == AlertBuilder.EVENT_ALERT.RADIO) {
                            int index = event_alert.getValue() - 1;
                            time = contents[index]+":00";
                            textReservationTime.setText(contents[index]);
                        }
                    }
                });

                builder.builder().show(this);
                break;
            case R.id.text_push_reservation_send:
                if(StringUtils.isEmpty(date)) {
                    showAlert(R.string.msg_input_reservation_date);
                    return;
                }

                if(StringUtils.isEmpty(time)) {
                    showAlert(R.string.msg_input_reservation_time);
                    return;
                }

                String reservationDate = date + " " + time;

                if(compareDate(reservationDate)) {
                    showAlert(R.string.msg_input_over_current_time);
                    return;
                }
                msg.setReserveDate(reservationDate);

                switch (mType) {

                    case push:
                        sendPush(msg);
                        break;
                    case sms:
                        sendSms(msg);
                        break;
                }

                break;
        }
    }

    private void sendSms(final Msg msg){

        AlertBuilder.Builder builder = new AlertBuilder.Builder();
        builder.setTitle(getString(R.string.word_notice_alert));
        builder.addContents(new AlertData.MessageData(getString(R.string.msg_sms_send_alert1), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
        builder.addContents(new AlertData.MessageData(getString(R.string.format_sms_send_alert2, msg.getTargetList().size()), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_sms_send));
        builder.setOnAlertResultListener(new OnAlertResultListener(){

            @Override
            public void onCancel(){

            }

            @Override
            public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                switch (event_alert) {
                    case RIGHT:
                        showProgress("");
                        ApiBuilder.create().insertSmsMsg(msg).setCallback(new PplusCallback<NewResultResponse<Msg>>(){

                            @Override
                            public void onResponse(final Call<NewResultResponse<Msg>> call, NewResultResponse<Msg> response){

                                hideProgress();
                                setResult(RESULT_OK);
                                AlertBuilder.Builder builder = new AlertBuilder.Builder();
                                builder.setTitle(getString(R.string.word_notice_alert));
                                builder.addContents(new AlertData.MessageData(getString(R.string.msg_success_reserved_sms), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
                                builder.addContents(new AlertData.MessageData(getString(R.string.msg_success_send), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
                                builder.setLeftText(getString(R.string.word_confirm));
                                builder.setAutoCancel(false);
                                builder.setBackgroundClickable(false);
                                builder.setOnAlertResultListener(new OnAlertResultListener(){

                                    @Override
                                    public void onCancel(){

                                    }

                                    @Override
                                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                                        switch (event_alert) {
                                            case SINGLE:
                                                reloadSession();
                                                break;
                                        }
                                    }
                                }).builder().show(PushReservationActivity.this);


                            }

                            @Override
                            public void onFailure(Call<NewResultResponse<Msg>> call, Throwable t, NewResultResponse<Msg> response){

                                hideProgress();
                            }
                        }).build().call();
                        break;
                }
            }
        }).builder().show(this);
    }

    private void sendPush(final Msg msg){

        AlertBuilder.Builder builder = new AlertBuilder.Builder();
        builder.setTitle(getString(R.string.word_notice_alert));
        builder.addContents(new AlertData.MessageData(getString(R.string.msg_push_send_alert1), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
        builder.addContents(new AlertData.MessageData(getString(R.string.format_push_send_alert2, msg.getTargetList().size()), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_push_send));
        builder.setOnAlertResultListener(new OnAlertResultListener(){

            @Override
            public void onCancel(){

            }

            @Override
            public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                switch (event_alert) {
                    case RIGHT:
                        showProgress("");
                        ApiBuilder.create().insertPushMsg(msg).setCallback(new PplusCallback<NewResultResponse<Msg>>(){

                            @Override
                            public void onResponse(final Call<NewResultResponse<Msg>> call, NewResultResponse<Msg> response){

                                hideProgress();
                                setResult(RESULT_OK);
                                AlertBuilder.Builder builder = new AlertBuilder.Builder();
                                builder.setTitle(getString(R.string.word_notice_alert));
                                builder.addContents(new AlertData.MessageData(getString(R.string.msg_success_reserved_push), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
                                builder.addContents(new AlertData.MessageData(getString(R.string.msg_success_send), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
                                builder.setLeftText(getString(R.string.word_confirm));
                                builder.setAutoCancel(false);
                                builder.setBackgroundClickable(false);
                                builder.setOnAlertResultListener(new OnAlertResultListener(){

                                    @Override
                                    public void onCancel(){

                                    }

                                    @Override
                                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                                        switch (event_alert) {
                                            case SINGLE:
                                                reloadSession();
                                                break;
                                        }
                                    }
                                }).builder().show(PushReservationActivity.this);
                            }

                            @Override
                            public void onFailure(Call<NewResultResponse<Msg>> call, Throwable t, NewResultResponse<Msg> response){

                                hideProgress();
                            }
                        }).build().call();
                        break;
                }
            }
        }).builder().show(this);
    }

    private void reloadSession(){

        ApiBuilder.create().reloadSession().setCallback(new PplusCallback<NewResultResponse<User>>(){

            @Override
            public void onResponse(Call<NewResultResponse<User>> call, NewResultResponse<User> response){

                LoginInfoManager.getInstance().getUser().setTotalCash(response.getData().getTotalCash());
                LoginInfoManager.getInstance().save();
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onFailure(Call<NewResultResponse<User>> call, Throwable t, NewResultResponse<User> response){

            }
        }).build().call();
    }

    private boolean compareDate(String reservationDate){

        try {

            SimpleDateFormat formatter = new SimpleDateFormat(DateFormatUtils.PPLUS_DATE_FORMAT.getPattern(), Locale.getDefault());


            Date date1 = new Date(System.currentTimeMillis());

            Date date2 = formatter.parse(reservationDate);

            if(date1.compareTo(date2) < 0) {
                return false;
            }

        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return true;
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_reservation_sent), ToolbarOption.ToolbarMenu.LEFT);
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
