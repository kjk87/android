package com.pplus.prnumberuser.apps.page.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pplus.utils.part.info.DeviceUtil;
import com.pplus.prnumberuser.R;
import com.pplus.prnumberuser.apps.common.ui.common.ZoomImageView;
import com.pplus.prnumberuser.core.network.model.dto.PageImage;

import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

/**
 * Created by KIMSOONHO on 2017-02-01.
 */

public class ZoomablePageImagePagerAdapter extends PagerAdapter{

    private final LayoutInflater mInflater;
    private Context mContext;
    private List<PageImage> dataList = new ArrayList<>();

    public onSingleTapListener mOnSingleTapListener;

    public void setOnSingleTapListener(onSingleTapListener listener) {
        mOnSingleTapListener = listener;
    }

    public interface onSingleTapListener {
        void onSingleTap();
    }

    public ZoomablePageImagePagerAdapter(final Context context, final List<PageImage> dataList){
        mContext = context;
        this.dataList = dataList;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

    public PageImage getData(int position){

        return dataList.get(position);
    }

    @Override
    public int getCount(){

        return dataList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position){

        View view = mInflater.inflate(R.layout.item_zoomable, container, false);

        ZoomImageView imageView = view.findViewById(R.id.imageView);
        ImageView imagePlay = view.findViewById(R.id.image_detail_youtube_play);
        imagePlay.setVisibility(View.GONE);
        PageImage item = dataList.get(position);
        Glide.with(mContext).load(item.getImage()).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img).override(DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS, DeviceUtil.DISPLAY.SCREEN_HEIGHT_PIXELS))
                .into(imageView);

        imageView.setSingleTapListener(new ImageViewTouch.OnImageViewTouchSingleTapListener() {
            @Override
            public void onSingleTapConfirmed() {
                if(mOnSingleTapListener != null) {
                    mOnSingleTapListener.onSingleTap();
                }
            }
        });

        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object){

        return view == object;
    }

}
