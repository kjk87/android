package com.pplus.prnumberbiz.apps.signup.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.core.network.model.dto.FranchiseGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class FranchiseGroupAdapter extends RecyclerView.Adapter<FranchiseGroupAdapter.ViewHolder>{

    private List<FranchiseGroup> mDataList;
    private OnItemClickListener listener;
    private FranchiseGroup mSelectGroup;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public FranchiseGroupAdapter(Context context){

        setHasStableIds(true);

        this.mDataList = new ArrayList<>();
    }

    @Override
    public long getItemId(int position){

        return position;
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public FranchiseGroup getItem(int position){

        return mDataList.get(position);
    }

    public List<FranchiseGroup> getDataList(){

        return mDataList;
    }

    public void add(FranchiseGroup data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<FranchiseGroup> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, FranchiseGroup data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void remove(int position){

        mDataList.remove(position);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<FranchiseGroup> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    public void setSelectGroup(int pos){

        this.mSelectGroup = mDataList.get(pos);
        notifyDataSetChanged();
    }

    public FranchiseGroup getSelectGroup(){

        return mSelectGroup;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView text_group;

        public ViewHolder(View itemView){

            super(itemView);
            text_group = (TextView) itemView.findViewById(R.id.text_franchise_group);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_franchise_group, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){

        final FranchiseGroup item = mDataList.get(position);

        holder.text_group.setText(item.getName());

        if(mSelectGroup != null && mSelectGroup.equals(item)){
            holder.itemView.setSelected(true);
        }else{
            holder.itemView.setSelected(false);
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
