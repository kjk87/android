package com.pplus.prnumberbiz.apps.pages.data;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.core.network.model.dto.Attachment;

import java.util.ArrayList;

/**
 * Created by 김종경 on 2015-06-17.
 */
public class IntroImageDeleteAdapter extends RecyclerView.Adapter<IntroImageDeleteAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Attachment> mDataList;
    private ArrayList<Attachment> mSelectDataList;

    public IntroImageDeleteAdapter(Context context){

        this.context = context;
        mSelectDataList = new ArrayList<>();
        mDataList = new ArrayList<>();
    }

    public ArrayList<Attachment> getSelectGalleryList(){

        return mSelectDataList;
    }

    public void setDataList(ArrayList<Attachment> dataList){

        mDataList = new ArrayList<>();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void add(Attachment data){

        mDataList.add(0, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mDataList.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView image_delete_image;
        public View delete_check;

        public ViewHolder(View itemView){

            super(itemView);
            image_delete_image = (ImageView) itemView.findViewById(R.id.image_delete_image);
            delete_check = itemView.findViewById(R.id.layout_delete_check);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_delete, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final Attachment item = mDataList.get(position);

        Glide.with(context).load(item.getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img).skipMemoryCache(true)).into(holder.image_delete_image);

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                if(!mSelectDataList.contains(item)) {
                    mSelectDataList.add(item);
                } else {
                    mSelectDataList.remove(item);
                }

                notifyDataSetChanged();

            }
        });

        if(mSelectDataList.contains(item)){
            holder.delete_check.setVisibility(View.VISIBLE);
        }else{
            holder.delete_check.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount(){

        return mDataList.size();
    }
}
