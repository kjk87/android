package com.pplus.prnumberbiz.apps.customer.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pple.pplus.utils.part.apps.resource.ResourceUtil;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.core.network.model.dto.SavedMsg;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class SelectSmsLockerAdapter extends RecyclerView.Adapter<SelectSmsLockerAdapter.ViewHolder>{

    private Context mContext;
    private List<SavedMsg> mDataList;
    private OnItemClickListener listener;

    private SavedMsg mSelectData;


    public HashMap<Integer, Integer> viewPageStates = new HashMap<>();

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public SelectSmsLockerAdapter(Context context){

        setHasStableIds(true);

        this.mContext = context;
        this.mDataList = new ArrayList<>();
    }

    public void setSelectData(SavedMsg selectData){
        mSelectData = selectData;
        notifyDataSetChanged();
    }

    public SavedMsg getSelectData(){
        return mSelectData;
    }

    @Override
    public long getItemId(int position){

        return mDataList.get(position).getNo();
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public SavedMsg getItem(int position){

        return mDataList.get(position);
    }

    public List<SavedMsg> getDataList(){

        return mDataList;
    }

    public void add(SavedMsg data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<SavedMsg> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, SavedMsg data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<SavedMsg> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_checkbox;
        public TextView text_type, text_msg, text_byte;

        public ViewHolder(View itemView){

            super(itemView);

            image_checkbox = (ImageView) itemView.findViewById(R.id.image_select_sms_locker_checkbox);
            text_type = (TextView)itemView.findViewById(R.id.text_select_sms_locker_type);
            text_msg = (TextView)itemView.findViewById(R.id.text_select_sms_locker_msg);
            text_byte = (TextView)itemView.findViewById(R.id.text_select_sms_locker_byte);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_sms_locker, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final SavedMsg item = mDataList.get(position);
        holder.text_msg.setText(""+item.getProperties().getMsg());

        int bytes = item.getProperties().getMsg().getBytes().length;
        if(bytes <= 80) {
            holder.text_type.setText(R.string.word_sms_en);
            holder.text_type.setBackgroundColor(ResourceUtil.getColor(mContext, R.color.color_618eff));
        }else{
            holder.text_type.setText(R.string.word_lms);
            holder.text_type.setBackgroundColor(ResourceUtil.getColor(mContext, R.color.color_6ebd52));
        }

        holder.text_byte.setText(String.valueOf(bytes)+" "+mContext.getString(R.string.word_byte_en));

        if(mSelectData != null && item.equals(mSelectData)){
            holder.image_checkbox.setVisibility(View.VISIBLE);
        }else{
            holder.image_checkbox.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                if(listener != null) {
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
