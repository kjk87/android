//package com.pplus.prnumberbiz.apps.bol.data;
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
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseArrayAdapter;
//import com.pplus.prnumberbiz.core.code.common.EnumData;
//import com.pplus.prnumberbiz.core.network.model.dto.Bol;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//
///**
// * Created by j2n on 2016. 12. 22..
// */
//
//public class BolConfigAdapter extends BaseArrayAdapter<Bol>{
//
//    public BolConfigAdapter(Context context){
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
//        Bol item = getItem(position);
//        holder.text_dat3.setText(FormatUtil.getMoneyType(""+item.getAmount()));
//        holder.text_dat2.setText(""+item.getSubject());
//
//        switch (EnumData.PrimaryType.valueOf(item.getPrimaryType())){
//
//            case increase:
//                switch (EnumData.BolType.valueOf(item.getSecondaryType())){
//
//                    case buy:
//                        break;
//                    case winEvent:
//                        break;
//                    case invite:
//                        break;
//                    case invitee:
//                        break;
//                    case recvPush:
//                        break;
//                    case review:
//                        break;
//                    case comment:
//                        break;
//                }
//                break;
//            case decrease:
//
//                switch (EnumData.BolType.valueOf(item.getSecondaryType())){
//
//                    case giftBol:
//                        break;
//                    case giftBols:
//                        break;
//                    case exchange:
//                        break;
//                    case buyMobileGift:
//                        break;
//                    case sendPush:
//                        break;
//                    case rewardReview:
//                        break;
//                    case rewardComment:
//                        break;
//                }
//
//                break;
//        }
//
//
//
//        try {
//            Date d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.getRegDate());
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
