//package com.pplus.prnumberbiz.apps.cash.data;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.format.FormatUtil;
//import com.pplus.prnumberbiz.R;
//
//import java.util.ArrayList;
//
///**
// * Created by 김종경 on 2015-06-17.
// */
//public class CashChargeAdapter extends RecyclerView.Adapter<CashChargeAdapter.ViewHolder>{
//
//    private Context context;
//    private ArrayList<String> mDataList;
//    private OnClickListener mOnClickListener;
//
//    public interface OnClickListener{
//
//        void onClick(int position);
//    }
//
//    public CashChargeAdapter(Context context){
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
//    static class ViewHolder extends RecyclerView.ViewHolder{
//
//        public TextView text_name;
//
//        public ViewHolder(View itemView){
//
//            super(itemView);
//            text_name = (TextView) itemView.findViewById(R.id.text_cash_name);
//        }
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
//
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cash_grid, parent, false);
//        ViewHolder viewHolder = new ViewHolder(v);
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position){
//
//        final String item = mDataList.get(position);
//        holder.text_name.setText("+"+FormatUtil.getMoneyType(item) + " Cash");
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
