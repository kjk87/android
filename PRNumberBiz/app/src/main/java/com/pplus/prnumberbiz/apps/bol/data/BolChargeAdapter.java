//package com.pplus.prnumberbiz.apps.bol.data;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.text.Html;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.format.FormatUtil;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//
//import java.util.ArrayList;
//
///**
// * Created by 김종경 on 2015-06-17.
// */
//public class BolChargeAdapter extends RecyclerView.Adapter<BolChargeAdapter.ViewHolder>{
//
//    private Context context;
//    private ArrayList<String> mDataList;
//    private OnClickListener mOnClickListener;
//    private String mSelectData;
//
//    public interface OnClickListener{
//
//        void onClick(int position);
//    }
//
//    public BolChargeAdapter(Context context){
//
//        this.context = context;
//        mDataList = new ArrayList<>();
//    }
//
//    public void setOnClickListener(OnClickListener OnClickListener){
//
//        this.mOnClickListener = OnClickListener;
//    }
//
//    public void setDataList(ArrayList<String> dataList){
//
//        mDataList = new ArrayList<>();
//        mDataList.addAll(dataList);
//        notifyDataSetChanged();
//    }
//
//    public void add(String data){
//
//        mDataList.add(0, data);
//        notifyDataSetChanged();
//    }
//
//    public void clear(){
//
//        mDataList.clear();
//        notifyDataSetChanged();
//    }
//
//    public String getSelectData(){
//
//        return mSelectData;
//    }
//
//    public void setSelectData(String data){
//
//        this.mSelectData = data;
//        notifyDataSetChanged();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder{
//
//        public TextView text_price;
//
//        public ViewHolder(View itemView){
//
//            super(itemView);
//            text_price = (TextView) itemView.findViewById(R.id.text_bol_price);
//        }
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
//
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bol_charge, parent, false);
//        ViewHolder viewHolder = new ViewHolder(v);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position){
//
//        final String item = mDataList.get(position);
//        holder.text_price.setText(PplusCommonUtil.Companion.fromHtml(context.getString(R.string.html_bol_price, FormatUtil.getMoneyType(item))));
//
//        holder.text_price.setSelected(item.equals(mSelectData));
//
//        holder.itemView.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//
//                if(mOnClickListener != null) {
//                    mOnClickListener.onClick(holder.getAdapterPosition());
//                }
//            }
//        });
//
//    }
//
//    @Override
//    public int getItemCount(){
//
//        return mDataList.size();
//    }
//}
