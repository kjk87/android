package com.pplus.prnumberuser.apps.common.ui.common.data;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.pplus.prnumberuser.R;

import java.util.List;

public class ViewPagerAdapter extends PagerAdapter{

    private final LayoutInflater mInflater;
    private List<Integer> mImageResource;
    private OnItemClickListener listener;
    private Context mContext;

    public interface OnItemClickListener{

        void onItemClick(int position);
    }

    public ViewPagerAdapter(final Context context, List<Integer> imageResource){

        mImageResource = imageResource;
        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setOnItemClickListener(OnItemClickListener listener){

        this.listener = listener;
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

        return mImageResource.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position){

        ImageView imageView = (ImageView) mInflater.inflate(R.layout.layout_turoial, null, false);

        //        Glide.with(mContext).load(mImageResource.get(position)).into(imageView);

        imageView.setImageResource(mImageResource.get(position));

        if(listener != null) {
            imageView.setClickable(true);
            imageView.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View view){

                    listener.onItemClick(position);
                }
            });
        }

        container.addView(imageView);
        return imageView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object){

        return view == object;
    }
}
