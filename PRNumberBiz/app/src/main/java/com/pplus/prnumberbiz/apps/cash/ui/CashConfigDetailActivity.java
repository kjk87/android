package com.pplus.prnumberbiz.apps.cash.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pple.pplus.utils.part.format.FormatUtil;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.core.code.common.EnumData;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import java.util.HashMap;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class CashConfigDetailActivity extends BaseActivity implements View.OnClickListener{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_cash_config_detail;
    }

    @Override
    public void initializeView(Bundle savedInstanceState){

        final TextView text_purchase = (TextView) findViewById(R.id.text_cash_config_detail_purchase);
        final TextView text_receive = (TextView) findViewById(R.id.text_cash_config_detail_receive);
        final TextView text_use = (TextView) findViewById(R.id.text_cash_config_detail_use);
        TextView text_retention = (TextView) findViewById(R.id.text_cash_config_detail_retention);

        text_retention.setText(FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().getUser().getTotalCash()) + getString(R.string.word_money_unit));

        Map<String, String> params = new HashMap<>();
        params.put("filter", EnumData.CashType.buy.name());
        ApiBuilder.create().getCashHistoryTotalAmount(params).setCallback(new PplusCallback<NewResultResponse<String>>(){

            @Override
            public void onResponse(Call<NewResultResponse<String>> call, NewResultResponse<String> response){

                text_purchase.setText(FormatUtil.getMoneyType(response.getData()) + getString(R.string.word_money_unit));
            }

            @Override
            public void onFailure(Call<NewResultResponse<String>> call, Throwable t, NewResultResponse<String> response){

            }
        }).build().call();

        params = new HashMap<>();
        StringBuilder builder = new StringBuilder();
        builder.append(EnumData.CashType.cancelSendMsg.name()).append(",");
        builder.append(EnumData.CashType.refundMsgFail.name()).append(",");
        builder.append(EnumData.CashType.recvAdmin.name());
        params.put("filter", builder.toString());

        ApiBuilder.create().getCashHistoryTotalAmount(params).setCallback(new PplusCallback<NewResultResponse<String>>(){

            @Override
            public void onResponse(Call<NewResultResponse<String>> call, NewResultResponse<String> response){

                text_receive.setText(FormatUtil.getMoneyType(response.getData()) + getString(R.string.word_money_unit));
            }

            @Override
            public void onFailure(Call<NewResultResponse<String>> call, Throwable t, NewResultResponse<String> response){

            }
        }).build().call();

        builder = new StringBuilder();
        builder.append(EnumData.CashType.useTargetPush.name()).append(",");
        builder.append(EnumData.CashType.usePush.name()).append(",");
        builder.append(EnumData.CashType.useSms.name()).append(",");
        builder.append(EnumData.CashType.useLBS.name()).append(",");
        builder.append(EnumData.CashType.useAdKeyword.name()).append(",");
        builder.append(EnumData.CashType.refundAdmin.name()).append(",");
        builder.append(EnumData.CashType.buyBol.name());
        params.put("filter", builder.toString());

        ApiBuilder.create().getCashHistoryTotalAmount(params).setCallback(new PplusCallback<NewResultResponse<String>>(){

            @Override
            public void onResponse(Call<NewResultResponse<String>> call, NewResultResponse<String> response){

                text_use.setText(FormatUtil.getMoneyType(response.getData()) + getString(R.string.word_money_unit));
            }

            @Override
            public void onFailure(Call<NewResultResponse<String>> call, Throwable t, NewResultResponse<String> response){

            }
        }).build().call();

        findViewById(R.id.image_cash_config_detail_close).setOnClickListener(this);
    }

    @Override
    public void onClick(View view){

        switch (view.getId()) {
            case R.id.image_cash_config_detail_close:
                onBackPressed();
                break;
        }
    }
}
