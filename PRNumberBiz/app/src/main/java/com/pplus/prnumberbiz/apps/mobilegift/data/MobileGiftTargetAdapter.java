package com.pplus.prnumberbiz.apps.mobilegift.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.mobilegift.ui.MobileGiftPurchaseActivity;
import com.pplus.prnumberbiz.core.network.model.dto.MsgTarget;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class MobileGiftTargetAdapter extends RecyclerView.Adapter<MobileGiftTargetAdapter.ViewHolder>{

    private Context mContext;
    private List<MsgTarget> mDataList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public MobileGiftTargetAdapter(Context context){

        setHasStableIds(true);

        this.mContext = context;
        this.mDataList = new ArrayList<>();
    }

    @Override
    public long getItemId(int position){

        return position;
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public MsgTarget getItem(int position){

        return mDataList.get(position);
    }

    public List<MsgTarget> getDataList(){

        return mDataList;
    }

    public void add(MsgTarget data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<MsgTarget> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, MsgTarget data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<MsgTarget> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        EditText edit_mobile, edit_name;
        View image_delete;

        public ViewHolder(View itemView){

            super(itemView);
            edit_mobile = (EditText) itemView.findViewById(R.id.edit_mobile_gift_target_mobile);
            edit_name = (EditText) itemView.findViewById(R.id.edit_mobile_gift_target_name);
            image_delete = itemView.findViewById(R.id.image_add_mobile_gift_target_delete);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mobile_gift_target, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final MsgTarget item = mDataList.get(position);


        if(StringUtils.isNotEmpty(item.getMobile())){
            holder.edit_mobile.setText(item.getMobile());
        }else{
            holder.edit_mobile.setText("");
        }

        if(StringUtils.isNotEmpty(item.getName())){
            holder.edit_name.setText(item.getName());
        }else{
            holder.edit_name.setText("");
        }

        holder.edit_mobile.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}

            @Override
            public void afterTextChanged(Editable s){
                mDataList.get(holder.getAdapterPosition()).setMobile(s.toString());
            }
        });

        holder.edit_name.addTextChangedListener(new TextWatcher(){

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count){}

            @Override
            public void afterTextChanged(Editable s){
                mDataList.get(holder.getAdapterPosition()).setName(s.toString());
            }
        });

        if(position == 0){
            holder.image_delete.setVisibility(View.GONE);
        }else{
            holder.image_delete.setVisibility(View.VISIBLE);
        }

        holder.image_delete.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                mDataList.remove(holder.getAdapterPosition());
                notifyDataSetChanged();
                ((MobileGiftPurchaseActivity)mContext).setData();
            }
        });
    }

    @Override
    public int getItemCount(){

        return mDataList.size();
    }
}
