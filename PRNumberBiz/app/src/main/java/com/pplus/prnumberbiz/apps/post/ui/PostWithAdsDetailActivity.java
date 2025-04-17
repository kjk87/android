//package com.pplus.prnumberbiz.apps.post.ui;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v4.view.ViewPager;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.apps.resource.ResourceUtil;
//import com.pple.pplus.utils.part.logs.LogUtil;
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pple.pplus.utils.part.utils.time.DateFormatUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.ads.ui.AdsDetailActivity;
//import com.pplus.prnumberbiz.apps.ads.ui.AdsSettingActivity2;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.common.ui.custom.DirectionIndicator;
//import com.pplus.prnumberbiz.apps.post.data.PostImagePagerAdapter;
//import com.pplus.prnumberbiz.apps.post.data.ReplyAdapter;
//import com.pplus.prnumberbiz.core.code.common.EnumData;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.Advertise;
//import com.pplus.prnumberbiz.core.network.model.dto.Attachment;
//import com.pplus.prnumberbiz.core.network.model.dto.Comment;
//import com.pplus.prnumberbiz.core.network.model.dto.Post;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class PostWithAdsDetailActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{
//
//    @Override
//    public String getPID(){
//
//        return null;
//    }
//
//    @Override
//    public String getSID(){
//
//        return null;
//    }
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_post_detail;
//    }
//
//    private Long mNo;
//    private Post mPost;
//    private List<Attachment> attachList;
//    private TextView text_contents, text_comment;
//    private ViewPager pager_image;
//    private PostImagePagerAdapter mPostImagePagerAdapter;
//    private DirectionIndicator indicator_post;
//    private ReplyAdapter mReplyAdapter;
//    private EditText edit_reply;
//    private TextView text_regDate, text_ads_total_count, text_ads_description, text_ads_status;
//    private View layout_advertise, layout_status_rate, view_ads_visit_rate, image_share, layout_more;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        mNo = getIntent().getLongExtra(Const.POST_NO, -1);
//
//        ListView list = (ListView) findViewById(R.id.list_post_detail);
//        mReplyAdapter = new ReplyAdapter(this);
//        list.setAdapter(mReplyAdapter);
//
//        View headerView = getLayoutInflater().inflate(R.layout.item_my_post, null);
//        headerView.setPadding(0, getResources().getDimensionPixelSize(R.dimen.height_60), 0, 0);
//
//        text_contents = (TextView) headerView.findViewById(R.id.text_my_post_contents);
//        pager_image = (ViewPager) headerView.findViewById(R.id.pager_my_post_image);
//        indicator_post = (DirectionIndicator) headerView.findViewById(R.id.indicator_my_post);
//        text_comment = (TextView) headerView.findViewById(R.id.text_my_post_comment);
//        text_comment.setOnClickListener(this);
//        text_regDate = (TextView) headerView.findViewById(R.id.text_my_post_regDate);
//        layout_advertise = headerView.findViewById(R.id.layout_my_post_advertise);
//        text_ads_total_count = (TextView) headerView.findViewById(R.id.text_my_post_ads_total_count);
//        text_ads_description = (TextView) headerView.findViewById(R.id.text_my_post_ads_description);
//        layout_status_rate = headerView.findViewById(R.id.layout_my_post_ads_status_rate);
//        view_ads_visit_rate = headerView.findViewById(R.id.view_my_post_ads_visit_rate);
//        text_ads_status = (TextView) headerView.findViewById(R.id.text_my_post_ads_status);
//        image_share = headerView.findViewById(R.id.layout_my_post_share);
//        layout_more = headerView.findViewById(R.id.image_my_post_more);
//        image_share.setOnClickListener(this);
//        layout_more.setOnClickListener(this);
//
//        list.addHeaderView(headerView);
//
//        edit_reply = (EditText) findViewById(R.id.edit_reply);
//        findViewById(R.id.text_input_reply).setOnClickListener(this);
//
//        getPost();
//
//    }
//
//    private void getPost(){
//
//        showProgress("");
//        Map<String, String> params = new HashMap<>();
//        params.put("no", "" + mNo);
//        ApiBuilder.create().getPostWithAdvertise(params).setCallback(new PplusCallback<NewResultResponse<Post>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Post>> call, NewResultResponse<Post> response){
//
//                hideProgress();
//                Post post = response.getData();
//                if(post != null) {
//                    mPost = post;
//
//                    try {
//                        Calendar todayC = Calendar.getInstance();
//                        int todayYear = todayC.get(Calendar.YEAR);
//                        int todayMonth = todayC.get(Calendar.MONTH);
//                        int todayDay = todayC.get(Calendar.DAY_OF_MONTH);
//
//                        Date d = DateFormatUtils.PPLUS_DATE_FORMAT.parse(post.getRegDate());
//                        Calendar c = Calendar.getInstance();
//                        c.setTime(d);
//
//                        int year = c.get(Calendar.YEAR);
//                        int month = c.get(Calendar.MONTH);
//                        int day = c.get(Calendar.DAY_OF_MONTH);
//
//                        if(todayYear == year && todayMonth == month && todayDay == day) {
//                            SimpleDateFormat output = new SimpleDateFormat("a HH:mm");
//                            text_regDate.setText(output.format(d));
//                        } else {
//                            SimpleDateFormat output = new SimpleDateFormat("yyyy/MM/dd");
//                            text_regDate.setText(output.format(d));
//                        }
//
//                    } catch (Exception e) {
//
//                    }
//
//                    text_contents.setText("" + post.getContents());
//
//                    attachList = post.getAttachList();
//                    if(attachList == null) {
//                        attachList = new ArrayList<>();
//
//                    }
//
//                    text_comment.setText(getString(R.string.word_reply) + " " + post.getCommentCount());
//                    mPostImagePagerAdapter = new PostImagePagerAdapter(PostWithAdsDetailActivity.this, attachList);
//                    pager_image.setAdapter(mPostImagePagerAdapter);
//                    indicator_post.removeAllViews();
//                    indicator_post.build(LinearLayout.HORIZONTAL, attachList.size());
//
//                    mPostImagePagerAdapter.setListener(new PostImagePagerAdapter.OnItemClickListener(){
//
//                        @Override
//                        public void onItemClick(int position){
//
//                            Intent intent = new Intent(PostWithAdsDetailActivity.this, PhotoDetailViewerActivity.class);
//                            intent.putExtra(Const.POSITION, pager_image.getCurrentItem());
//                            intent.putParcelableArrayListExtra(Const.DATA, (ArrayList<Attachment>) mPostImagePagerAdapter.getDataList());
//                            startActivity(intent);
//                        }
//                    });
//                    pager_image.addOnPageChangeListener(new ViewPager.OnPageChangeListener(){
//
//                        @Override
//                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels){
//
//                        }
//
//                        @Override
//                        public void onPageSelected(int position){
//
//                            indicator_post.setCurrentItem(position);
//                        }
//
//                        @Override
//                        public void onPageScrollStateChanged(int state){
//
//                        }
//                    });
//
//                    Advertise advertise = mPost.getLastAdvertise();
//
//                    if(advertise != null) {
//                        text_ads_total_count.setText(getString(R.string.format_first_come, advertise.getTotalCount()));
//
//                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view_ads_visit_rate.getLayoutParams();
//
//                        float contactRate = (float) advertise.getCurrentCount() / (float) advertise.getTotalCount();
//
//                        layoutParams.weight = contactRate;
//
//                        EnumData.AdsStatus status = EnumData.AdsStatus.valueOf(advertise.getStatus());
//                        switch (status) {
//
//                            case ing:
//                                layout_status_rate.setVisibility(View.VISIBLE);
//                                text_ads_description.setText(getString(R.string.format_msg_ing_ads, advertise.getCurrentCount()));
//                                view_ads_visit_rate.setBackgroundColor(ResourceUtil.getColor(PostWithAdsDetailActivity.this, R.color.color_8700ff));
//                                text_ads_status.setText(getString(R.string.word_ads_history));
//                                break;
//                            case ready:
//                                layout_status_rate.setVisibility(View.GONE);
//                                text_ads_description.setText(getString(R.string.msg_ready_ads));
//                                text_ads_status.setText(getString(R.string.word_ready_ads));
//                                break;
//                            case stop:
//                                layout_status_rate.setVisibility(View.VISIBLE);
//                                text_ads_description.setText(getString(R.string.format_msg_stop_ads, advertise.getCurrentCount()));
//                                view_ads_visit_rate.setBackgroundColor(ResourceUtil.getColor(PostWithAdsDetailActivity.this, R.color.color_ff4646));
//                                text_ads_status.setText(getString(R.string.word_ads_history));
//                                break;
//                            case finish:
//                                layout_status_rate.setVisibility(View.VISIBLE);
//                                text_ads_description.setText(getString(R.string.format_msg_finish_ads, advertise.getCurrentCount()));
//                                view_ads_visit_rate.setBackgroundColor(ResourceUtil.getColor(PostWithAdsDetailActivity.this, R.color.color_8700ff));
//                                text_ads_status.setText(getString(R.string.word_ads_result));
//                                break;
//                            case complete:
//                                layout_status_rate.setVisibility(View.VISIBLE);
//                                text_ads_description.setText(getString(R.string.format_msg_finish_ads, advertise.getCurrentCount()));
//                                view_ads_visit_rate.setBackgroundColor(ResourceUtil.getColor(PostWithAdsDetailActivity.this, R.color.color_8700ff));
//                                text_ads_status.setText(getString(R.string.word_ads_result));
//                                break;
//                        }
//
//                        text_ads_status.setOnClickListener(new View.OnClickListener(){
//
//                            @Override
//                            public void onClick(View v){
//
//                                Intent intent = new Intent(PostWithAdsDetailActivity.this, AdsDetailActivity.class);
//                                intent.putExtra(Const.KEY, EnumData.AdsType.article);
//                                intent.putExtra(Const.POST, mPost);
//                                startActivityForResult(intent, Const.REQ_POST);
//                            }
//                        });
//
//
//                    } else {
//                        layout_status_rate.setVisibility(View.GONE);
//                        text_ads_description.setVisibility(View.GONE);
//                        text_ads_status.setText(getString(R.string.msg_do_ads));
//                        text_ads_status.setOnClickListener(new View.OnClickListener(){
//
//                            @Override
//                            public void onClick(View v){
//
//                                Intent intent = new Intent(PostWithAdsDetailActivity.this, AdsSettingActivity2.class);
//                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                                intent.putExtra(Const.KEY, EnumData.AdsType.article);
//                                intent.putExtra(Const.POST, mPost);
//                                startActivityForResult(intent, Const.REQ_ADS_SETTING);
//                            }
//                        });
//                    }
//                    getComment();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Post>> call, Throwable t, NewResultResponse<Post> response){
//
//                hideProgress();
//            }
//        }).build().call();
//    }
//
//    private void getComment(){
//
//        showProgress("");
//        ApiBuilder.create().getCommentAll(mNo).setCallback(new PplusCallback<NewResultResponse<Comment>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Comment>> call, NewResultResponse<Comment> response){
//
//                hideProgress();
//                if(response.getDatas() != null) {
//
//                    List<Comment> commentList = response.getDatas();
//                    List<Comment> pCommentList = new ArrayList<>();
//                    Map<Long, ArrayList<Comment>> cCommentList = new HashMap<Long, ArrayList<Comment>>();
//                    for(Comment comment : commentList) {
//                        Post post = new Post();
//                        post.setNo(mPost.getNo());
//                        comment.setPost(post);
//                        if(comment.getDepth() == 1) {
//                            pCommentList.add(comment);
//                        } else {
//                            LogUtil.e(LOG_TAG, "comment : {}", comment.toString());
//                            if(cCommentList.get(comment.getParent().getNo()) == null) {
//                                ArrayList<Comment> childList = new ArrayList<>();
//                                childList.add(comment);
//                                cCommentList.put(comment.getParent().getNo(), childList);
//                            } else {
//                                cCommentList.get(comment.getParent().getNo()).add(comment);
//                            }
//                        }
//                    }
//
//                    mReplyAdapter.setPostType(mPost.getType());
//                    mReplyAdapter.clear();
//                    mReplyAdapter.setcCommentList(cCommentList);
//                    mReplyAdapter.addAll(pCommentList);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Comment>> call, Throwable t, NewResultResponse<Comment> response){
//
//                hideProgress();
//            }
//        }).build().call();
//    }
//
//    public void deleteComment(Long no){
//
//        showProgress("");
//        ApiBuilder.create().deleteComment(no).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//
//                hideProgress();
//                getComment();
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
//    private void deletePost(Long no){
//
//        showProgress("");
//        ApiBuilder.create().deletePost(no).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//
//                hideProgress();
//                setResult(RESULT_OK);
//                finish();
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Object>> call, Throwable t, NewResultResponse<Object> response){
//
//                hideProgress();
//                if(response.getResultCode() == 513) {
//                    showAlert(R.string.msg_can_not_delete_post_advertise);
//                } else {
//                    showAlert(R.string.msg_can_not_delete_post);
//                }
//            }
//        }).build().call();
//    }
//
//    private void share(){
//
//        String shareText = mPost.getSubject() + "\n" + getString(R.string.format_msg_invite_url, "pr_detail.php?prDetailNo=" + mPost.getNo());
//
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setAction(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        //        intent.putExtra(Intent.EXTRA_SUBJECT, subjectText);
//        intent.putExtra(Intent.EXTRA_TEXT, shareText);
//
//        Intent chooserIntent = Intent.createChooser(intent, getString(R.string.word_share_post));
//        //        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareIntentList.toArray(new Parcelable[]{}));
//        startActivity(chooserIntent);
//    }
//
//    @Override
//    public void onClick(View view){
//
//        switch (view.getId()) {
//            case R.id.text_post_detail_comment:
//                Intent intent = new Intent(this, ReplyActivity.class);
//                intent.putExtra(Const.POST_NO, mNo);
//                startActivityForResult(intent, Const.REQ_REPLY);
//                break;
//            case R.id.layout_my_post_share:
//                share();
//                break;
//            case R.id.image_my_post_more:
//                postAlert();
//                break;
//            case R.id.text_input_reply:
//
//                String comment = edit_reply.getText().toString().trim();
//                if(StringUtils.isEmpty(comment)) {
//                    return;
//                }
//
//                showProgress("");
//                Comment params = new Comment();
//                params.setComment(comment);
//                Post post = new Post();
//                post.setNo(mNo);
//                params.setPost(post);
//
//                ApiBuilder.create().insertComment(params).setCallback(new PplusCallback<NewResultResponse<Comment>>(){
//
//                    @Override
//                    public void onResponse(Call<NewResultResponse<Comment>> call, NewResultResponse<Comment> response){
//
//                        hideProgress();
//                        edit_reply.setText("");
//                        getComment();
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
//        switch (requestCode) {
//            case Const.REQ_POST:
//                if(resultCode == RESULT_OK) {
//                    getPost();
//                    setResult(RESULT_OK);
//                }
//
//                break;
//            case Const.REQ_REPLY:
//                getComment();
//                break;
//        }
//        if(requestCode == Const.REQ_REPLY) {
//            getComment();
//        }
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_post_detail), ToolbarOption.ToolbarMenu.LEFT);
//
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
//
//    private void postAlert(){
//
//        if(mPost == null) {
//            return;
//        }
//        AlertBuilder.Builder builder = new AlertBuilder.Builder();
//        builder.setLeftText(getString(R.string.word_cancel));
//
//        builder.setContents(getString(R.string.word_share), getString(R.string.word_modified), getString(R.string.word_delete));
//
//        builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM).setOnAlertResultListener(new OnAlertResultListener(){
//
//            @Override
//            public void onCancel(){
//
//            }
//
//            @Override
//            public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                switch (event_alert.getValue()) {
//                    case 1:
//                        share();
//
//                        break;
//                    case 2:
//                        Intent intent = new Intent(PostWithAdsDetailActivity.this, PostWriteActivity.class);
//                        intent.putExtra(Const.MODE, EnumData.MODE.UPDATE);
//                        intent.putExtra(Const.POST, mPost);
//                        startActivityForResult(intent, Const.REQ_POST);
//                        break;
//                    case 3:
//                        new AlertBuilder.Builder().setContents(getString(R.string.msg_question_delete_post)).setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm)).setOnAlertResultListener(new OnAlertResultListener(){
//
//                            @Override
//                            public void onCancel(){
//
//                            }
//
//                            @Override
//                            public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                                switch (event_alert) {
//                                    case RIGHT:
//                                        deletePost(mPost.getNo());
//                                        break;
//                                }
//                            }
//                        }).builder().show(PostWithAdsDetailActivity.this);
//                        break;
//                }
//            }
//        }).builder().show(PostWithAdsDetailActivity.this);
//    }
//}
