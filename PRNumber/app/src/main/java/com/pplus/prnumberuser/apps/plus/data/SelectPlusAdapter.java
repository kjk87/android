//package com.pplus.prnumberuser.apps.plus.data;
//
//import android.content.Context;
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberuser.R;
//import com.pplus.prnumberuser.apps.plus.ui.SelectPlusConfigActivity;
//import com.pplus.prnumberuser.core.network.model.dto.Plus;
//import com.pplus.prnumberuser.core.util.PplusNumberUtil;
//
//import java.util.ArrayList;
//import java.util.List;
//
//
///**
// * Created by 김종경 on 2015-06-17.
// */
//public class SelectPlusAdapter extends RecyclerView.Adapter<SelectPlusAdapter.ViewHolder>{
//
//    private Context mContext;
//    private List<Plus> mDataList;
//    private OnItemClickListener listener;
//    private ArrayList<Plus> mSelectList;
//
//    public interface OnItemClickListener{
//
//        void onItemClick(int position);
//    }
//
//    public SelectPlusAdapter(Context context){
//
//        setHasStableIds(true);
//        mContext = context;
//        mDataList = new ArrayList<>();
//        mSelectList = new ArrayList<>();
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
//    public Plus getItem(int position){
//
//        return mDataList.get(position);
//    }
//
//    public List<Plus> getDataList(){
//
//        return mDataList;
//    }
//
//    public void add(Plus data){
//
//        if(mDataList == null) {
//            mDataList = new ArrayList<>();
//        }
//        mDataList.add(data);
//        notifyDataSetChanged();
//    }
//
//    public void addAll(List<Plus> dataList){
//
//        if(this.mDataList == null) {
//            this.mDataList = new ArrayList<>();
//        }
//
//        this.mDataList.addAll(dataList);
//        notifyDataSetChanged();
//    }
//
//    public void replaceData(int position, Plus data){
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
//    public void setDataList(List<Plus> dataList){
//
//        this.mDataList = dataList;
//        notifyDataSetChanged();
//    }
//
//    public ArrayList<Plus> getSelectList(){
//
//        return mSelectList;
//    }
//
//    public void setSelectList(ArrayList<Plus> mSelectList){
//
//        this.mSelectList = mSelectList;
//    }
//
//    public void noneSelect(){
//        mSelectList = new ArrayList<>();
//        notifyDataSetChanged();
//    }
//
//    public void allSelect(){
//        mSelectList = new ArrayList<>();
//        mSelectList.addAll(mDataList);
//        notifyDataSetChanged();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder{
//
//        public ImageView image_profileImg;
//        public TextView text_name, text_subInfo, text_subInfo2;
//        public View image_check;
//
//        public ViewHolder(View itemView){
//
//            super(itemView);
//            image_profileImg = itemView.findViewById(R.id.image_main_number_page_profileImg);
//            text_name = itemView.findViewById(R.id.text_main_number_page_name);
//            text_subInfo = itemView.findViewById(R.id.text_main_number_page_subInfo);
//            text_subInfo.setSingleLine();
//            text_subInfo2 = itemView.findViewById(R.id.text_main_number_page_subInfo2);
//            image_check = itemView.findViewById(R.id.image_main_number_checkbox);
//            image_check.setVisibility(View.VISIBLE);
//        }
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
//
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_number_page, parent, false);
//        return new ViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position){
//
//        final Plus item = getItem(position);
//
//        if(item.getProfileImage() != null && StringUtils.isNotEmpty(item.getProfileImage().getUrl())) {
//            Glide.with(mContext).load(item.getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image_profileImg);
//        } else {
//            holder.image_profileImg.setImageResource(R.drawable.prnumber_default_img);
//        }
//
//        holder.text_name.setText(""+item.getName());
//
//        if(StringUtils.isNotEmpty(item.getCatchphrase())){
//            holder.text_subInfo.setVisibility(View.VISIBLE);
//            holder.text_subInfo.setText(""+item.getCatchphrase());
//        }else{
//            holder.text_subInfo.setVisibility(View.GONE);
//        }
//
//        if(item.getNumberList() != null && item.getNumberList().size() > 0){
//            String number = item.getNumberList().get(0).getNumber();
//            holder.text_subInfo2.setVisibility(View.VISIBLE);
//            holder.text_subInfo2.setText(""+ PplusNumberUtil.getPrNumberFormat(number));
//        }else{
//            holder.text_subInfo2.setVisibility(View.GONE);
//        }
//
//        holder.itemView.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//                if(mSelectList.contains(item)){
//                    mSelectList.remove(item);
//                }else{
//                    mSelectList.add(item);
//                }
//
//                if(mContext instanceof SelectPlusConfigActivity){
//                    ((SelectPlusConfigActivity)mContext).isTotal();
//                }
//
//                notifyDataSetChanged();
//            }
//        });
//
//        holder.image_check.setSelected(mSelectList.contains(item));
//    }
//
//    @Override
//    public int getItemCount(){
//
//        return mDataList.size();
//    }
//}
