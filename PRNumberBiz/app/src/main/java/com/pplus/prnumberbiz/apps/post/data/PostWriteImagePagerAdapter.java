package com.pplus.prnumberbiz.apps.post.data;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.pple.pplus.utils.part.logs.LogUtil;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
import com.pplus.prnumberbiz.apps.post.ui.PostWriteActivity;
import com.pplus.prnumberbiz.core.network.model.dto.Attachment;

import java.util.ArrayList;
import java.util.List;


public class PostWriteImagePagerAdapter extends PagerAdapter{

    private final LayoutInflater mInflater;
    private Context mContext;
    private ArrayList<String> dataList = new ArrayList<>();
    private SparseArray<Attachment> mAttachList = new SparseArray<>();
    private List<Attachment> mAttachedList = new ArrayList<>();
    private SparseArray<View> views;

    public PostWriteImagePagerAdapter(final Context context){

        mContext = context;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        views = new SparseArray<>();
    }

    public ArrayList<String> getDataList(){

        return dataList;
    }

    public List<Attachment> getAttachedList(){

        return mAttachedList;
    }

    public void setAttachedList(List<Attachment> attachedList){

        this.mAttachedList.addAll(attachedList);
        notifyDataSetChanged();
    }

    public void setDataList(ArrayList<String> dataList){

        this.dataList = new ArrayList<>();
        this.dataList.addAll(dataList);
        notifyDataSetChanged();
    }

    public void addData(String data){

        this.dataList.add(data);
        notifyDataSetChanged();
    }

    public void insertAttach(String url, Attachment attachment){

        mAttachList.put(dataList.indexOf(url), attachment);
    }

    public ArrayList<Attachment> getAttachList(){
        ArrayList<Attachment> arrayList = new ArrayList<Attachment>(mAttachList.size());
        for (int i = 0; i < mAttachList.size(); i++)
            arrayList.add(mAttachList.valueAt(i));
        return arrayList;
    }

    public static <C> List<C> asList(SparseArray<C> sparseArray) {
        if (sparseArray == null) return null;
        List<C> arrayList = new ArrayList<C>(sparseArray.size());
        for (int i = 0; i < sparseArray.size(); i++)
            arrayList.add(sparseArray.valueAt(i));
        return arrayList;
    }

    @Override
    public void destroyItem(ViewGroup container, int postion, Object obj){

        ((ViewPager) container).removeView((View) obj);
    }

    @Override
    public int getCount(){

        return mAttachedList.size() + dataList.size();
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position){

        View view = mInflater.inflate(R.layout.item_post_write_image, container, false);
        views.put(position, view);
        ImageView imageView = (ImageView) view.findViewById(R.id.image_post_write);
        LogUtil.e("attached", "attach size : {}", mAttachedList.size());
        if(position < mAttachedList.size()) {

            Glide.with(mContext).load(mAttachedList.get(position).getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(imageView);
        } else {
            final String data = dataList.get(position - mAttachedList.size());
            LogUtil.e("ImageAdapter", data);
            Glide.with(mContext).load(data).apply(new RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true)).into(imageView);
        }

        imageView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                AlertBuilder.Builder builder = new AlertBuilder.Builder();
                builder.setLeftText(mContext.getString(R.string.word_cancel));
                builder.setContents(mContext.getString(R.string.word_add), mContext.getString(R.string.word_delete));

                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_CENTER).setOnAlertResultListener(new OnAlertResultListener(){

                    @Override
                    public void onCancel(){

                    }

                    @Override
                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                        switch (event_alert.getValue()) {
                            case 1:
                                ((PostWriteActivity) mContext).goPostGallery();
                                break;
                            case 2:
                                if(position < mAttachedList.size()) {
                                    mAttachedList.remove(position);
                                } else {
                                    mAttachList.remove(position - mAttachedList.size());
                                    dataList.remove(position - mAttachedList.size());
                                }
                                notifyDataSetChanged();
                                break;
                        }
                    }
                }).builder().show(mContext);
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
