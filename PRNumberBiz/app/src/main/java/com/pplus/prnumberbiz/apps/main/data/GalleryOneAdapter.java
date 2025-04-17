package com.pplus.prnumberbiz.apps.main.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.main.ui.OneGalleryActivity;
import com.pplus.prnumberbiz.core.network.model.dto.GalleryData;

import java.util.ArrayList;

/**
 * Created by 김종경 on 2015-06-17.
 */
public class GalleryOneAdapter extends RecyclerView.Adapter<GalleryOneAdapter.ViewHolder>{

    private Context context;
    private ArrayList<GalleryData> mGalleryList;
    private int mBeforePosition = -1;

    public GalleryOneAdapter(Context context){

        this.context = context;
        mGalleryList = new ArrayList<>();
    }
    public void addAll(ArrayList<GalleryData> dataList){

        mGalleryList = dataList;
        notifyDataSetChanged();
    }

    public void add(GalleryData data){
        if(mBeforePosition != -1){
            mGalleryList.get(mBeforePosition).setChecked(false);
        }

        mGalleryList.add(0, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mGalleryList.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageGallery;
        public View layout_gallery_on_check;

        public ViewHolder(View itemView){

            super(itemView);
            imageGallery = (ImageView) itemView.findViewById(R.id.image_gallery_one);
            layout_gallery_on_check = itemView.findViewById(R.id.layout_gallery_on_check);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery_one, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position){

        final GalleryData item = mGalleryList.get(position);

        Glide.with(context).load(item.getImageUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).listener(new RequestListener<Drawable>(){

            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource){

                item.setIsBroken(true);
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource){

                return false;
            }
        }).into(holder.imageGallery);

        holder.itemView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                if(!item.isBroken()) {
                    if(mBeforePosition != -1){
                        mGalleryList.get(mBeforePosition).setChecked(false);
                    }
                    if(mBeforePosition != holder.getAdapterPosition()){
                        mBeforePosition = holder.getAdapterPosition();
                        mGalleryList.get(holder.getAdapterPosition()).setChecked(true);

                        ((OneGalleryActivity)context).setSelect(item);

                        notifyDataSetChanged();
                    }
                }else {
                    Toast.makeText(context, R.string.msg_do_not_attach_broken_image, Toast.LENGTH_SHORT).show();
                }
            }
        });

        if(item.getChecked()){
            holder.layout_gallery_on_check.setVisibility(View.VISIBLE);
        }else{
            holder.layout_gallery_on_check.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount(){

        return mGalleryList.size();
    }
}
