package com.pplus.prnumberuser.apps.bol.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pplus.prnumberuser.R;
import com.pplus.prnumberuser.core.network.model.dto.User;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class SelectUserAdapter extends RecyclerView.Adapter<SelectUserAdapter.ViewHolder>{

    private Context mContext;
    private List<User> mDataList;
    private OnItemClickListener listener;
    private ArrayList<User> mSelectList;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public SelectUserAdapter(Context context){

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

    public User getItem(int position){

        return mDataList.get(position);
    }

    public List<User> getDataList(){

        return mDataList;
    }

    public void add(User data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<User> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, User data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<User> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    public ArrayList<User> getSelectList(){

        return mSelectList;
    }

    public void setSelectList(ArrayList<User> selectList){

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

        final User item = getItem(position);

        if(item.getProfileImage() != null) {
            Glide.with(mContext).load(item.getProfileImage().getUrl()).apply(new RequestOptions().fitCenter().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(holder.image_user_profileImg);
        } else {
            holder.image_user_profileImg.setImageResource(R.drawable.ic_gift_profile_default);
        }

        holder.text_user_subInfo.setVisibility(View.GONE);
        holder.text_user_name.setText(item.getNickname());

        holder.image_user_checkbox.setSelected(mSelectList.contains(item));

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                if(mSelectList.contains(item)) {
                    mSelectList.remove(item);
                } else {
                    mSelectList.add(item);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount(){

        return mDataList.size();
    }
}
