package com.pplus.prnumberuser.apps.mobilegift.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pplus.utils.part.format.FormatUtil;
import com.pplus.prnumberuser.R;
import com.pplus.prnumberuser.core.network.model.dto.MobileGiftHistoryTarget;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class MobileGiftHistoryTargetAdapter extends RecyclerView.Adapter<MobileGiftHistoryTargetAdapter.ViewHolder>{

    private Context mContext;
    private List<MobileGiftHistoryTarget> mDataList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public MobileGiftHistoryTargetAdapter(Context context){

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

    public MobileGiftHistoryTarget getItem(int position){

        return mDataList.get(position);
    }

    public List<MobileGiftHistoryTarget> getDataList(){

        return mDataList;
    }

    public void add(MobileGiftHistoryTarget data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<MobileGiftHistoryTarget> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, MobileGiftHistoryTarget data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<MobileGiftHistoryTarget> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView text_name, text_mobile_number;

        public ViewHolder(View itemView){

            super(itemView);
            text_name = (TextView)itemView.findViewById(R.id.text_mobile_gift_history_detail_target_name);
            text_mobile_number = (TextView)itemView.findViewById(R.id.text_mobile_gift_history_detail_target_mobile_number);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mobile_gift_history_target, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final MobileGiftHistoryTarget item = mDataList.get(position);
        holder.text_name.setText(item.getName());
        holder.text_mobile_number.setText(FormatUtil.getPhoneNumber(item.getMobile()));

    }

    @Override
    public int getItemCount(){

        return mDataList.size();
    }
}
