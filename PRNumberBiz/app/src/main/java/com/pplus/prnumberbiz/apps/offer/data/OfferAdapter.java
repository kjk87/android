package com.pplus.prnumberbiz.apps.offer.data;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.core.network.model.dto.Group;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class OfferAdapter extends RecyclerView.Adapter<OfferAdapter.ViewHolder>{

    private Context mContext;
    private List<Group> mDataList;
    private OnItemClickListener listener;
    private Group mSelectGroup;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public OfferAdapter(Context context){

        setHasStableIds(true);

        this.mContext = context;
        this.mDataList = new ArrayList<>();
    }

    @Override
    public long getItemId(int position){

        return mDataList.get(position).getNo(); // need to return stable (= not change even after reordered) value
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public Group getItem(int position){

        return mDataList.get(position);
    }

    public List<Group> getDataList(){

        return mDataList;
    }

    public void add(Group data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<Group> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, Group data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<Group> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    public void setSelectGroup(Group group){

        this.mSelectGroup = group;
        notifyDataSetChanged();
    }

    public Group getSelectGroup(){

        return mSelectGroup;
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image_profile, image_offer;
        TextView text_name, text_title, text_contents, text_offer_date, text_offer_reply;
        private View text_offer_look_knows, text_offer_reply_new, text_offer_closed;

        public ViewHolder(View itemView){

            super(itemView);
            image_profile = (ImageView) itemView.findViewById(R.id.image_offer_profile);
            text_name = (TextView) itemView.findViewById(R.id.text_offer_name);
            image_offer = (ImageView) itemView.findViewById(R.id.image_offer);
            text_title = (TextView) itemView.findViewById(R.id.text_offer_title);
            text_contents = (TextView) itemView.findViewById(R.id.text_offer_contents);
            text_offer_look_knows = itemView.findViewById(R.id.text_offer_look_knows);
            text_offer_date = (TextView)itemView.findViewById(R.id.text_offer_date);
            text_offer_reply = (TextView)itemView.findViewById(R.id.text_offer_reply);
            text_offer_reply_new = itemView.findViewById(R.id.text_offer_reply_new);
            text_offer_closed = itemView.findViewById(R.id.text_offer_closed);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offer, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final Group item = mDataList.get(position);
    }

    @Override
    public int getItemCount(){

        return mDataList.size();
    }
}
