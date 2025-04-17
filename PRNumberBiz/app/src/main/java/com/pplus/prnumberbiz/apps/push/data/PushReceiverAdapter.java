package com.pplus.prnumberbiz.apps.push.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pple.pplus.utils.part.format.FormatUtil;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.core.network.model.dto.Target;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class PushReceiverAdapter extends RecyclerView.Adapter<PushReceiverAdapter.ViewHolder>{

    private Context mContext;
    private List<Target> mDataList;
    private OnItemClickListener listener;
    private String mType;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public PushReceiverAdapter(Context context, String type){

        setHasStableIds(true);
        mContext = context;
        mDataList = new ArrayList<>();
        mType = type;
    }

    @Override
    public long getItemId(int position){

        return position;
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public Target getItem(int position){

        return mDataList.get(position);
    }

    public List<Target> getDataList(){

        return mDataList;
    }

    public void add(Target data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<Target> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, Target data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<Target> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_user_profileImg, image_user_checkbox;
        public TextView text_user_name, text_user_subInfo;
        public View layout_user_customer, text_user_recommend, image_user_more;

        public ViewHolder(View itemView){

            super(itemView);
            image_user_profileImg = (ImageView) itemView.findViewById(R.id.image_user_profileImg);
            text_user_name = (TextView) itemView.findViewById(R.id.text_user_name);
            text_user_subInfo = (TextView) itemView.findViewById(R.id.text_user_subInfo);
            image_user_checkbox = (ImageView) itemView.findViewById(R.id.image_user_checkbox);
            image_user_checkbox.setVisibility(View.GONE);
            layout_user_customer = itemView.findViewById(R.id.layout_user_customer);
            layout_user_customer.setVisibility(View.VISIBLE);
            text_user_recommend = itemView.findViewById(R.id.text_user_recommend);
            text_user_recommend.setVisibility(View.GONE);
            image_user_more = itemView.findViewById(R.id.image_user_more);
            image_user_more.setVisibility(View.GONE);
            text_user_recommend.setVisibility(View.GONE);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final Target item = getItem(position);

        if(mType.equals("push")){
            holder.text_user_name.setText(item.getUser().getDisplayName());
            if(item.getUser().getProfileImage() != null) {
                Glide.with(mContext).load(item.getUser().getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(holder.image_user_profileImg);
            } else {
                holder.image_user_profileImg.setImageResource(R.drawable.ic_gift_profile_default);
            }
            holder.text_user_subInfo.setVisibility(View.GONE);
        }else{
            holder.text_user_subInfo.setVisibility(View.VISIBLE);
            holder.text_user_name.setText(item.getCustomer().getName());
            holder.image_user_profileImg.setImageResource(R.drawable.ic_gift_profile_default);
            holder.text_user_subInfo.setText(FormatUtil.getPhoneNumber(item.getCustomer().getMobile()));
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
