//package com.pplus.prnumberuser.apps.goods.data;
//
//import android.content.Context;
//import android.graphics.BitmapFactory;
//import androidx.viewpager.widget.PagerAdapter;
//import androidx.viewpager.widget.ViewPager;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//
//import com.pplus.utils.part.info.DeviceUtil;
//import com.pplus.prnumberuser.R;
//import com.pplus.prnumberuser.core.network.model.dto.Attachment;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by KIMSOONHO on 2017-02-01.
// */
//
//public class GoodsPagerAdapter extends PagerAdapter{
//
//    private final LayoutInflater mInflater;
//    private Context mContext;
//    private List<Attachment> dataList = new ArrayList<>();
//    private OnItemClickListener mListener;
//    private int width, height;
//
//    public interface OnItemClickListener{
//
//        void onItemClick(int position);
//    }
//
//    public void setListener(OnItemClickListener mListener){
//
//        this.mListener = mListener;
//
//    }
//
//    public GoodsPagerAdapter(final Context context, List<Attachment> dataList){
//
//        mContext = context;
//        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        this.dataList = new ArrayList<>();
//        this.dataList.addAll(dataList);
//        width = DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS - context.getResources().getDimensionPixelSize(R.dimen.width_144);
//        height = (int) (width * 0.75);
//    }
//
//    public void setDataList(List<Attachment> dataList){
//
//        this.dataList = new ArrayList<>();
//        this.dataList.addAll(dataList);
//        notifyDataSetChanged();
//    }
//
//    @Override
//    public void destroyItem(ViewGroup container, int position, Object object){
//
//        ((ViewPager) container).removeView((View) object);
//    }
//
//    public List<Attachment> getDataList(){
//
//        return dataList;
//    }
//
//    @Override
//    public int getCount(){
//
//        return dataList.size();
//    }
//
//    @Override
//    public Object instantiateItem(ViewGroup container, final int position){
//
//        View view = mInflater.inflate(R.layout.item_goods_image, container, false);
//
//        ImageView imageView = (ImageView) view.findViewById(R.id.image_goods);
//
//        Attachment data = dataList.get(position);
//
////        Glide.with(mContext).load(data.getUrl()).apply(new RequestOptions().fitCenter().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(imageView);
//
//        imageView.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//
//                if(mListener != null) {
//                    mListener.onItemClick(position);
//                }
//            }
//        });
//
//        container.addView(view);
//        return view;
//    }
//
//    @Override
//    public boolean isViewFromObject(View view, Object object){
//
//        return view == object;
//    }
//
//    public BitmapFactory.Options getBitmapSize(String fullname){
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(new File(fullname).getAbsolutePath(), options);
//        return options;
//    }
//}
