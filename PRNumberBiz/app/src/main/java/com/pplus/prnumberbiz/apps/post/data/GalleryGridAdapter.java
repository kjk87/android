package com.pplus.prnumberbiz.apps.post.data;

import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.core.network.model.dto.GalleryData;
import com.pplus.prnumberbiz.apps.post.ui.PostGalleryActivity;
import com.pplus.prnumberbiz.core.util.ToastUtil;

import java.util.ArrayList;

/**
 * Created by 김종경 on 2015-06-17.
 */
public class GalleryGridAdapter extends RecyclerView.Adapter<GalleryGridAdapter.ViewHolder>{

    private int mMaxCount = Const.IMAGE_UPLOAD_MAX_COUNT;
    private Context context;
    private ArrayList<GalleryData> mGalleryList;
    private ArrayList<GalleryData> mSelectGalleryList;
    private int mLastCheckPosition = -1;
    private boolean blocking = false;

    public GalleryGridAdapter(Context context){

        this.context = context;
        mSelectGalleryList = new ArrayList<>();
        mGalleryList = new ArrayList<>();
    }

    public boolean isBlocking(){

        return blocking;
    }

    public void setBlocking(boolean blocking){

        this.blocking = blocking;
    }

    public void setMaxCount(int maxCount){

        this.mMaxCount = maxCount;
    }

    public void setSelectGalleryList(ArrayList<GalleryData> selectGalleryList){

        this.mSelectGalleryList = selectGalleryList;
    }

    public ArrayList<GalleryData> getSelectGalleryList(){

        return mSelectGalleryList;
    }

    public void selectLastPhoto(){

        if(mSelectGalleryList.size() < mMaxCount) {
            mSelectGalleryList.add(mGalleryList.get(0));
            notifyDataSetChanged();
        }
    }

    public void addAll(ArrayList<GalleryData> dataList){

        mGalleryList = new ArrayList<>();
        mGalleryList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void add(GalleryData data){

        mGalleryList.add(0, data);
        notifyDataSetChanged();
    }

    public void clear(){

        mGalleryList.clear();
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView imageGallery;
        public TextView text_gallery_select;

        public ViewHolder(View itemView){

            super(itemView);
            imageGallery = (ImageView) itemView.findViewById(R.id.image_gallery);
            text_gallery_select = (TextView) itemView.findViewById(R.id.text_gallery_select);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_gallery, parent, false);
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

                if(!isBlocking()) {
                    if(!item.isBroken()) {
                        int position = holder.getAdapterPosition();
                        if(mLastCheckPosition != position) {
                            if(!mSelectGalleryList.contains(item)) {
                                if(mSelectGalleryList.size() < mMaxCount) {
                                    mGalleryList.get(position).setChecked(true);
                                    mSelectGalleryList.add(item);
                                    mLastCheckPosition = position;
                                    ((PostGalleryActivity) context).addImage(item);
                                    notifyDataSetChanged();
                                } else {
                                    ToastUtil.show(context, context.getString(R.string.format_msg_image_count, mMaxCount));
                                }

                            } else {
                                mLastCheckPosition = position;
                                ((PostGalleryActivity) context).changeImage(mSelectGalleryList.indexOf(item));
                                //                            notifyDataSetChanged();
                            }

                        } else {
                            if(mSelectGalleryList.contains(item)) {
                                ((PostGalleryActivity) context).removeImage(mSelectGalleryList.indexOf(item));
                                mSelectGalleryList.remove(item);
                                mGalleryList.get(position).setChecked(false);
                                if(mSelectGalleryList.size() > 0) {
                                    mLastCheckPosition = mGalleryList.indexOf(mSelectGalleryList.get(mSelectGalleryList.size() - 1));
                                } else {
                                    mLastCheckPosition = -1;
                                }
                                notifyDataSetChanged();
                            }
                        }

                    } else {
                        ToastUtil.show(context, R.string.msg_do_not_attach_broken_image);
                    }
                }



            }
        });

        if(item.getChecked() && !mSelectGalleryList.contains(item)) {
            mSelectGalleryList.add(item);
            mLastCheckPosition = position;
            ((PostGalleryActivity) context).addImage(item);
        }

        if(item.getChecked()) {
            holder.text_gallery_select.setVisibility(View.VISIBLE);
            holder.text_gallery_select.setText(String.valueOf(mSelectGalleryList.indexOf(item) + 1));
        } else {
            holder.text_gallery_select.setVisibility(View.GONE);
            holder.text_gallery_select.setText("");
        }


    }

    @Override
    public int getItemCount(){

        return mGalleryList.size();
    }
}
