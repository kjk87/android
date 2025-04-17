//package com.pplus.prnumberbiz.apps.note.data;
//
//import android.content.Context;
//import android.os.Bundle;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.ui.custom.swipeLayout.SwipeRevealLayout;
//import com.pplus.prnumberbiz.apps.common.ui.custom.swipeLayout.ViewBinderHelper;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.NoteReceive;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
///**
// * Created by ksh on 2016-09-05.
// */
//public class NoteContainerAdapter extends RecyclerView.Adapter<NoteContainerAdapter.ViewHolder>{
//
//    private static final String TAG = NoteContainerAdapter.class.getSimpleName();
//    private List<NoteReceive> mDataList;
//    private OnItemClickListener listener;
//    private Context mContext;
//    private final ViewBinderHelper binderHelper = new ViewBinderHelper();
//
//    public void saveStates(Bundle outState){
//
//        binderHelper.saveStates(outState);
//    }
//
//    public void restoreStates(Bundle inState){
//
//        binderHelper.restoreStates(inState);
//    }
//
//    public NoteContainerAdapter(Context context){
//
//        mDataList = new ArrayList<>();
//        mContext = context;
//        binderHelper.setOpenOnlyOne(true);
//    }
//
//    public void setItemList(ArrayList<NoteReceive> mItemList){
//
//        this.mDataList = mItemList;
//        notifyDataSetChanged();
//    }
//
//    public interface OnItemClickListener{
//
//        void onItemClick(int position);
//    }
//
//    public interface OnItemLongClickListener{
//
//        void onItemLongClick(int position);
//    }
//
//    public NoteReceive getItem(int postion){
//
//        return mDataList != null ? mDataList.get(postion) : null;
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener){
//
//        this.listener = listener;
//    }
//
//    public void addAll(List<NoteReceive> channels){
//
//        mDataList.addAll(channels);
//    }
//
//    public void clear(){
//
//        mDataList.clear();
//    }
//
//    public void replace(NoteReceive data){
//
//        for(NoteReceive noteReceive : mDataList) {
//            if(noteReceive.getNo().equals(data.getNo())) {
//                mDataList.remove(noteReceive);
//                break;
//            }
//        }
//
//        mDataList.add(0, data);
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public int getItemViewType(int position){
//
//        return position;
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
//
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_container, parent, false);
//        ViewHolder viewHolder = new ViewHolder(view);
//
//        return viewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position){
//
//        final NoteReceive item = mDataList.get(position);
//
//        binderHelper.bind(holder.swipeLayout, String.valueOf(position));
//
//        holder.layout.setOnClickListener(new View.OnClickListener(){
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
//        if(item.isReaded()){
//            holder.image_read.setVisibility(View.GONE);
//        }else{
//            holder.image_read.setVisibility(View.VISIBLE);
//        }
//
//        if(item != null && item.getAuthor() != null && item.getAuthor().getProfileImage() != null) {
//            Glide.with(mContext).load(item.getAuthor().getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(holder.image);
//        }
//        holder.text_name.setText(item.getAuthor().getNickname());
//
//        // 마지막 메시지 출력
//        holder.text_contents.setText(item.getContents());
//        // 나가기 처리
//        holder.text_delete.setTag(position);
//        holder.text_delete.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//
//                Map<String, String> params = new HashMap<String, String>();
//                params.put("no", ""+item.getNo());
//                ApiBuilder.create().deleteReceiveNote(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//                    @Override
//                    public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//                        mDataList.remove(holder.getAdapterPosition());
//                        notifyItemRemoved(holder.getAdapterPosition());
//                    }
//
//                    @Override
//                    public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
//
//                    }
//                }).build().call();
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount(){
//
//        return mDataList != null ? mDataList.size() : null;
//    }
//
//    public class ViewHolder extends RecyclerView.ViewHolder{
//
//        SwipeRevealLayout swipeLayout;
//        LinearLayout layout;
//        ImageView image;
//        View image_read;
//        TextView text_name, text_contents, text_date, text_delete;
//
//        public ViewHolder(View itemView){
//
//            super(itemView);
//
//            swipeLayout = (SwipeRevealLayout) itemView.findViewById(R.id.swipe_layout);
//            layout = (LinearLayout) itemView.findViewById(R.id.layout_note_container);
//            image = (ImageView) itemView.findViewById(R.id.image_note_container);
//            text_name = (TextView) itemView.findViewById(R.id.text_note_container_name);
//            text_contents = (TextView) itemView.findViewById(R.id.text_note_container_contents);
//            text_date = (TextView)itemView.findViewById(R.id.text_note_container_date);
//            text_delete = (TextView) itemView.findViewById(R.id.text_note_container_delete);
//            image_read = itemView.findViewById(R.id.image_message_container_read);
//            text_contents.setSingleLine();
//        }
//
//    }
//
//
//}
