//package com.pplus.prnumberbiz.apps.pages.data;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.pplus.prnumberbiz.R;
//
//import java.util.ArrayList;
//
///**
// * Created by Windows7-00 on 2016-12-23.
// */
//
//public class SearchWordAdapter extends RecyclerView.Adapter<SearchWordAdapter.ViewHolder>{
//
//    private OnItemClickListener listener;
//    private DataChangedListener mDataChangedListener;
//
//    private int[] resIds = {R.drawable.border_color_ffedf5, R.drawable.border_color_ffe0e0, R.drawable.border_color_ffeee4, R.drawable.border_color_fffbdc, R.drawable.border_color_ecfff7, R.drawable.border_color_e0f4ff, R.drawable.border_color_f1edff};
//
//    public interface OnItemClickListener{
//
//        void onItemClick(int position);
//    }
//
//    public interface DataChangedListener{
//
//        void onChanged(int position);
//    }
//
//    private ArrayList<String> mDataList;
//
//    public SearchWordAdapter(){
//
//        this.mDataList = new ArrayList<>();
//    }
//
//    public void setDataChangedListener(DataChangedListener dataChangedListener){
//
//        this.mDataChangedListener = dataChangedListener;
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener){
//
//        this.listener = listener;
//    }
//
//    public void add(String data){
//
//        if(mDataList == null) {
//            mDataList = new ArrayList<>();
//        }
//        mDataList.add(data);
//        notifyDataSetChanged();
//    }
//
//    public void addAll(ArrayList<String> dataList){
//
//        if(this.mDataList == null) {
//            this.mDataList = new ArrayList<>();
//        }
//
//        this.mDataList.addAll(dataList);
//        notifyDataSetChanged();
//    }
//
//    public void clear(){
//
//        mDataList = new ArrayList<>();
//        notifyDataSetChanged();
//    }
//
//    public void setDataList(ArrayList<String> dataList){
//
//        if(dataList != null) {
//            this.mDataList = dataList;
//            notifyDataSetChanged();
//        }
//    }
//
//    public String getItem(int position){
//
//        return mDataList.get(position);
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder{
//
//        RelativeLayout container;
//        TextView textRecent;
//        ImageView imageDelete;
//        View layoutRecent;
//
//        public ViewHolder(View itemView){
//
//            super(itemView);
//            container = (RelativeLayout) itemView.findViewById(R.id.rl_item_container);
//            layoutRecent = itemView.findViewById(R.id.layout_search_word_recent);
//            textRecent = (TextView) itemView.findViewById(R.id.text_search_word_recent);
//            textRecent.setSingleLine();
//            imageDelete = (ImageView) itemView.findViewById(R.id.image_search_word_delete);
//        }
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
//
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_keyword, parent, false);
//        return new ViewHolder(v);
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position){
//
//        holder.layoutRecent.setVisibility(View.VISIBLE);
//        holder.textRecent.setText(mDataList.get(position));
//        holder.imageDelete.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//
//                int position = holder.getAdapterPosition();
//
//                if(mDataChangedListener != null) {
//                    mDataChangedListener.onChanged(position);
//                }
//                notifyDataSetChanged();
//            }
//        });
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
//
//    }
//
//    @Override
//    public int getItemCount(){
//
//        return mDataList.size();
//    }
//}
