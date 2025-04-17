package com.pplus.prnumberbiz.apps.push.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pple.pplus.utils.part.utils.StringUtils;
import com.pple.pplus.utils.part.utils.time.DateFormatUtils;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.core.code.common.EnumData;
import com.pplus.prnumberbiz.core.network.model.dto.Msg;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class PushListAdapter extends RecyclerView.Adapter<PushListAdapter.ViewHolder>{

    private Context mContext;
    private List<Msg> mDataList;
    private OnItemClickListener listener;
    private EnumData.MsgStatus mStatus;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public interface OnDataChangeListener{

        void onChange();
    }

    public PushListAdapter(Context context, EnumData.MsgStatus status){

        this.mContext = context;
        this.mDataList = new ArrayList<>();
        this.mStatus = status;
    }

    public void setStatus(EnumData.MsgStatus status){

        this.mStatus = status;
//        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public Msg getItem(int position){

        return mDataList.get(position);
    }

    public void add(Msg data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<Msg> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, Msg data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<Msg> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView text_date, text_contents, text_push_list_result, text_status;

        public ViewHolder(View itemView){

            super(itemView);
            text_date = (TextView) itemView.findViewById(R.id.text_push_list_date);
            text_contents = (TextView) itemView.findViewById(R.id.text_push_list_contents);
            text_contents.setSingleLine();
            text_push_list_result = (TextView) itemView.findViewById(R.id.text_push_list_result);
            text_push_list_result.setVisibility(View.GONE);
            text_status = (TextView)itemView.findViewById(R.id.text_push_list_status);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_push_list, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final Msg item = mDataList.get(position);

        holder.text_contents.setText(item.getContents());

        String date = null;
        switch (mStatus) {

            case reserved:
                date = item.getReserveDate();
                holder.text_push_list_result.setVisibility(View.GONE);
                holder.text_status.setText(R.string.word_reservation_info);
                break;
            case finish:
                date = item.getCompleteDate();
                holder.text_push_list_result.setVisibility(View.VISIBLE);
                if(item.getType().equals("push")){
                    holder.text_push_list_result.setText(PplusCommonUtil.Companion.fromHtml(mContext.getString(R.string.html_push_result, item.getTargetCount(), item.getSuccessCount(), item.getFailCount(), item.getReadCount())));
                }else {
                    holder.text_push_list_result.setText(PplusCommonUtil.Companion.fromHtml(mContext.getString(R.string.html_sms_result, item.getTargetCount(), item.getSuccessCount(), item.getFailCount())));
                }
                holder.text_status.setText(R.string.word_send_info);

                break;
            case cancelSend:
                date = item.getRegDate();
                holder.text_push_list_result.setVisibility(View.GONE);
                holder.text_status.setVisibility(View.GONE);
                break;
        }

        if(StringUtils.isNotEmpty(date)){
            holder.text_date.setVisibility(View.VISIBLE);
            try {
                Date d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(date);
                SimpleDateFormat outputDate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat outputTime = new SimpleDateFormat("HH:mm");
                holder.text_date.setText(PplusCommonUtil.Companion.fromHtml(mContext.getString(R.string.html_reserved_date, outputDate.format(d), outputTime.format(d))));
            } catch (Exception e) {

            }
        }else{
            holder.text_date.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
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
