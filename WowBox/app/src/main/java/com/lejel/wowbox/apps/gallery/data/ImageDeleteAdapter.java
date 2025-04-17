package com.lejel.wowbox.apps.gallery.data;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lejel.wowbox.R;

import java.util.ArrayList;

/**
 * Created by 김종경 on 2015-06-17.
 */
public class ImageDeleteAdapter extends RecyclerView.Adapter<ImageDeleteAdapter.ViewHolder>{

    private Context context;
    private ArrayList<Uri> mDataList;
    private ArrayList<Uri> mSelectDataList;

    public ImageDeleteAdapter(Context context){

        this.context = context;
        mSelectDataList = new ArrayList<>();
        mDataList = new ArrayList<>();
    }

    public ArrayList<Uri> getSelectGalleryList(){

        return mSelectDataList;
    }

    public void setDataList(ArrayList<Uri> dataList){

        mDataList = new ArrayList<>();
        mDataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void add(Uri data){

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

        final Uri item = mDataList.get(position);

        Glide.with(context).load(item.getPath()).apply(new RequestOptions().centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(holder.image_delete_image);

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
        if(mSelectDataList.contains(item)) {
            holder.delete_check.setVisibility(View.VISIBLE);
        } else {
            holder.delete_check.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount(){

        return mDataList.size();
    }
}
