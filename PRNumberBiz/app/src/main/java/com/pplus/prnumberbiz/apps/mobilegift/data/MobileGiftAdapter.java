package com.pplus.prnumberbiz.apps.mobilegift.data;

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
import com.pplus.prnumberbiz.core.network.model.dto.MobileGift;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class MobileGiftAdapter extends RecyclerView.Adapter<MobileGiftAdapter.ViewHolder>{

    private Context mContext;
    private List<MobileGift> mDataList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public MobileGiftAdapter(Context context){

        setHasStableIds(true);

        this.mContext = context;
        this.mDataList = new ArrayList<>();
    }

    @Override
    public long getItemId(int position){

        return mDataList.get(position).getNo();
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public MobileGift getItem(int position){

        return mDataList.get(position);
    }

    public List<MobileGift> getDataList(){

        return mDataList;
    }

    public void add(MobileGift data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<MobileGift> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, MobileGift data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<MobileGift> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        ImageView image;
        TextView text_name, text_price;

        public ViewHolder(View itemView){

            super(itemView);
            image = (ImageView)itemView.findViewById(R.id.image_mobile_gift);
            text_name = (TextView)itemView.findViewById(R.id.text_mobile_gift_name);
            text_price = (TextView)itemView.findViewById(R.id.text_mobile_gift_price);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mobile_gift, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final MobileGift item = mDataList.get(position);
        holder.text_name.setText(item.getName());

        Glide.with(mContext).load(item.getViewImage1()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image);

        holder.text_price.setText(PplusCommonUtil.Companion.fromHtml(mContext.getString(R.string.html_money_unit, FormatUtil.getMoneyType(String.valueOf(item.getSalesPrice())))));

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
