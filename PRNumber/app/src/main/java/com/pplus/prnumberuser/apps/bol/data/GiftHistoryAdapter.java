//package com.pplus.prnumberuser.apps.bol.data;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.pplus.utils.part.format.FormatUtil;
//import com.pplus.utils.part.logs.LogUtil;
//import com.pplus.utils.part.utils.time.DateFormatUtils;
//import com.pplus.prnumberuser.R;
//import com.pplus.prnumberuser.apps.common.ui.base.BaseArrayAdapter;
//import com.pplus.prnumberuser.core.network.model.dto.Bol;
//import com.pplus.prnumberuser.core.network.model.dto.User;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//
///**
// * Created by j2n on 2016. 12. 22..
// */
//
//public class GiftHistoryAdapter extends BaseArrayAdapter<Bol>{
//
//    public GiftHistoryAdapter(Context context){
//
//        super(context, R.layout.item_history);
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
//        final Bol item = getItem(position);
//        LogUtil.e("BOl", "{} : {}, {}", item.getAmount(), item.getNo(), item.getTargetList().size());
//        List<User> targetList = item.getTargetList();
//        if(targetList.size() == 1) {
//            holder.text_receiver.setText("" + targetList.get(0).getNickname());
//        } else {
//            holder.text_receiver.setText(getContext().getString(R.string.format_other, "" + targetList.get(0).getNickname(), targetList.size() - 1));
//        }
//
//        holder.text_giftPoint.setText(FormatUtil.getMoneyType(item.getAmount()));
//
//        try {
//            Date d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.getRegDate());
//
//            SimpleDateFormat output = new SimpleDateFormat("yyyy.MM.dd");
//            holder.text_giftDate.setText(output.format(d));
//
//        } catch (Exception e) {
//            holder.text_giftDate.setText("");
//        }
//        return convertView;
//    }
//
//    class ViewHolder{
//
//        public TextView text_receiver, text_giftPoint, text_giftDate;
//        public Context context;
//
//        public void initialize(View itemView){
//
//            context = itemView.getContext();
//
//            text_receiver = (TextView) itemView.findViewById(R.id.text_history_dat1);
//            text_giftPoint = (TextView) itemView.findViewById(R.id.text_history_dat2);
//            text_giftDate = (TextView) itemView.findViewById(R.id.text_history_dat3);
//        }
//    }
//}
