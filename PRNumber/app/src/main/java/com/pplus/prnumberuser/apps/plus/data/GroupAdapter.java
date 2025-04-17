package com.pplus.prnumberuser.apps.plus.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pplus.prnumberuser.R;
import com.pplus.prnumberuser.core.network.model.dto.Group;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder>{

    private Context mContext;
    private List<Group> mDataList;
    private OnItemClickListener listener;
    private Group mSelectGroup;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public GroupAdapter(Context context){

        setHasStableIds(true);

        this.mContext = context;
        this.mDataList = new ArrayList<>();
    }

    @Override
    public long getItemId(int position){

        return mDataList.get(position).getNo(); // need to return stable (= not change even after reordered) value
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public Group getItem(int position){

        return mDataList.get(position);
    }

    public List<Group> getDataList(){

        return mDataList;
    }

    public void add(Group data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<Group> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, Group data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<Group> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    public void setSelectGroup(Group group){

        this.mSelectGroup = group;
        notifyDataSetChanged();
    }

    public Group getSelectGroup(){

        return mSelectGroup;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textName;

        public ViewHolder(View itemView){

            super(itemView);
            textName = (TextView) itemView.findViewById(R.id.text_group_name);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final Group item = mDataList.get(position);
        holder.textName.setText(item.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                if(listener != null){
                    listener.onItemClick(holder.getAdapterPosition());
                }
                notifyDataSetChanged();
            }
        });

        if(mSelectGroup != null && item.getNo().equals(mSelectGroup.getNo())){
            holder.itemView.setSelected(true);
        }else{
            holder.itemView.setSelected(false);
        }
    }

    @Override
    public int getItemCount(){

        return mDataList.size();
    }
}
