package com.pplus.prnumberbiz.apps.pages.data;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.core.network.model.dto.Attachment;

import java.util.ArrayList;
import java.util.List;


public class TemplatePagerAdapter extends PagerAdapter{

    private Context context;
    private List<Attachment> mDataList;
    private Attachment mSelectData;
    private SparseArray<View> views;

    public TemplatePagerAdapter(Context context){

        this.context = context;
        mDataList = new ArrayList<>();
        views = new SparseArray<>();
    }

    public void clear(){

        notifyDataSetChanged();
    }

    public int getData(int position){

        return position;
    }

    public List<Attachment> getDataList(){

        return mDataList;
    }

    public void setDataList(List<Attachment> dataList){

        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    public Attachment getSelectData(){

        return mSelectData;
    }

    @Override
    public int getCount(){

        return mDataList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object){

        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){

        View view = (View) object;
        container.removeView(view);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position){

        final View view = LayoutInflater.from(context).inflate(R.layout.item_template, new LinearLayout(context), false);
        views.put(position, view);
        ImageView imageTemplate = (ImageView) view.findViewById(R.id.image_template);
        final View layoutCheck = view.findViewById(R.id.layout_template_check);

        Glide.with(context).load(mDataList.get(position).getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(imageTemplate);

        view.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){
                mSelectData = mDataList.get(position);
                notifyDataSetChanged();
            }
        });

        if(mSelectData != null) {
            if(mSelectData.equals(mDataList.get(position))) {
                layoutCheck.setVisibility(View.VISIBLE);
            } else {
                layoutCheck.setVisibility(View.GONE);
            }
        }

        container.addView(view);
        return view;
    }

    @Override
    public void notifyDataSetChanged(){

        int key = 0;
        for(int i = 0; i < views.size(); i++) {
            key = views.keyAt(i);
            View view = views.get(key);
            if(mSelectData != null) {
                if(mSelectData.equals(mDataList.get(i))) {
                    view.findViewById(R.id.layout_template_check).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.layout_template_check).setVisibility(View.GONE);
                }
            }
        }
        super.notifyDataSetChanged();
    }
}
