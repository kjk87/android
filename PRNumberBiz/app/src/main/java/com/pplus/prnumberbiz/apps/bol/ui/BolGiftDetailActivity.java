package com.pplus.prnumberbiz.apps.bol.ui;

import androidx.annotation.NonNull;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.pple.pplus.utils.part.format.FormatUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pple.pplus.utils.part.utils.time.DateFormatUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.BolGift;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import network.common.PplusCallback;
import retrofit2.Call;

public class BolGiftDetailActivity extends BaseActivity implements ImplToolbar{

    @Override
    public String getPID(){

        return null;
    }

    @Override
    public int getLayoutRes(){

        return R.layout.activity_bol_gift_detail;
    }

    private BolGift mBolGift;

    @Override
    public void initializeView(Bundle savedInstanceState){

        mBolGift = getIntent().getParcelableExtra(Const.DATA);

        TextView text_amount = (TextView)findViewById(R.id.text_bol_gift_detail_amount);
        TextView text_sender = (TextView)findViewById(R.id.text_bol_gift_detail_sender);

        View text_message_title = findViewById(R.id.text_bol_gift_detail_message_title);
        TextView text_message = (TextView)findViewById(R.id.text_bol_gift_detail_message);
        TextView text_date = (TextView)findViewById(R.id.text_bol_gift_detail_date);
        TextView text_receive_date = (TextView)findViewById(R.id.text_bol_gift_detail_receive_date);
        SimpleDateFormat output = new SimpleDateFormat("yyyy.MM.dd");
        try {
            Date d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBolGift.getHistory().getRegDate());
            text_date.setText(output.format(d));
        } catch (Exception e) {

        }

        String name = null;
        if(mBolGift.getHistory().getUser().getPage() != null){
            name = mBolGift.getHistory().getUser().getPage().getName();
        }else{
            name = mBolGift.getHistory().getUser().getNickname();
        }
        text_amount.setText(FormatUtil.getMoneyType(mBolGift.getAmount())+"P");
        text_sender.setText(name);
        if(mBolGift.getHistory().getProperties() == null || StringUtils.isEmpty(mBolGift.getHistory().getProperties().get("message").getAsString())){
            text_message_title.setVisibility(View.GONE);
            text_message.setVisibility(View.GONE);
        }else{
            text_message_title.setVisibility(View.VISIBLE);
            text_message.setVisibility(View.VISIBLE);
            text_message.setText(mBolGift.getHistory().getProperties().get("message").getAsString());
        }

        if(mBolGift.isReceived()){
            findViewById(R.id.text_bol_gift_receive).setVisibility(View.GONE);
            findViewById(R.id.text_bol_gift_description).setVisibility(View.GONE);
            findViewById(R.id.text_bol_gift_detail_receive_date_title).setVisibility(View.VISIBLE);
            text_receive_date.setVisibility(View.VISIBLE);
            try {
                Date d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mBolGift.getReceiveDate());
                text_receive_date.setText(output.format(d));
            }catch (Exception e){

            }

        }else{
            findViewById(R.id.text_bol_gift_receive).setVisibility(View.VISIBLE);
            findViewById(R.id.text_bol_gift_description).setVisibility(View.VISIBLE);
            findViewById(R.id.text_bol_gift_detail_receive_date_title).setVisibility(View.GONE);
            text_receive_date.setVisibility(View.GONE);
        }

        findViewById(R.id.text_bol_gift_receive).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                showProgress("");
                Map<String, String> params = new HashMap<String, String>();
                params.put("no", ""+mBolGift.getHistory().getNo());
                showProgress("");
                ApiBuilder.create().receiveGift(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){

                    @Override
                    public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
                        hideProgress();
                        showAlert(R.string.msg_rewarded_bol);
                        setResult(RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
                        hideProgress();
                    }
                }).build().call();
            }
        });
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption(){

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.word_detail_history), ToolbarOption.ToolbarMenu.LEFT);
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
