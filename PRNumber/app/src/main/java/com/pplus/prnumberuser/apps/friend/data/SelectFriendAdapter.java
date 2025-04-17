package com.pplus.prnumberuser.apps.friend.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pplus.prnumberuser.core.database.entity.ContactDao;
import com.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberuser.R;
import com.pplus.prnumberuser.core.database.DBManager;
import com.pplus.prnumberuser.core.database.entity.Contact;
import com.pplus.prnumberuser.core.network.model.dto.Friend;
import com.pplus.prnumberuser.core.network.model.dto.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 김종경 on 2016-08-24.
 */
public class SelectFriendAdapter extends RecyclerView.Adapter<SelectFriendAdapter.ViewHolder>{

    private Context mContext;

    public static class Header{

        public Header(String name, int count){

            this.name = name;
            this.count = count;
        }

        private String name;
        private int count;

        public String getName(){

            return name;
        }

        public void setName(String name){

            this.name = name;
        }

        public int getCount(){

            return count;
        }

        public void setCount(int count){

            this.count = count;
        }
    }

    private ContactDao mContactDao;
    private OnItemClickListener listener;
    private ArrayList<User> mSelectList;
    List<Friend> mDataList;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public SelectFriendAdapter(Context context){

        super();
        mContext = context;
        mContactDao = DBManager.getInstance(context).getSession().getContactDao();
        mDataList = new ArrayList<>();
        mSelectList = new ArrayList<>();
    }

    @Override
    public long getItemId(int position){

        return position;
    }

    public Friend getItem(int position){

        return mDataList.get(position);
    }

    public List<Friend> getDataList(){

        return mDataList;
    }

    public void add(Friend data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<Friend> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, Friend data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<Friend> dataList){

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

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final Friend item = getItem(position);
        List<Contact> contacts = mContactDao.queryBuilder().where(ContactDao.Properties.MobileNumber.eq(item.getMobile())).list();
        String name = null;
        if(contacts != null && contacts.size() > 0) {
            name = contacts.get(0).getMemberName();
        } else {
            if(StringUtils.isNotEmpty(item.getFriend().getNickname())) {
                name = item.getFriend().getNickname();
            } else {
                name = mContext.getString(R.string.word_unknown);
            }
        }

        if(item.getFriend().getProfileImage() != null) {
            Glide.with(mContext).load(item.getFriend().getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(holder.image_profileImg);
        } else {
            holder.image_profileImg.setImageResource(R.drawable.ic_gift_profile_default);
        }

        holder.text_name.setText(name);
        holder.text_subInfo.setText(item.getFriend().getNickname());


        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                if(mSelectList.contains(item.getFriend())) {
                    mSelectList.remove(item.getFriend());
                } else {
                    mSelectList.add(item.getFriend());
                }
                if(listener != null) {
                    listener.onItemClick(holder.getAdapterPosition());
                }
                notifyDataSetChanged();
            }
        });

        holder.image_check.setSelected(mSelectList.contains(item.getFriend()));
    }

    @Override
    public int getItemCount(){

        return mDataList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_friend, parent, false);
        return new ViewHolder(v);
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profileImg;
        public TextView text_name, text_subInfo;
        public View image_check;

        public ViewHolder(View itemView){

            super(itemView);
            image_profileImg = itemView.findViewById(R.id.image_select_friend_profileImg);
            text_name = itemView.findViewById(R.id.text_select_friend_name);
            text_subInfo = itemView.findViewById(R.id.text_select_friend_subInfo);
            text_subInfo.setSingleLine();
            image_check = (ImageView) itemView.findViewById(R.id.image_select_friend_checkbox);
        }
    }

}
