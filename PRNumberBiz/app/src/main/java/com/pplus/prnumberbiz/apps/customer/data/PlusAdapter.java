//package com.pplus.prnumberbiz.apps.customer.data;
//
//import android.app.Activity;
//import android.content.Context;
//import android.content.Intent;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.customer.ui.CustomerDirectRegActivity;
//import com.pplus.prnumberbiz.core.network.model.dto.Fan;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * Created by 김종경 on 2015-06-17.
// */
//public class PlusAdapter extends RecyclerView.Adapter<PlusAdapter.ViewHolder>{
//
//    private Context mContext;
//    private List<Fan> mDataList;
//    private OnItemClickListener listener;
//
//    public interface OnItemClickListener{
//
//        void onItemClick(int position);
//    }
//
//    public PlusAdapter(Context context){
//
//        setHasStableIds(true);
//        mContext = context;
//        mDataList = new ArrayList<>();
//    }
//
//    @Override
//    public long getItemId(int position){
//
//        return mDataList.get(position).getNo();
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener){
//
//        this.listener = listener;
//    }
//
//    public Fan getItem(int position){
//
//        return mDataList.get(position);
//    }
//
//    public List<Fan> getDataList(){
//
//        return mDataList;
//    }
//
//    public void add(Fan data){
//
//        if(mDataList == null) {
//            mDataList = new ArrayList<>();
//        }
//        mDataList.add(data);
//        notifyDataSetChanged();
//    }
//
//    public void addAll(List<Fan> dataList){
//
//        if(this.mDataList == null) {
//            this.mDataList = new ArrayList<>();
//        }
//
//        this.mDataList.addAll(dataList);
//        notifyDataSetChanged();
//    }
//
//    public void replaceData(int position, Fan data){
//
//        mDataList.remove(position);
//        mDataList.add(position, data);
//        notifyDataSetChanged();
//    }
//
//    public void clear(){
//
//        mDataList = new ArrayList<>();
//        notifyDataSetChanged();
//    }
//
//    public void setDataList(List<Fan> dataList){
//
//        this.mDataList = dataList;
//        notifyDataSetChanged();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder{
//
//        public ImageView image_user_profileImg, image_user_checkbox;
//        public TextView text_user_name, text_user_subInfo;
//        public View layout_user_customer, text_user_recommend, image_user_more;
//
//        public ViewHolder(View itemView){
//
//            super(itemView);
//            image_user_profileImg = (ImageView) itemView.findViewById(R.id.image_user_profileImg);
//            text_user_name = (TextView) itemView.findViewById(R.id.text_user_name);
//            text_user_subInfo = (TextView) itemView.findViewById(R.id.text_user_subInfo);
//            image_user_checkbox = (ImageView) itemView.findViewById(R.id.image_user_checkbox);
//            image_user_checkbox.setVisibility(View.GONE);
//            layout_user_customer = itemView.findViewById(R.id.layout_user_customer);
//            layout_user_customer.setVisibility(View.VISIBLE);
//            text_user_recommend = itemView.findViewById(R.id.text_user_recommend);
//            text_user_recommend.setVisibility(View.GONE);
//            image_user_more = itemView.findViewById(R.id.image_user_more);
//            image_user_more.setVisibility(View.GONE);
//            text_user_recommend.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
//
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
//        return new ViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position){
//
//        final Fan item = getItem(position);
//        if(item.getProfileImage() != null) {
//            Glide.with(mContext).load(item.getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(holder.image_user_profileImg);
//        } else {
//            holder.image_user_profileImg.setImageResource(R.drawable.ic_gift_profile_default);
//        }
//
//        if(StringUtils.isNotEmpty(item.getNickname())){
//            holder.text_user_name.setText(item.getNickname());
//            holder.text_user_name.setVisibility(View.VISIBLE);
//        }else{
//            holder.text_user_name.setVisibility(View.GONE);
//        }
//
//        holder.text_user_subInfo.setVisibility(View.GONE);
//
//
//        holder.itemView.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//
//                if(listener != null) {
//                    listener.onItemClick(holder.getAdapterPosition());
//                }
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount(){
//
//        return mDataList.size();
//    }
//}
