package com.pplus.prnumberbiz.apps.bol.ui;

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

public class BolConfigDetailActivity extends BaseActivity implements View.OnClickListener{

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

        TextView text_earn_title = (TextView)findViewById(R.id.text_cash_config_detail_receive_title);
        text_earn_title.setText(R.string.word_earn_price);

        final TextView text_purchase = (TextView) findViewById(R.id.text_cash_config_detail_purchase);
        final TextView text_receive = (TextView) findViewById(R.id.text_cash_config_detail_receive);
        final TextView text_use = (TextView) findViewById(R.id.text_cash_config_detail_use);
        TextView text_retention = (TextView) findViewById(R.id.text_cash_config_detail_retention);

        text_retention.setText(FormatUtil.getMoneyType("" + LoginInfoManager.getInstance().getUser().getTotalBol()) + "P");

        Map<String, String> params = new HashMap<>();
        params.put("filter", EnumData.BolType.buy.name());
        ApiBuilder.create().getBolHistoryTotalAmount(params).setCallback(new PplusCallback<NewResultResponse<String>>(){

            @Override
            public void onResponse(Call<NewResultResponse<String>> call, NewResultResponse<String> response){

                text_purchase.setText(FormatUtil.getMoneyType(response.getData()) + "P");
            }

            @Override
            public void onFailure(Call<NewResultResponse<String>> call, Throwable t, NewResultResponse<String> response){

            }
        }).build().call();

        StringBuilder builder = new StringBuilder();
        builder.append(EnumData.BolType.winEvent.name()).append(",");
        builder.append(EnumData.BolType.invite.name()).append(",");
        builder.append(EnumData.BolType.invitee.name()).append(",");
        builder.append(EnumData.BolType.recvPush.name()).append(",");
        builder.append(EnumData.BolType.review.name()).append(",");
        builder.append(EnumData.BolType.comment.name()).append(",");
        builder.append(EnumData.BolType.recvGift.name()).append(",");
        builder.append(EnumData.BolType.denyExchange.name()).append(",");
        builder.append(EnumData.BolType.denyRecv.name());
        params = new HashMap<>();
        params.put("filter", builder.toString());

        ApiBuilder.create().getBolHistoryTotalAmount(params).setCallback(new PplusCallback<NewResultResponse<String>>(){

            @Override
            public void onResponse(Call<NewResultResponse<String>> call, NewResultResponse<String> response){

                text_receive.setText(FormatUtil.getMoneyType(response.getData()) + "P");
            }

            @Override
            public void onFailure(Call<NewResultResponse<String>> call, Throwable t, NewResultResponse<String> response){

            }
        }).build().call();

        builder = new StringBuilder();
        builder.append(EnumData.BolType.giftBol.name()).append(",");
        builder.append(EnumData.BolType.giftBols.name()).append(",");
        builder.append(EnumData.BolType.exchange.name()).append(",");
        builder.append(EnumData.BolType.buyMobileGift.name()).append(",");
        builder.append(EnumData.BolType.sendPush.name()).append(",");
        builder.append(EnumData.BolType.rewardReview.name()).append(",");
        builder.append(EnumData.BolType.rewardComment.name()).append(",");
        builder.append(EnumData.BolType.reqExchange.name());

        params.put("filter", builder.toString());

        ApiBuilder.create().getBolHistoryTotalAmount(params).setCallback(new PplusCallback<NewResultResponse<String>>(){

            @Override
            public void onResponse(Call<NewResultResponse<String>> call, NewResultResponse<String> response){

                text_use.setText(FormatUtil.getMoneyType(response.getData()) + "P");
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
