package com.pplus.prnumberuser.apps.main.data;

import android.content.Context;
import android.graphics.Color;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberuser.R;
import com.pplus.prnumberuser.core.network.model.dto.Page;
import com.pplus.prnumberuser.core.util.PplusNumberUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 김종경 on 2015-06-17.
 */
public class MainNumberAdapter extends RecyclerView.Adapter<MainNumberAdapter.ViewHolder>{

    private Context mContext;
    private List<Page> mDataList;
    private OnItemClickListener listener;
    private String mSearchNumber;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public MainNumberAdapter(Context context){

        setHasStableIds(true);
        mContext = context;
        mDataList = new ArrayList<>();
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
    }

    public void setSearchNumber(String searchNumber){
        mSearchNumber = searchNumber;
//        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position){

        return position;
    }

    public Page getItem(int position){

        return mDataList.get(position);
    }

    public List<Page> getDataList(){

        return mDataList;
    }

    public void add(Page data){

        if(mDataList == null) {
            mDataList = new ArrayList<>();
        }
        mDataList.add(data);
        notifyDataSetChanged();
    }

    public void addAll(List<Page> dataList){

        if(this.mDataList == null) {
            this.mDataList = new ArrayList<>();
        }

        this.mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void replaceData(int position, Page data){

        mDataList.remove(position);
        mDataList.add(position, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList = new ArrayList<>();
        notifyDataSetChanged();
    }

    public void setDataList(List<Page> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }
    static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_profileImg;
        public TextView text_name, text_subInfo, text_subInfo2;

        public ViewHolder(View itemView){

            super(itemView);
            image_profileImg = itemView.findViewById(R.id.image_main_number_page_profileImg);
            text_name = itemView.findViewById(R.id.text_main_number_page_name);
            text_subInfo = itemView.findViewById(R.id.text_main_number_page_subInfo);
            text_subInfo.setSingleLine();
            text_subInfo2 = itemView.findViewById(R.id.text_main_number_page_subInfo2);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main_number_page, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final Page item = getItem(position);

        if(StringUtils.isNotEmpty(item.getThumbnail())) {
            Glide.with(mContext).load(item.getThumbnail()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image_profileImg);
        } else {
            holder.image_profileImg.setImageResource(R.drawable.prnumber_default_img);
        }

        holder.text_name.setText(""+item.getName());

        if(StringUtils.isNotEmpty(item.getCatchphrase())){
            holder.text_subInfo.setVisibility(View.VISIBLE);
            holder.text_subInfo.setText(""+item.getCatchphrase());
        }else{
            holder.text_subInfo.setVisibility(View.GONE);
        }

        if(item.getNumberList() != null && item.getNumberList().size() > 0){
            String number = item.getNumberList().get(0).getVirtualNumber();
            holder.text_subInfo2.setVisibility(View.VISIBLE);
            holder.text_subInfo2.setText(""+ PplusNumberUtil.getPrNumberFormat(number));

            if(StringUtils.isNotEmpty(mSearchNumber) && number.contains(mSearchNumber)){
                Spannable spannable = new SpannableString(number);
                int pos = number.indexOf(mSearchNumber);
                spannable.setSpan(new ForegroundColorSpan(Color.parseColor("#8700ff")), pos, pos+mSearchNumber.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                holder.text_subInfo2.setText(spannable);
            }
        }else{
            holder.text_subInfo2.setVisibility(View.GONE);
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
