package com.pplus.prnumberuser.apps.post.data;

import android.content.Context;
import android.graphics.BitmapFactory;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;
import com.pplus.utils.part.info.DeviceUtil;
import com.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberuser.Const;
import com.pplus.prnumberuser.R;
import com.pplus.prnumberuser.apps.common.ui.common.ZoomImageView;
import com.pplus.prnumberuser.core.network.model.dto.Attachment;
import com.pplus.prnumberuser.core.util.PplusCommonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;

/**
 * Created by KIMSOONHO on 2017-02-01.
 */

public class ZoomablePageAdapter extends PagerAdapter{

    private final LayoutInflater mInflater;
    private Context mContext;
    private List<Attachment> dataList = new ArrayList<>();

    public onSingleTapListener mOnSingleTapListener;

    public void setOnSingleTapListener(onSingleTapListener listener) {
        mOnSingleTapListener = listener;
    }

    public interface onSingleTapListener {
        void onSingleTap();
    }

    public ZoomablePageAdapter(final Context context, final List<Attachment> photoDatList){
        mContext = context;
        dataList = photoDatList;
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

    public Attachment getData(int position){

        return dataList.get(position);
    }

    @Override
    public int getCount(){

        return dataList.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position){

        View view = mInflater.inflate(R.layout.item_zoomable, container, false);

        ZoomImageView imageView = (ZoomImageView) view.findViewById(R.id.imageView);
        ImageView imagePlay = (ImageView)view.findViewById(R.id.image_detail_youtube_play);

        Attachment item = dataList.get(position);

        if(StringUtils.isNotEmpty(item.getTargetType()) && item.getTargetType().equals("youtube")) {
            imagePlay.setVisibility(View.VISIBLE);
            Glide.with(mContext).load("http://img.youtube.com/vi/" + PplusCommonUtil.Companion.getYoutubeVideoId(item.getUrl()) + "/mqdefault.jpg").apply(new RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(imageView);
        } else {
            imagePlay.setVisibility(View.GONE);
            GlideUrl glideUrl = new GlideUrl(Const.API_URL+"attachment/image?id="+item.getId());
            if(item.getWidth() != 0 && item.getHeight()!= 0) {

                Glide.with(mContext).load(glideUrl).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(imageView);
            }
            else {
                Glide.with(mContext).load(glideUrl).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE).fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img).override(DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS, DeviceUtil.DISPLAY.SCREEN_HEIGHT_PIXELS))
                        .into(imageView);
            }
        }

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

    public BitmapFactory.Options getBitmapSize(String fullname){

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(new File(fullname).getAbsolutePath(), options);
        return options;
    }

}
