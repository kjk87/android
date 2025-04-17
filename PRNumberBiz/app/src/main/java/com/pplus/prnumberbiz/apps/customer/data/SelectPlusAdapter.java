package com.pplus.prnumberbiz.apps.customer.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.customer.ui.SelectCustomerConfigActivity;
import com.pplus.prnumberbiz.apps.customer.ui.SelectPlusActivity;
import com.pplus.prnumberbiz.core.network.model.dto.Fan;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class SelectPlusAdapter extends RecyclerView.Adapter<SelectPlusAdapter.ViewHolder>{

    private Context mContext;
    private List<Fan> mDataList;
    private OnItemClickListener listener;
    private ArrayList<Fan> mSelectList;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public SelectPlusAdapter(Context context){

        setHasStableIds(true);
        mContext = context;
        mDataList = new ArrayList<>();
        mSelectList = new ArrayList<>();
    }

    @Override
    public long getItemId(int position){

        return mDataList.get(position).getNo();
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public Fan getItem(int position){

        return mDataList.get(position);
    }

    public List<Fan> getDataList(){

        return mDataList;
    }

    public void add(Fan data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<Fan> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, Fan data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<Fan> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    public ArrayList<Fan> getSelectList(){

        return mSelectList;
    }

    public void setSelectList(ArrayList<Fan> selectList){
        mSelectList = new ArrayList<>();
        mSelectList.addAll(selectList);
        notifyDataSetChanged();
    }

    public void noneSelect(){
        mSelectList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void allSelect(){
        mSelectList = new ArrayList<>();
        mSelectList.addAll(mDataList);
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
            image_user_checkbox.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final Fan item = getItem(position);

        holder.text_user_name.setText(item.getNickname());
        if(item.getProfileImage() != null){
            Glide.with(mContext).load(item.getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(holder.image_user_profileImg);
        }else{
            holder.image_user_profileImg.setImageResource(R.drawable.ic_gift_profile_default);
        }


        if(item.getPage() != null) {
            holder.text_user_name.setText(item.getPage().getName());
            holder.text_user_subInfo.setVisibility(View.VISIBLE);
            holder.text_user_subInfo.setText(item.getPage().getNumberList().get(0).getNumber());
        }else{
            holder.text_user_subInfo.setVisibility(View.GONE);
            holder.text_user_name.setText(item.getNickname());
        }


        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                if(mSelectList.contains(item)){
                    mSelectList.remove(item);
                }else{
                    mSelectList.add(item);
                }

                if(mContext instanceof SelectPlusActivity){
                    ((SelectPlusActivity)mContext).isTotal();
                }else if(mContext instanceof SelectCustomerConfigActivity){
                    ((SelectCustomerConfigActivity)mContext).isTotal();
                }

                notifyDataSetChanged();
            }
        });

        holder.image_user_checkbox.setSelected(mSelectList.contains(item));
    }

    @Override
    public int getItemCount(){

        return mDataList.size();
    }
}
