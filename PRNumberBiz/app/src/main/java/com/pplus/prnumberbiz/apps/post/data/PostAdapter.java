//package com.pplus.prnumberbiz.apps.post.data;
//
//import android.content.Context;
//import android.content.Intent;
//import android.support.v4.view.ViewPager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.pple.pplus.utils.part.utils.time.DateFormatUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.ui.custom.DirectionIndicator;
//import com.pplus.prnumberbiz.apps.common.ui.custom.GradeBar;
//import com.pplus.prnumberbiz.apps.post.ui.ReplyActivity;
//import com.pplus.prnumberbiz.core.code.common.EnumData;
//import com.pplus.prnumberbiz.core.network.model.dto.Page;
//import com.pplus.prnumberbiz.core.network.model.dto.Post;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//
//
///**
// * Created by 김종경 on 2015-06-17.
// */
//public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder>{
//
//    private Context mContext;
//    private List<Post> mDataList;
//    private OnItemClickListener listener;
//    private static EnumData.PostType mPostType;
//    private int mTodayYear, mTodayMonth, mTodayDay;
//
//
//    public HashMap<Integer, Integer> viewPageStates = new HashMap<>();
//
//    public interface OnItemClickListener{
//
//        void onItemClick(int position);
//    }
//
//    public PostAdapter(Context context, EnumData.PostType postType){
//
//        setHasStableIds(true);
//
//        this.mContext = context;
//        this.mDataList = new ArrayList<>();
//        this.mPostType = postType;
//
//        Calendar c = Calendar.getInstance();
//        mTodayYear = c.get(Calendar.YEAR);
//        mTodayMonth = c.get(Calendar.MONTH);
//        mTodayDay = c.get(Calendar.DAY_OF_MONTH);
//    }
//
//    @Override
//    public long getItemId(int position){
//
//        return mDataList.get(position).getNo();
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener){
//
//        this.listener = listener;
//    }
//
//    public Post getItem(int position){
//
//        return mDataList.get(position);
//    }
//
//    public List<Post> getDataList(){
//
//        return mDataList;
//    }
//
//    public void add(Post data){
//
//        if(mDataList == null) {
//            mDataList = new ArrayList<>();
//        }
//        mDataList.add(data);
//        notifyDataSetChanged();
//    }
//
//    public void addAll(List<Post> dataList){
//
//        if(this.mDataList == null) {
//            this.mDataList = new ArrayList<>();
//        }
//
//        this.mDataList.addAll(dataList);
//        notifyDataSetChanged();
//    }
//
//    public void replaceData(int position, Post data){
//
//        mDataList.remove(position);
//        mDataList.add(position, data);
//        notifyDataSetChanged();
//    }
//
//    public void clear(){
//
//        mDataList = new ArrayList<>();
//        notifyDataSetChanged();
//    }
//
//    public void setDataList(List<Post> dataList){
//
//        this.mDataList = dataList;
//        notifyDataSetChanged();
//    }
//
//    static class ViewHolder extends RecyclerView.ViewHolder{
//
//        private ImageView image_post_profileImage;
//        ViewPager pager_image;
//        View layout_post_profile, layout_image, layout_post_grade, image_post_sns;
//        DirectionIndicator indicator_post;
//        TextView text_post_name, text_title, text_contents, text_regDate, text_comment, text_share;
//        GradeBar gradebar_review_post;
//
//        public ViewHolder(View itemView){
//
//            super(itemView);
//            layout_post_profile = itemView.findViewById(R.id.layout_post_profile);
//            image_post_profileImage = (ImageView) itemView.findViewById(R.id.image_post_profileImage);
//            text_post_name = (TextView) itemView.findViewById(R.id.text_post_name);
//            pager_image = (ViewPager) itemView.findViewById(R.id.pager_post_image);
//            layout_image = itemView.findViewById(R.id.layout_post_image);
//            indicator_post = (DirectionIndicator) itemView.findViewById(R.id.indicator_post);
//            text_title = (TextView) itemView.findViewById(R.id.text_post_title);
//            text_title.setSingleLine();
//            text_contents = (TextView) itemView.findViewById(R.id.text_post_contents);
//            text_regDate = (TextView) itemView.findViewById(R.id.text_post_regDate);
//            text_comment = (TextView) itemView.findViewById(R.id.text_post_comment);
//            text_share = (TextView) itemView.findViewById(R.id.text_post_share);
//            layout_post_grade = itemView.findViewById(R.id.layout_post_grade);
//            image_post_sns = itemView.findViewById(R.id.image_post_sns);
//            image_post_sns.setVisibility(View.GONE);
////            layout_product.setVisibility(View.GONE);
//            gradebar_review_post = itemView.findViewById(R.id.gradebar_review_post);
//            switch (mPostType) {
//                case pr:
//                case sns:
//                    layout_post_profile.setVisibility(View.GONE);
//                    text_share.setVisibility(View.VISIBLE);
//                    itemView.findViewById(R.id.view_post_share_bar).setVisibility(View.VISIBLE);
//                    break;
//                case review:
//                    layout_post_profile.setVisibility(View.VISIBLE);
//                    text_share.setVisibility(View.GONE);
//                    itemView.findViewById(R.id.view_post_share_bar).setVisibility(View.GONE);
//                    break;
//            }
//        }
//    }
//
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
//
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
//        return new ViewHolder(v);
//    }
//
//    @Override
//    public void onViewRecycled(final ViewHolder holder){
//
//        super.onViewRecycled(holder);
//        viewPageStates.put(holder.getAdapterPosition(), holder.pager_image.getCurrentItem());
//
//    }
//
//    @Override
//    public void onBindViewHolder(final ViewHolder holder, int position){
//
//        final Post item = mDataList.get(position);
//        holder.text_title.setText(item.getSubject());
//
//        holder.text_contents.setText("" + item.getContents());
//
//        if(item.getType().equals(EnumData.PostType.sns.name())){
//            holder.image_post_sns.setVisibility(View.VISIBLE);
//        }else {
//            holder.image_post_sns.setVisibility(View.GONE);
//        }
//
//        if(item.getAttachList() != null && item.getAttachList().size() > 0) {
//            holder.text_contents.setMaxLines(2);
//            holder.layout_image.setVisibility(View.VISIBLE);
//            holder.pager_image.setVisibility(View.VISIBLE);
//            PostImagePagerAdapter imageAdapter = new PostImagePagerAdapter(mContext, item.getAttachList());
//            holder.pager_image.setAdapter(imageAdapter);
//            holder.indicator_post.setVisibility(View.VISIBLE);
//            holder.indicator_post.removeAllViews();
//            holder.indicator_post.build(LinearLayout.HORIZONTAL, item.getAttachList().size());
//            holder.pager_image.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
//
//                @Override
//                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
//
//                }
//
//                @Override
//                public void onPageSelected(int position){
//
//                    holder.indicator_post.setCurrentItem(position);
//                }
//
//                @Override
//                public void onPageScrollStateChanged(int state){
//
//                }
//            });
//
//            holder.pager_image.setCurrentItem(viewPageStates.containsKey(position) ? viewPageStates.get(position) : 0);
//            imageAdapter.setListener(new PostImagePagerAdapter.OnItemClickListener(){
//
//                @Override
//                public void onItemClick(int pos){
//
//                    if(listener != null) {
//                        listener.onItemClick(holder.getAdapterPosition());
//                    }
//                }
//            });
//        } else {
//            holder.text_contents.setMaxLines(5);
//            holder.indicator_post.removeAllViews();
//            holder.indicator_post.setVisibility(View.GONE);
//            holder.pager_image.setVisibility(View.GONE);
//            holder.pager_image.setAdapter(null);
//            holder.layout_image.setVisibility(View.GONE);
//        }
//
//        if(item.getAuthor().getProfileImage() != null){
//            Glide.with(mContext).load(item.getAuthor().getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(holder.image_post_profileImage);
//        }
//
//        holder.text_post_name.setText("" + item.getAuthor().getNickname());
//
//
//        holder.text_comment.setText(mContext.getString(R.string.word_reply)+ " " + item.getCommentCount());
////        holder.text_post_price.setPaintFlags(holder.text_post_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//
//        try {
//            Date d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.getRegDate());
//            Calendar c = Calendar.getInstance();
//            c.setTime(d);
//
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//
//            if(mTodayYear == year && mTodayMonth == month && mTodayDay == day) {
//                SimpleDateFormat output = new SimpleDateFormat("a HH:mm");
//                holder.text_regDate.setText(output.format(d));
//            } else {
//                SimpleDateFormat output = new SimpleDateFormat("yyyy.MM.dd");
//                holder.text_regDate.setText(output.format(d));
//            }
//
//        } catch (Exception e) {
//
//        }
//
////        if(mPostType.equals(EnumData.PostType.review)){
////            if(item.getProperties() != null && StringUtils.isNotEmpty(item.getProperties().getStarPoint())){
////                holder.layout_post_grade.setVisibility(View.VISIBLE);
////                holder.gradebar_review_post.build(item.getProperties().getStarPoint());
////            }else{
////                holder.layout_post_grade.setVisibility(View.GONE);
////            }
////        }
//
//        holder.text_comment.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//                Intent intent = new Intent(mContext, ReplyActivity.class);
//                intent.putExtra(Const.POST_NO, item.getNo());
//                mContext.startActivity(intent);
//            }
//        });
//
//        holder.text_share.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//                share(item);
//            }
//        });
//
//        holder.itemView.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//
//                if(listener != null) {
//                    listener.onItemClick(holder.getAdapterPosition());
//                }
//            }
//        });
//    }
//
//    private void share(Post post){
//
//        String subjectText = null;
//        Page page = LoginInfoManager.getInstance().getUser().getPage();
//        subjectText = page.getName();
//
//        String shareText = post.getSubject() + "\n" + mContext.getString(R.string.format_msg_invite_url, "news.php?boardNo=" + post.getNo());
//
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setAction(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        //        intent.putExtra(Intent.EXTRA_SUBJECT, subjectText);
//        intent.putExtra(Intent.EXTRA_TEXT, shareText);
//
//        Intent chooserIntent = Intent.createChooser(intent, mContext.getString(R.string.word_share_post));
//        //        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareIntentList.toArray(new Parcelable[]{}));
//        mContext.startActivity(chooserIntent);
//    }
//
//    @Override
//    public int getItemCount(){
//
//        return mDataList.size();
//    }
//}
