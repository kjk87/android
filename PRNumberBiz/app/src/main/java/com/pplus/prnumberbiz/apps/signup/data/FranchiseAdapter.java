package com.pplus.prnumberbiz.apps.signup.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.core.network.model.dto.Franchise;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class FranchiseAdapter extends RecyclerView.Adapter<FranchiseAdapter.ViewHolder>{

    private List<Franchise> mDataList;
    private OnItemClickListener listener;
    private Franchise mSelectData;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public FranchiseAdapter(Context context){

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

    public Franchise getItem(int position){

        return mDataList.get(position);
    }

    public List<Franchise> getDataList(){

        return mDataList;
    }

    public void add(Franchise data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<Franchise> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, Franchise data){

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

    public void setDataList(List<Franchise> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    public void setSelectData(int pos){

        this.mSelectData = mDataList.get(pos);
        notifyDataSetChanged();
    }

    public Franchise getSelectData(){

        return mSelectData;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView text_name;
        ImageView image_check;

        public ViewHolder(View itemView){

            super(itemView);
            text_name = (TextView) itemView.findViewById(R.id.text_franchise);
            image_check = (ImageView)itemView.findViewById(R.id.image_franchise_check);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_franchise, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){

        final Franchise item = mDataList.get(position);

        holder.text_name.setText(item.getName());

        if(mSelectData != null && mSelectData.equals(item)){
            holder.image_check.setVisibility(View.VISIBLE);
        }else{
            holder.image_check.setVisibility(View.INVISIBLE);
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
