//package com.pplus.prnumberuser.apps.post.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.pplus.utils.part.utils.StringUtils;
//import com.pplus.utils.part.utils.time.DateFormatUtils;
//import com.pplus.prnumberuser.Const;
//import com.pplus.prnumberuser.R;
//import com.pplus.prnumberuser.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberuser.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberuser.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberuser.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberuser.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberuser.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberuser.apps.post.data.ReplyDetailAdapter;
//import com.pplus.prnumberuser.core.code.common.EnumData;
//import com.pplus.prnumberuser.core.network.ApiBuilder;
//import com.pplus.prnumberuser.core.network.model.dto.Comment;
//import com.pplus.prnumberuser.core.network.model.dto.Post;
//import com.pplus.prnumberuser.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberuser.core.util.PplusCommonUtil;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.Date;
//
//import com.pplus.networks.common.PplusCallback;
//import retrofit2.Call;
//
//public class ReplyDetailActivity extends BaseActivity implements View.OnClickListener, ImplToolbar{
//
//    private Comment mComment;
//    private ArrayList<Comment> mChildList;
//    private ReplyDetailAdapter mAdapter;
//    private EditText edit_reply;
//
//    @Override
//    public String getPID(){
//
//        return null;
//    }
//
//    @Override
//    public int getLayoutView(){
//
//        return R.layout.activity_reply_detail;
//    }
//
//    private int mEditPos = 0;
//    private Post mPost;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        mPost = getIntent().getParcelableExtra(Const.POST);
//        mComment = getIntent().getParcelableExtra(Const.REPLY);
//        mChildList = getIntent().getParcelableArrayListExtra(Const.REPLY_CHILD);
//        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_reply_detail);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mAdapter = new ReplyDetailAdapter(this, mPost);
//        recyclerView.setAdapter(mAdapter);
//
//        if(mChildList != null) {
//            Collections.sort(mChildList, cmpAsc);
//            mAdapter.setDataList(mChildList);
//        }
//
//        mAdapter.setOnItemClickListener(new ReplyDetailAdapter.OnItemClickListener(){
//
//            @Override
//            public void onItemClick(int position){
//
//                mEditPos = position;
//
//                final AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                builder.setLeftText(getString(R.string.word_cancel));
//
//                final boolean isMe = LoginInfoManager.getInstance().getUser().getNo().equals(mAdapter.getItem(mEditPos).getAuthor().getNo());
//
//                if(isMe) {
//                    builder.setContents(getString(R.string.word_modified), getString(R.string.word_delete));
//                } else {
//                    builder.setContents(getString(R.string.msg_report));
//                }
//
//                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM).setOnAlertResultListener(new OnAlertResultListener(){
//
//                    @Override
//                    public void onCancel(){
//
//                    }
//
//                    @Override
//                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                        switch (event_alert.getValue()) {
//                            case 1:
//                                if(isMe) {
//                                    Intent intent = new Intent(ReplyDetailActivity.this, ReplyEditActivity.class);
//                                    intent.putExtra(Const.REPLY, mAdapter.getItem(mEditPos));
//                                    startActivityForResult(intent, Const.REQ_REPLY);
//
//                                } else {
//                                    PplusCommonUtil.Companion.report(ReplyDetailActivity.this, EnumData.REPORT_TYPE.comment, mAdapter.getItem(mEditPos).getNo());
//                                }
//                                break;
//                            case 2:
//                                if(isMe) {
//
//                                    new AlertBuilder.Builder().setContents(getString(R.string.msg_question_delete_reply)).setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm)).setOnAlertResultListener(new OnAlertResultListener(){
//
//                                        @Override
//                                        public void onCancel(){
//
//                                        }
//
//                                        @Override
//                                        public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                                            switch (event_alert) {
//                                                case RIGHT:
//                                                    deleteComment(mAdapter.getItem(mEditPos).getNo());
//                                                    break;
//                                            }
//                                        }
//                                    }).builder().show(ReplyDetailActivity.this);
//
//                                }
//                                break;
//                        }
//                    }
//                }).builder().show(ReplyDetailActivity.this);
//            }
//        });
//
//        edit_reply = (EditText) findViewById(R.id.edit_reply);
//        findViewById(R.id.text_input_reply).setOnClickListener(this);
//
//        ImageView image_profileImage = (ImageView) findViewById(R.id.image_reply_profileImage);
//        TextView text_name = (TextView) findViewById(R.id.text_reply_name);
//        TextView text_contents = (TextView) findViewById(R.id.text_reply_contents);
//        TextView text_regDate = (TextView) findViewById(R.id.text_reply_regDate);
//
//        if(mComment.getAuthor().getUseStatus() != null && mComment.getAuthor().getUseStatus().equals(EnumData.UseStatus.leave.name())){
//            text_name.setText(R.string.word_unknown);
//            image_profileImage.setImageResource(R.drawable.ic_gift_profile_default);
//            text_contents.setText("" + mComment.getComment());
//        }else{
//            if(mComment.isDeleted()) {
//                text_name.setText(R.string.word_unknown);
//                text_contents.setText(R.string.msg_delete_comment);
//                image_profileImage.setImageResource(R.drawable.img_post_profile_default);
//            } else {
//                text_name.setText("" + mComment.getAuthor().getNickname());
//                if(mPost != null && mPost.getType().equals(EnumData.PostType.pr.name()) && mComment.getAuthor().getNo().equals(mPost.getAuthor().getNo()) && mPost.getPage() != null) {
//                    if(StringUtils.isNotEmpty(mPost.getAuthor().getPage().getThumbnail())) {
//                        Glide.with(this).load(mPost.getAuthor().getPage().getThumbnail()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(image_profileImage);
//                    }
//                } else {
//                    if(mComment.getAuthor().getProfileImage() != null) {
//                        Glide.with(this).load(mComment.getAuthor().getProfileImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.ic_gift_profile_default).error(R.drawable.ic_gift_profile_default)).into(image_profileImage);
//                    }
//                }
//
//                text_contents.setText("" + mComment.getComment());
//
//            }
//        }
//
//        try {
//
//            Calendar todayC = Calendar.getInstance();
//            int todayYear = todayC.get(Calendar.YEAR);
//            int todayMonth = todayC.get(Calendar.MONTH);
//            int todayDay = todayC.get(Calendar.DAY_OF_MONTH);
//
//            Date d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(mComment.getRegDate());
//            Calendar c = Calendar.getInstance();
//            c.setTime(d);
//
//            int year = c.get(Calendar.YEAR);
//            int month = c.get(Calendar.MONTH);
//            int day = c.get(Calendar.DAY_OF_MONTH);
//
//            if(todayYear == year && todayMonth == month && todayDay == day) {
//                SimpleDateFormat output = new SimpleDateFormat("a HH:mm");
//                text_regDate.setText(output.format(d));
//            } else {
//                SimpleDateFormat output = new SimpleDateFormat("yyyy/MM/dd");
//                text_regDate.setText(output.format(d));
//            }
//
//        } catch (Exception e) {
//
//        }
//    }
//
//    Comparator<Comment> cmpAsc = new Comparator<Comment>(){
//
//        @Override
//        public int compare(Comment o1, Comment o2){
//
//            Date d1 = null, d2 = null;
//            try {
//                d1 = DateFormatUtils.PPLUS_DATE_FORMAT.parse(o1.getRegDate());
//                d2 = DateFormatUtils.PPLUS_DATE_FORMAT.parse(o2.getRegDate());
//            } catch (Exception e) {
//
//            }
//
//            if(d1.after(d2)) {
//                return -1;
//            } else {
//                return 1;
//            }
//
//        }
//    };
//
//
//    private void deleteComment(Long no){
//
//        showProgress("");
//        ApiBuilder.create().deleteComment(no).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//
//                hideProgress();
//                mAdapter.remove(mEditPos);
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
//
//                hideProgress();
//            }
//        }).build().call();
//    }
//
//    @Override
//    public void onClick(View view){
//
//        switch (view.getId()) {
//            case R.id.text_input_reply:
//                String comment = edit_reply.getText().toString().trim();
//                if(StringUtils.isEmpty(comment)) {
//                    return;
//                }
//
//                showProgress("");
//                Comment params = new Comment();
//                params.setComment(comment);
//                params.setGroup_seq_no(mComment.getGroup_seq_no());
//                Post post = new Post();
//                post.setNo(mComment.getPost().getNo());
//                params.setPost(post);
//                Comment parentComment = new Comment();
//                parentComment.setNo(mComment.getNo());
//                params.setParent(parentComment);
//
//                ApiBuilder.create().insertComment(params).setCallback(new PplusCallback<NewResultResponse<Comment>>(){
//
//                    @Override
//                    public void onResponse(Call<NewResultResponse<Comment>> call, NewResultResponse<Comment> response){
//
//                        hideProgress();
//                        edit_reply.setText("");
//                        mAdapter.add(response.getData());
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<NewResultResponse<Comment>> call, Throwable t, NewResultResponse<Comment> response){
//
//                        hideProgress();
//                    }
//                }).build().call();
//                break;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case Const.REQ_REPLY:
//                    if(data != null) {
//                        Comment comment = data.getParcelableExtra(Const.REPLY);
//                        mAdapter.replaceData(mEditPos, comment);
//                    }
//                    break;
//            }
//
//        }
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_reply_of_reply), ToolbarOption.ToolbarMenu.LEFT);
//        return toolbarOption;
//    }
//
//    @Override
//    public OnToolbarListener getOnToolbarClickListener(){
//
//        return new OnToolbarListener(){
//
//            @Override
//            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag){
//
//                switch (toolbarMenu) {
//                    case LEFT:
//                        if(tag.equals(1)) {
//                            onBackPressed();
//                        }
//                        break;
//                }
//            }
//        };
//    }
//}
