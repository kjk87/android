package com.pplus.prnumberbiz.apps.post.data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pple.pplus.utils.part.utils.time.DateFormatUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseArrayAdapter;
import com.pplus.prnumberbiz.apps.post.ui.PostDetailActivity;
import com.pplus.prnumberbiz.apps.post.ui.ReplyActivity;
import com.pplus.prnumberbiz.apps.post.ui.ReplyDetailActivity;
import com.pplus.prnumberbiz.apps.post.ui.ReplyEditActivity;
import com.pplus.prnumberbiz.core.code.common.EnumData;
import com.pplus.prnumberbiz.core.network.model.dto.Comment;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Map;


/**
 * Created by j2n on 2016. 12. 22..
 */

public class ReplyAdapter extends BaseArrayAdapter<Comment>{

    private Map<Long, ArrayList<Comment>> cCommentList;
    private int mTodayYear, mTodayMonth, mTodayDay;
    private String mPostType;

    public ReplyAdapter(Context context){

        super(context, R.layout.item_reply);
        Calendar c = Calendar.getInstance();
        mTodayYear = c.get(Calendar.YEAR);
        mTodayMonth = c.get(Calendar.MONTH);
        mTodayDay = c.get(Calendar.DAY_OF_MONTH);
    }

    public void setPostType(String postType){

        this.mPostType = postType;
    }

    public void setcCommentList(Map<Long, ArrayList<Comment>> cCommentList){

        this.cCommentList = cCommentList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        ViewHolder holder;

        if(convertView == null) {

            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_reply, parent, false);
            holder = new ViewHolder();
            holder.initialize(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final Comment item = getItem(position);



        if(item.isDeleted()) {
            holder.text_name.setText(R.string.word_unknown);
            holder.text_contents.setText(R.string.msg_delete_comment);
            holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default);
        } else {
            if(item.getAuthor().getNo().equals(LoginInfoManager.getInstance().getUser().getNo())){
                holder.text_name.setText("" + LoginInfoManager.getInstance().getUser().getPage().getName());
                if(LoginInfoManager.getInstance().getUser().getPage().getProfileImage() != null) {
                    Glide.with(getContext()).load(LoginInfoManager.getInstance().getUser().getPage().getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_profileImage);
                }else{
                    holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default);
                }

            }else{
                holder.text_name.setText("" + item.getAuthor().getNickname());
                if(item.getAuthor().getProfileImage() != null) {
                    Glide.with(getContext()).load(item.getAuthor().getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_profileImage);
                }else{
                    holder.image_profileImage.setImageResource(R.drawable.img_post_profile_default);
                }
            }

            holder.text_contents.setText("" + item.getComment());
        }


        if(cCommentList.get(item.getNo()) != null) {
            holder.layout_child.setVisibility(View.VISIBLE);

            int replyCount = cCommentList.get(item.getNo()).size();
            Comment childComment = cCommentList.get(item.getNo()).get(cCommentList.get(item.getNo()).size() - 1);
            if(childComment.getAuthor().getProfileImage() != null) {
                Glide.with(getContext()).load(childComment.getAuthor().getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.img_post_profile_default).error(R.drawable.img_post_profile_default)).into(holder.image_child_profileImage);
            }else{
                holder.image_child_profileImage.setImageResource(R.drawable.img_post_profile_default);
            }

            holder.text_child_name.setText("" + childComment.getAuthor().getNickname());
            holder.text_child_contents.setText("" + childComment.getComment());
            holder.text_child_replyCount.setText(getContext().getString(R.string.format_word_count_reply_of_reply, replyCount));

        } else {
            holder.layout_child.setVisibility(View.GONE);
        }

        try {
            Date d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(item.getRegDate());
            Calendar c = Calendar.getInstance();
            c.setTime(d);

            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            if(mTodayYear == year && mTodayMonth == month && mTodayDay == day) {
                SimpleDateFormat output = new SimpleDateFormat("a HH:mm");
                holder.text_regDate.setText(output.format(d));
            } else {
                SimpleDateFormat output = new SimpleDateFormat("yyyy/MM/dd");
                holder.text_regDate.setText(output.format(d));
            }

        } catch (Exception e) {

        }

        holder.layout_child.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                Intent intent = new Intent(getContext(), ReplyDetailActivity.class);
                intent.putExtra(Const.REPLY, item);

                if(cCommentList.get(item.getNo()) != null) {
                    intent.putParcelableArrayListExtra(Const.REPLY_CHILD, cCommentList.get(item.getNo()));
                }
                if(getContext() instanceof PostDetailActivity) {
                    ((PostDetailActivity) getContext()).startActivityForResult(intent, Const.REQ_REPLY);
                } else if(getContext() instanceof ReplyActivity) {
                    ((ReplyActivity) getContext()).startActivityForResult(intent, Const.REQ_REPLY);
                }

            }
        });

        if(item.getAuthor().getNo().equals(LoginInfoManager.getInstance().getUser().getNo())){
            holder.text_reply.setVisibility(View.GONE);
        }else{
            holder.text_reply.setVisibility(View.VISIBLE);
        }

        holder.text_reply.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){


                Intent intent = new Intent(getContext(), ReplyDetailActivity.class);
                intent.putExtra(Const.REPLY, item);

                if(cCommentList.get(item.getNo()) != null) {
                    intent.putParcelableArrayListExtra(Const.REPLY_CHILD, cCommentList.get(item.getNo()));
                }
                if(getContext() instanceof PostDetailActivity) {
                    ((PostDetailActivity) getContext()).startActivityForResult(intent, Const.REQ_REPLY);
                } else if(getContext() instanceof ReplyActivity) {
                    ((ReplyActivity) getContext()).startActivityForResult(intent, Const.REQ_REPLY);
                }

            }
        });

        holder.layout_reply_parent.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                final AlertBuilder.Builder builder = new AlertBuilder.Builder();
                builder.setLeftText(getContext().getString(R.string.word_cancel));

                final boolean isMe = LoginInfoManager.getInstance().getUser().getNo().equals(item.getAuthor().getNo());

                if(isMe) {
                    builder.setContents(getContext().getString(R.string.word_modified), getContext().getString(R.string.word_delete));
                } else {
                    builder.setContents(getContext().getString(R.string.msg_report));
                }

                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM).setOnAlertResultListener(new OnAlertResultListener(){

                    @Override
                    public void onCancel(){

                    }

                    @Override
                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                        switch (event_alert.getValue()) {
                            case 1:
                                if(isMe) {

                                    Intent intent = new Intent(getContext(), ReplyEditActivity.class);
                                    intent.putExtra(Const.REPLY, item);
                                    if(getContext() instanceof PostDetailActivity) {
                                        ((PostDetailActivity) getContext()).startActivityForResult(intent, Const.REQ_REPLY);
                                    }else if(getContext() instanceof ReplyActivity) {
                                        ((ReplyActivity) getContext()).startActivityForResult(intent, Const.REQ_REPLY);
                                    }

                                } else {
                                    PplusCommonUtil.Companion.report(getContext(), EnumData.REPORT_TYPE.comment, item.getNo());
                                }
                                break;
                            case 2:
                                if(isMe) {

                                    new AlertBuilder.Builder().setContents(getContext().getString(R.string.msg_question_delete_reply)).setLeftText(getContext().getString(R.string.word_cancel)).setRightText(getContext().getString(R.string.word_confirm)).setOnAlertResultListener(new OnAlertResultListener(){

                                        @Override
                                        public void onCancel(){

                                        }

                                        @Override
                                        public void onResult(AlertBuilder.EVENT_ALERT event_alert){

                                            switch (event_alert) {
                                                case RIGHT:
                                                    if(getContext() instanceof PostDetailActivity) {
                                                        ((PostDetailActivity) getContext()).deleteComment(item.getNo());
                                                    }else if(getContext() instanceof ReplyActivity) {
                                                        ((ReplyActivity) getContext()).deleteComment(item.getNo());
                                                    }

                                                    break;
                                            }
                                        }
                                    }).builder().show(getContext());

                                }
                                break;
                        }
                    }
                }).builder().show(getContext());
            }
        });


        return convertView;
    }

    class ViewHolder{

        public ImageView image_profileImage, image_child_profileImage;
        public TextView text_name, text_contents, text_regDate, text_child_name, text_child_contents, text_child_replyCount;
        private View text_reply, layout_reply_parent, layout_child;
        public Context context;

        public void initialize(View itemView){

            context = itemView.getContext();
            layout_reply_parent = itemView.findViewById(R.id.layout_reply_parent);
            image_profileImage = (ImageView) itemView.findViewById(R.id.image_reply_profileImage);
            image_child_profileImage = (ImageView) itemView.findViewById(R.id.image_reply_child_profileImage);
            text_name = (TextView) itemView.findViewById(R.id.text_reply_name);
            text_contents = (TextView) itemView.findViewById(R.id.text_reply_contents);
            text_regDate = (TextView) itemView.findViewById(R.id.text_reply_regDate);
            text_child_name = (TextView) itemView.findViewById(R.id.text_reply_child_name);
            text_child_contents = (TextView) itemView.findViewById(R.id.text_reply_child_contents);
            text_child_replyCount = (TextView) itemView.findViewById(R.id.text_reply_child_replyCount);
            text_reply = itemView.findViewById(R.id.text_reply_reply);
            layout_child = itemView.findViewById(R.id.layout_reply_child);
            layout_child.setVisibility(View.GONE);
        }
    }
}
