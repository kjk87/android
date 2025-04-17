package com.pplus.prnumberbiz.apps.mobilegift.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pple.pplus.utils.part.format.FormatUtil;
import com.pple.pplus.utils.part.utils.time.DateFormatUtils;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.core.network.model.dto.MobileGiftHistory;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class MobileGiftHistoryAdapter extends RecyclerView.Adapter<MobileGiftHistoryAdapter.ViewHolder>{

    private Context mContext;
    private List<MobileGiftHistory> mDataList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public MobileGiftHistoryAdapter(Context context){

        setHasStableIds(true);

        this.mContext = context;
        this.mDataList = new ArrayList<>();
    }

    @Override
    public long getItemId(int position){

        return mDataList.get(position).getNo();
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public MobileGiftHistory getItem(int position){

        return mDataList.get(position);
    }

    public List<MobileGiftHistory> getDataList(){

        return mDataList;
    }

    public void add(MobileGiftHistory data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<MobileGiftHistory> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, MobileGiftHistory data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<MobileGiftHistory> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView text_name, text_price, text_date, text_receiver;

        public ViewHolder(View itemView){

            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.image_mobile_gift_history);
            text_name = (TextView)itemView.findViewById(R.id.text_mobile_gift_history_name);
            text_price = (TextView)itemView.findViewById(R.id.text_mobile_gift_history_price);
            text_date = (TextView)itemView.findViewById(R.id.text_mobile_gift_history_date);
            text_receiver = (TextView)itemView.findViewById(R.id.text_mobile_gift_history_receiver);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mobile_gift_history, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final MobileGiftHistory item = mDataList.get(position);
        Glide.with(mContext).load(item.getMobileGift().getViewImage1()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image);
        holder.text_name.setText(item.getMobileGift().getName());
        holder.text_price.setText(mContext.getString(R.string.format_product_price, FormatUtil.getMoneyType(String.valueOf(item.getMobileGift().getSalesPrice()))));

        try {
            Date d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.getRegDate());
            SimpleDateFormat output = new SimpleDateFormat("yyyy.MM.dd");
            holder.text_date.setText(mContext.getString(R.string.format_buy_date, output.format(d)));
        } catch (Exception e) {

        }

        if(item.getTargetCount() > 1){
            holder.text_receiver.setText(mContext.getString(R.string.format_send_target, mContext.getString(R.string.format_other, item.getMainName(), item.getTargetCount()-1)));
        }else{
            holder.text_receiver.setText(mContext.getString(R.string.format_send_target, item.getMainName()));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                if(listener != null){
                    listener.onItemClick(holder.getAdapterPosition());
                }
            }
        });

    }

    @Override
    public int getItemCount(){

        return mDataList.size();
    }
}
