//package com.pplus.prnumberbiz.apps.cash.data;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.format.FormatUtil;
//import com.pple.pplus.utils.part.utils.time.DateFormatUtils;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.cash.ui.CashConfigActivity;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseArrayAdapter;
//import com.pplus.prnumberbiz.core.code.common.EnumData;
//import com.pplus.prnumberbiz.core.network.model.dto.Cash;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//
///**
// * Created by j2n on 2016. 12. 22..
// */
//
//public class CashConfigAdapter extends BaseArrayAdapter<Cash>{
//
//    private CashConfigActivity.CashTab cashTab;
//
//    public CashConfigAdapter(Context context, CashConfigActivity.CashTab cashTab){
//
//        super(context, R.layout.item_history);
//        this.cashTab = cashTab;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent){
//
//        ViewHolder holder;
//
//        if(convertView == null) {
//
//            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history, parent, false);
//            holder = new ViewHolder();
//            holder.initialize(convertView);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//
//        Cash cash = getItem(position);
//        holder.text_dat3.setText(FormatUtil.getMoneyType(""+cash.getAmount()));
//
//        switch (EnumData.CashType.valueOf(cash.getSecondaryType())){
//
//            case buy:
//                holder.text_dat2.setText(R.string.type_cash_buy);
//                break;
//            case recvAdmin:
//                holder.text_dat2.setText(R.string.type_cash_recvAdmin);
//                break;
//            case refundMsgFail:
//                holder.text_dat2.setText(R.string.type_cash_refundMsgFail);
//                break;
//            case cancelSendMsg:
//                holder.text_dat2.setText(R.string.type_cash_cancelSendMsg);
//                break;
//            case useTargetPush:
//                holder.text_dat2.setText(R.string.cash_use_target_push);
//                break;
//            case usePush:
//                holder.text_dat2.setText(R.string.type_cash_usePush);
//                break;
//            case useSms:
//                holder.text_dat2.setText(R.string.type_cash_useSms);
//                break;
//            case useLBS:
//                holder.text_dat2.setText(R.string.cash_use_lbs);
//                break;
//            case useAdKeyword:
//                holder.text_dat2.setText(R.string.type_cash_useAdKeyword);
//                break;
//            case buyBol:
//                holder.text_dat2.setText(R.string.cash_buy_bol);
//                break;
//            case useAdvertise:
//                holder.text_dat2.setText(R.string.type_cash_useAdvertise);
//                break;
//            case refundAdvertise:
//                holder.text_dat2.setText(R.string.type_cash_refundAdvertise);
//                break;
//        }
//
//
//
//        try {
//            Date d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(cash.getRegDate());
//
//            SimpleDateFormat output = new SimpleDateFormat("yyyy.MM.dd");
//            holder.text_dat1.setText(output.format(d));
//
//        } catch (Exception e) {
//
//        }
//
//        return convertView;
//    }
//
//    class ViewHolder{
//
//        public TextView text_dat1, text_dat2, text_dat3;
//        public Context context;
//
//        public void initialize(View itemView){
//
//            context = itemView.getContext();
//
//            text_dat1 = (TextView) itemView.findViewById(R.id.text_history_dat1);
//            text_dat2 = (TextView) itemView.findViewById(R.id.text_history_dat2);
//            text_dat3 = (TextView) itemView.findViewById(R.id.text_history_dat3);
//        }
//    }
//}
