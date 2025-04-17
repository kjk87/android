package com.pplus.prnumberbiz.apps.bol.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pple.pplus.utils.part.apps.resource.ResourceUtil;
import com.pple.pplus.utils.part.format.FormatUtil;
import com.pple.pplus.utils.part.utils.time.DateFormatUtils;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.core.network.model.dto.BolGift;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by 김종경 on 2015-06-17.
 */
public class BolGiftAdapter extends RecyclerView.Adapter<BolGiftAdapter.ViewHolder>{

    private Context context;
    private List<BolGift> mDataList;
    private OnClickListener listener;

    public interface OnClickListener{

        void onClick(int position);
    }

    public BolGiftAdapter(Context context){

        this.context = context;
        mDataList = new ArrayList<>();
    }

    public void setOnClickListener(OnClickListener OnClickListener){

        this.listener = OnClickListener;
    }

    public void setDataList(List<BolGift> dataList){

        mDataList = new ArrayList<>();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addAll(List<BolGift> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void add(BolGift data){

        mDataList.add(0, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView text_date, text_subject;

        public ViewHolder(View itemView){

            super(itemView);
            text_date = (TextView) itemView.findViewById(R.id.text_pending_date);
            text_subject = (TextView) itemView.findViewById(R.id.text_pending_amount);
        }
    }

    public BolGift getItem(int position){

        return mDataList.get(position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pending, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final BolGift item = mDataList.get(position);

        try {
            Date d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.getHistory().getRegDate());
            SimpleDateFormat output = new SimpleDateFormat("yyyy.MM.dd");
            holder.text_date.setText(output.format(d));
        } catch (Exception e) {

        }

        String name = null;
        if(item.getHistory().getUser().getPage() != null){
            name = item.getHistory().getUser().getPage().getName();
        }else{
            name = item.getHistory().getUser().getNickname();
        }

        if(item.isReceived()){
            holder.text_date.setTextColor(ResourceUtil.getColor(context, R.color.color_737373));
            holder.text_subject.setTextColor(ResourceUtil.getColor(context, R.color.color_737373));
            holder.text_subject.setText(context.getString(R.string.format_msg_gift_bol, name, FormatUtil.getMoneyType(item.getAmount())));

        }else{
            holder.text_date.setTextColor(ResourceUtil.getColor(context, R.color.black));
            holder.text_subject.setTextColor(ResourceUtil.getColor(context, R.color.black));
            holder.text_subject.setText(PplusCommonUtil.Companion.fromHtml(context.getString(R.string.html_msg_gift_bol, name, FormatUtil.getMoneyType(item.getAmount()))));

        }


        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                if(listener != null) {
                    listener.onClick(holder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount(){

        return mDataList.size();
    }
}
