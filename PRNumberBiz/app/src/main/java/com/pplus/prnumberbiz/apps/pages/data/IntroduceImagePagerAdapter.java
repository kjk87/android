package com.pplus.prnumberbiz.apps.pages.data;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.request.RequestOptions;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.ui.common.YoutubePlayerActivity;
import com.pplus.prnumberbiz.core.network.model.dto.Attachment;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KIMSOONHO on 2017-02-01.
 */

public class IntroduceImagePagerAdapter extends PagerAdapter{

    private final LayoutInflater mInflater;
    private Context mContext;
    private List<Attachment> dataList = new ArrayList<>();
    private OnItemClickListener mListener;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public void setListener(OnItemClickListener mListener){

        this.mListener = mListener;

    }

    public IntroduceImagePagerAdapter(final Context context){

        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.dataList = new ArrayList<>();

    }

    public void setDataList(List<Attachment> dataList){

        this.dataList = new ArrayList<>();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){

        ((ViewPager) container).removeView((View) object);
    }

    @Override
    public int getCount(){

        return dataList.size();
    }

    public List<Attachment> getDataList(){

        return dataList;
    }

    public Attachment getData(int position){
        return dataList.get(position);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position){

        View view = mInflater.inflate(R.layout.item_introduce_image, container, false);

        ImageView imageView = (ImageView) view.findViewById(R.id.image_introduce);
        ImageView imagePlay = (ImageView)view.findViewById(R.id.image_youtube_play);

        final Attachment data = dataList.get(position);

        imagePlay.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                Intent intent = new Intent(mContext, YoutubePlayerActivity.class);
                intent.putExtra(Const.DATA, PplusCommonUtil.Companion.getYoutubeVideoId(data.getUrl()));
                mContext.startActivity(intent);
            }
        });


        if(StringUtils.isNotEmpty(data.getTargetType()) && data.getTargetType().equals("youtube")) {
            imagePlay.setVisibility(View.VISIBLE);
            Glide.with(mContext).load("http://img.youtube.com/vi/" + PplusCommonUtil.Companion.getYoutubeVideoId(data.getUrl()) + "/mqdefault.jpg").apply(new RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(imageView);
        } else {
            imagePlay.setVisibility(View.GONE);
            GlideUrl glideUrl = new GlideUrl(Const.API_URL+"attachment/image?id="+data.getId());
            Glide.with(mContext).load(glideUrl).apply(new RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(imageView);
        }


        imageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                if(mListener != null) {
                    mListener.onItemClick(position);
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

    @Override
    public int getItemPosition(Object object){

        return POSITION_NONE;
    }
}
