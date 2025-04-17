package com.pplus.prnumberbiz.apps.post.data;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pple.pplus.utils.part.utils.time.DateFormatUtils;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.core.code.common.EnumData;
import com.pplus.prnumberbiz.core.network.model.dto.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class ReplyDetailAdapter extends RecyclerView.Adapter<ReplyDetailAdapter.ViewHolder>{

    private Context mContext;
    private List<Comment> mDataList;
    private OnItemClickListener listener;
    private int mTodayYear, mTodayMonth, mTodayDay;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public ReplyDetailAdapter(Context context){

        setHasStableIds(true);

        this.mContext = context;
        this.mDataList = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        mTodayYear = c.get(Calendar.YEAR);
        mTodayMonth = c.get(Calendar.MONTH);
        mTodayDay = c.get(Calendar.DAY_OF_MONTH);
    }

    @Override
    public long getItemId(int position){

        return position;
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public Comment getItem(int position){

        return mDataList.get(position);
    }

    public List<Comment> getDataList(){

        return mDataList;
    }

    public void add(Comment data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<Comment> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, Comment data){

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

    public void setDataList(List<Comment> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image_profileImage;
        TextView text_name, text_contents, text_regDate;

        public ViewHolder(View itemView){

            super(itemView);
            image_profileImage = (ImageView) itemView.findViewById(R.id.image_reply_detail_profileImage);
            text_name = (TextView) itemView.findViewById(R.id.text_reply_detail_name);
            text_contents = (TextView) itemView.findViewById(R.id.text_reply_detail_contents);
            text_regDate = (TextView) itemView.findViewById(R.id.text_reply_detail_regDate);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply_detail, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position){

        final Comment item = mDataList.get(position);

        if(item.getAuthor().getUseStatus().equals(EnumData.UseStatus.leave.name())){
            holder.text_name.setText(R.string.word_unknown);
            holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default);
        }else{
            if(item.getAuthor().getNo().equals(LoginInfoManager.getInstance().getUser().getNo())) {
                holder.text_name.setText("" + LoginInfoManager.getInstance().getUser().getPage().getName());
                if(LoginInfoManager.getInstance().getUser().getPage().getProfileImage() != null) {
                    Glide.with(mContext).load(LoginInfoManager.getInstance().getUser().getPage().getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(holder.image_profileImage);
                } else {
                    holder.image_profileImage.setImageResource(R.drawable.ic_gift_profile_default);
                }

            } else {
                holder.text_name.setText("" + item.getAuthor().getNickname());
                if(item.getAuthor().getProfileImage() != null) {
                    Glide.with(mContext).load(item.getAuthor().getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(holder.image_profileImage);
                } else {
                    holder.image_profileImage.setImageResource(R.drawable.ic_gift_profile_default);
                }
            }
        }

        holder.text_contents.setText("" + item.getComment());

        try {
            Date d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.getRegDate());
            Calendar c = Calendar.getInstance();
            c.setTime(d);

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            if(mTodayYear == year && mTodayMonth == month && mTodayDay == day) {
                SimpleDateFormat output = new SimpleDateFormat("a HH:mm");
                holder.text_regDate.setText(output.format(d));
            } else {
                SimpleDateFormat output = new SimpleDateFormat("yyyy/MM/dd");
                holder.text_regDate.setText(output.format(d));
            }

        } catch (Exception e) {

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
