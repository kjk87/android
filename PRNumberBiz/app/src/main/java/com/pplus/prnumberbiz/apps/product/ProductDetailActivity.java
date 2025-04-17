//package com.pplus.prnumberbiz.apps.product;
//
//import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v4.view.ViewPager;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.pple.pplus.utils.part.apps.resource.ResourceUtil;
//import com.pple.pplus.utils.part.format.FormatUtil;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.common.ui.custom.DirectionIndicator;
//import com.pplus.prnumberbiz.apps.post.data.PostImagePagerAdapter;
//import com.pplus.prnumberbiz.core.code.common.EnumData;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.Attachment;
//import com.pplus.prnumberbiz.core.network.model.dto.Post;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class ProductDetailActivity extends BaseActivity implements ImplToolbar{
//
//    @Override
//    public String getPID(){
//
//        return null;
//    }
//
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_product_detail;
//    }
//
//    private TextView text_name, text_description, text_price;
//    private ViewPager pager_image;
//    private DirectionIndicator indicator_detail;
//    private View layout_unsale;
//    private List<Attachment> attachList;
//    private PostImagePagerAdapter mPagerAdapter;
//
//    private Long mNo;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        mNo = getIntent().getLongExtra(Const.POST_NO, -1);
//
//        text_name = (TextView) findViewById(R.id.text_product_detail_name);
//        text_description = (TextView) findViewById(R.id.text_product_detail_description);
//        text_price = (TextView)findViewById(R.id.text_product_detail_price);
//        pager_image = (ViewPager) findViewById(R.id.pager_product_detail_image);
//        indicator_detail = (DirectionIndicator) findViewById(R.id.indicator_product_detail);
//
//        text_price.setText(FormatUtil.getMoneyType("100000"));
//
//        layout_unsale = findViewById(R.id.layout_product_detail_unsale);
//        layout_unsale.setVisibility(View.GONE);
//
//        getPost();
//    }
//
//    private void getPost(){
//
//        showProgress("");
//        ApiBuilder.create().getPost(mNo).setCallback(new PplusCallback<NewResultResponse<Post>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Post>> call, NewResultResponse<Post> response){
//
//                hideProgress();
//                Post post = response.getData();
//                if(post != null) {
//
//                    text_name.setText("" + post.getSubject());
//
//                    attachList = post.getAttachList();
//                    if(attachList == null) {
//                        attachList = new ArrayList<>();
//                    }
//
//                    mPagerAdapter = new PostImagePagerAdapter(ProductDetailActivity.this, attachList);
//                    pager_image.setAdapter(mPagerAdapter);
//                    indicator_detail.build(LinearLayout.HORIZONTAL, attachList.size());
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
//                            indicator_detail.setCurrentItem(position);
//                        }
//
//                        @Override
//                        public void onPageScrollStateChanged(int state){
//
//                        }
//                    });
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
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_product_detail_info), ToolbarOption.ToolbarMenu.LEFT);
//
//        View view = getLayoutInflater().inflate(R.layout.item_top_right, null);
//        TextView textView = (TextView) view.findViewById(R.id.text_top_right);
//        ((RelativeLayout.LayoutParams) textView.getLayoutParams()).rightMargin = getResources().getDimensionPixelSize(R.dimen.width_0);
//        textView.setText(R.string.word_un_sale);
//        textView.setTextColor(ResourceUtil.getColor(this, R.color.black));
//        textView.setBackgroundResource(R.drawable.border_color_7f7f7f_1px);
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, view, 0);
//
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, R.drawable.ic_top_more);
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
//                    case RIGHT:
//                        if(tag.equals(1)) {
//                            if(layout_unsale.getVisibility() == View.VISIBLE){
//                                sale();
//                            }else{
//                                unSale();
//                            }
//
//                        } else if(tag.equals(2)) {
//                            detailAlert();
//                        }
//                        break;
//                }
//            }
//        };
//    }
//
//    private void unSale(){
//
//        AlertBuilder.Builder builder = new AlertBuilder.Builder();
//        builder.setTitle(getString(R.string.word_notice_alert));
//        builder.addContents(new AlertData.MessageData(getString(R.string.msg_unsale_description1), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
//        builder.addContents(new AlertData.MessageData(getString(R.string.msg_unsale_description2), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
//        builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//            @Override
//            public void onCancel(){
//
//            }
//
//            @Override
//            public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//                switch (event_alert){
//                    case RIGHT:
//                        layout_unsale.setVisibility(View.VISIBLE);
//                        break;
//                }
//            }
//        }).builder().show(this);
//    }
//
//    private void sale(){
//
//        AlertBuilder.Builder builder = new AlertBuilder.Builder();
//        builder.setTitle(getString(R.string.word_notice_alert));
//        builder.addContents(new AlertData.MessageData(getString(R.string.msg_sale_description), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
//        builder.addContents(new AlertData.MessageData(getString(R.string.msg_unsale_description2), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//        builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
//        builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//            @Override
//            public void onCancel(){
//
//            }
//
//            @Override
//            public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//                switch (event_alert){
//                    case RIGHT:
//                        layout_unsale.setVisibility(View.GONE);
//                        break;
//                }
//            }
//        }).builder().show(this);
//    }
//
//    private void detailAlert(){
//        AlertBuilder.Builder builder = new AlertBuilder.Builder();
//        builder.setLeftText(getString(R.string.word_cancel));
//        builder.setContents(getString(R.string.word_modified), getString(R.string.word_delete));
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
//                        Intent intent = new Intent(ProductDetailActivity.this, ProductRegActivity.class);
//                        intent.putExtra(Const.MODE, EnumData.MODE.UPDATE);
////                        intent.putExtra(Const.POST, mPost);
//                        startActivityForResult(intent, Const.REQ_POST);
//                        break;
//                    case 2:
//                        new AlertBuilder.Builder().setContents(getString(R.string.msg_question_delete_reply)).setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm)).setOnAlertResultListener(new OnAlertResultListener(){
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
////                                        deletePost(mPost.getNo());
//                                        break;
//                                }
//                            }
//                        }).builder().show(ProductDetailActivity.this);
//                        break;
//                }
//            }
//        }).builder().show(ProductDetailActivity.this);
//    }
//}
