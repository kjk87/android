//package com.pplus.prnumberbiz.apps.cash.ui;
//
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.format.FormatUtil;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.core.network.model.dto.Payment;
//import com.pplus.prnumberbiz.core.util.ResourcesAdditions;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//import static com.pple.pplus.utils.part.utils.time.DateFormatUtils.PPLUS_DATE_FORMAT;
//
//public class PayPendingDetailActivity extends BaseActivity implements ImplToolbar{
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
//        return R.layout.activity_pay_pending_detail;
//    }
//
//    Payment mPayment;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        mPayment = getIntent().getParcelableExtra(Const.DATA);
//
//        TextView text_date = (TextView) findViewById(R.id.text_pay_pending_detail_date);
//        TextView text_period = (TextView) findViewById(R.id.text_pay_pending_detail_period);
//
//        try {
//
//            SimpleDateFormat sdf = new SimpleDateFormat(PPLUS_DATE_FORMAT.getPattern(), Locale.getDefault());
//            Date d = sdf.parse(mPayment.getExpireDate());
//            SimpleDateFormat output = new SimpleDateFormat(getString(R.string.format_period_date, "yyyy", "MM", "dd", "HH", "mm"));
//            text_period.setText(output.format(d));
//
//            sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());
//            d = sdf.parse(mPayment.getProperties().getP_AUTH_DT());
//            output = new SimpleDateFormat("yyyy.MM.dd");
//            text_date.setText(output.format(d));
//
//            TextView text_bank = (TextView) findViewById(R.id.text_pay_pending_detail_bank);
//            text_bank.setText(""+mPayment.getProperties().getP_VACT_BANK_NAME());
//
//            TextView text_account = (TextView) findViewById(R.id.text_pay_pending_detail_account);
//            text_account.setText(""+mPayment.getProperties().getP_VACT_NUM());
//
//            TextView text_price = (TextView) findViewById(R.id.text_pay_pending_detail_price);
//            text_price.setText(FormatUtil.getMoneyType(mPayment.getProperties().getP_AMT()) + getString(R.string.word_money_unit));
//
//        } catch (Exception e) {
//
//        }
//
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_cash_pending_history), ToolbarOption.ToolbarMenu.LEFT);
//
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
//
//}
