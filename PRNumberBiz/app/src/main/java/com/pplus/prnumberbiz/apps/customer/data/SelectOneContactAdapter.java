package com.pplus.prnumberbiz.apps.customer.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.core.database.entity.Contact;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class SelectOneContactAdapter extends RecyclerView.Adapter<SelectOneContactAdapter.ViewHolder>{

    private Context mContext;
    private List<Contact> mDataList;
    private OnItemClickListener listener;

    private Contact mSelectData;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public SelectOneContactAdapter(Context context){

        setHasStableIds(true);

        this.mContext = context;
        this.mDataList = new ArrayList<>();
    }

    public void setSelectData(int position){

        mSelectData = getItem(position);
        notifyDataSetChanged();
    }

    public Contact getSelectData(){

        return mSelectData;
    }

    @Override
    public long getItemId(int position){

        return position;
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public Contact getItem(int position){

        return mDataList.get(position);
    }

    public List<Contact> getDataList(){

        return mDataList;
    }

    public void add(Contact data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<Contact> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, Contact data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<Contact> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_user_profileImg, image_user_checkbox;
        public TextView text_user_name, text_user_subInfo;

        public ViewHolder(View itemView){

            super(itemView);

            image_user_profileImg = (ImageView) itemView.findViewById(R.id.image_user_profileImg);
            text_user_name = (TextView) itemView.findViewById(R.id.text_user_name);
            text_user_subInfo = (TextView) itemView.findViewById(R.id.text_user_subInfo);
            image_user_checkbox = (ImageView) itemView.findViewById(R.id.image_user_checkbox);
            image_user_checkbox.setImageResource(R.drawable.ic_gift_checkbox_on);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final Contact item = mDataList.get(position);

        holder.text_user_name.setText(""+item.getMemberName());
        holder.text_user_subInfo.setText(""+item.getMobileNumber());

        if(mSelectData != null && item.equals(mSelectData)) {
            holder.image_user_checkbox.setVisibility(View.VISIBLE);
        } else {
            holder.image_user_checkbox.setVisibility(View.GONE);
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
