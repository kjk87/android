package com.pplus.prnumberbiz.apps.post.data;

import android.content.Context;
import android.net.Uri;
import androidx.viewpager.widget.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.post.ui.PostImageEditActivity;

import java.util.ArrayList;

/**
 * Created by KIMSOONHO on 2017-02-01.
 */

public class PostImageEditPagerAdapter extends PagerAdapter{

    private final LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<Uri> dataList = new ArrayList<>();
    private SparseArray<View> views;

    public PostImageEditPagerAdapter(final Context context, final ArrayList<Uri> dataList){

        mContext = context;
        this.dataList = dataList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        views = new SparseArray<>();
    }

    public ArrayList<Uri> getDataList(){

        return dataList;
    }

    public void setDataList(ArrayList<Uri> dataList){
        this.dataList = new ArrayList<>();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int postion, Object obj){

        View v = (View) obj;
        if(v instanceof ImageView) {
            ImageView imgView = ((ImageView) v);
            imgView.setImageDrawable(null);
        }
        container.removeView(v);
        v = null;
    }

    @Override
    public int getCount(){

        return dataList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position){

        View view = mInflater.inflate(R.layout.item_post_pager_image, container, false);
        views.put(position, view);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_post_pager_image);

        final Uri data = dataList.get(position);

        Glide.with(mContext).load(data.getPath()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(imageView);
        view.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                ((PostImageEditActivity)mContext).goFilter(position, data);
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object){

        return view == object;
    }

    @Override
    public int getItemPosition(Object object){

        return POSITION_NONE;
    }
}
