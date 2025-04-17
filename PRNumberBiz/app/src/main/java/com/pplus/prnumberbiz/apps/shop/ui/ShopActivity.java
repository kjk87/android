//package com.pplus.prnumberbiz.apps.shop.ui;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.bumptech.glide.request.RequestOptions;
//import com.pple.pplus.utils.part.apps.permission.Permission;
//import com.pple.pplus.utils.part.apps.permission.PermissionListener;
//import com.pple.pplus.utils.part.logs.LogUtil;
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.builder.PPlusPermission;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.note.ui.NoteContainerActivity;
//import com.pplus.prnumberbiz.apps.pages.ui.PhotoTakerActivity;
//import com.pplus.prnumberbiz.apps.pages.ui.TemplateActivity;
//import com.pplus.prnumberbiz.apps.post.ui.ReviewActivity;
//import com.pplus.prnumberbiz.core.code.common.SnsTypeCode;
//import com.pplus.prnumberbiz.core.code.custom.AttachmentTargetTypeCode;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.Attachment;
//import com.pplus.prnumberbiz.core.network.model.dto.ImgUrl;
//import com.pplus.prnumberbiz.core.network.model.dto.Page;
//import com.pplus.prnumberbiz.core.network.model.dto.Sns;
//import com.pplus.prnumberbiz.core.network.model.request.params.ParamsAttachment;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberbiz.core.network.upload.DefaultUpload;
//import com.pplus.prnumberbiz.core.network.upload.PplusUploadListener;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//import com.pplus.prnumberbiz.core.util.PplusNumberUtil;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//import static com.kakao.kakaostory.StringSet.url;
//
//public class ShopActivity extends BaseActivity implements View.OnClickListener{
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
//        return R.layout.activity_shop;
//    }
//
//    private Page mPage;
//
//    private View image_facebook, image_kakao, image_twitter, image_instagram;
//    private TextView text_catchphrase, text_shop_review;
//    private ImageView image_bg;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        mPage = LoginInfoManager.getInstance().getUser().getPage();
//
//        findViewById(R.id.image_shop_back).setOnClickListener(this);
//        findViewById(R.id.text_shop_share).setOnClickListener(this);
//        TextView text_name = (TextView) findViewById(R.id.image_shop_name);
//        TextView text_number = (TextView) findViewById(R.id.image_shop_number);
//        image_bg = (ImageView) findViewById(R.id.image_shop_background);
////        TextView text_rating = (TextView) findViewById(R.id.text_shop_main_rating);
//        findViewById(R.id.text_shop_introduce).setOnClickListener(tabClickListener);
//        findViewById(R.id.text_shop_promotion).setOnClickListener(tabClickListener);
//        findViewById(R.id.text_shop_product).setOnClickListener(tabClickListener);
//        findViewById(R.id.text_shop_product).setVisibility(View.GONE);
//        findViewById(R.id.text_shop_coupon).setOnClickListener(tabClickListener);
//
//        text_catchphrase = (TextView)findViewById(R.id.text_shop_catchphrase);
//        text_catchphrase.setText(mPage.getCatchphrase());
//
//        image_facebook = findViewById(R.id.image_shop_facebook);
//        image_kakao = findViewById(R.id.image_shop_kakao);
//        image_twitter = findViewById(R.id.image_shop_twitter);
//        image_instagram = findViewById(R.id.image_shop_instagram);
//        image_facebook.setOnClickListener(onSnsClickListener);
//        image_kakao.setOnClickListener(onSnsClickListener);
//        image_twitter.setOnClickListener(onSnsClickListener);
//        image_instagram.setOnClickListener(onSnsClickListener);
//
//        text_name.setText(mPage.getName());
//        text_number.setText(PplusNumberUtil.getPrNumberFormat(mPage.getNumberList().get(0).getNumber()));
//        if(mPage.getBackgroundImage() != null && StringUtils.isNotEmpty(mPage.getBackgroundImage().getUrl())) {
//            Glide.with(this).load(mPage.getBackgroundImage().getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_bg);
//        }else{
//            image_bg.setImageResource(R.drawable.img_main_default);
//        }
//
//        View image_shop_call = findViewById(R.id.image_shop_call);
//        if(StringUtils.isNotEmpty(mPage.getPhone())){
//            image_shop_call.setVisibility(View.VISIBLE);
//        }else{
//            image_shop_call.setVisibility(View.GONE);
//        }
//
//        image_shop_call.setOnClickListener(this);
//
//        text_shop_review = (TextView)findViewById(R.id.text_shop_review);
//        text_shop_review.setOnClickListener(this);
//        findViewById(R.id.text_shop_note).setOnClickListener(this);
//        findViewById(R.id.image_shop_camera).setOnClickListener(this);
//
////        if(mPage.getValuationCount() != null && mPage.getValuationPoint() != null){
////
////            Float grade = (float)mPage.getValuationPoint()/(float)mPage.getValuationCount();
////            if(!grade.isNaN()){
////                text_rating.setText(String.format("%.1f", grade));
////            }else{
////                text_rating.setText("0.0");
////            }
////        }else{
////            text_rating.setText("0.0");
////        }
//
//        getSnsLink();
//        getReviewCount();
//    }
//
//    private void getReviewCount(){
//        Map<String, String> params = new HashMap<>();
//        params.put("boardNo", "" + mPage.getReviewBoard().getNo());
//        ApiBuilder.create().getPostCount(params).setCallback(new PplusCallback<NewResultResponse<Integer>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Integer>> call, NewResultResponse<Integer> response){
//                if(response.getData() > 0){
//                    text_shop_review.setText(getString(R.string.word_review) + " " + response.getData());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Integer>> call, Throwable t, NewResultResponse<Integer> response){
//
//            }
//        }).build().call();
//    }
//
//    private void getSnsLink(){
//
//        Map<String, String> params = new HashMap<>();
//        params.put("no", "" + mPage.getNo());
//
//        ApiBuilder.create().getSnsLinkAll(params).setCallback(new PplusCallback<NewResultResponse<Sns>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Sns>> call, NewResultResponse<Sns> response){
//
//                initSNS(response.getDatas());
//            }
//
//            @Override
//            public void onFailure(Call<NewResultResponse<Sns>> call, Throwable t, NewResultResponse<Sns> response){
//
//            }
//        }).build().call();
//    }
//
//    private void initSNS(List<Sns> snsList){
//
//        if(snsList != null && !snsList.isEmpty()) {
//            for(Sns sns : snsList) {
//                setSns(sns);
//            }
//        }
//    }
//
//    private void setSns(Sns sns){
//
//        if(sns != null) {
//            if(StringUtils.isEmpty(sns.getUrl())) {
//                switch (SnsTypeCode.valueOf(sns.getType())) {
//
//                    case twitter:
//                        image_twitter.setVisibility(View.GONE);
//                        break;
//                    case facebook:
//                        image_facebook.setVisibility(View.GONE);
//                        break;
//                    case instagram:
//                        image_instagram.setVisibility(View.GONE);
//                        break;
//                    case kakao:
//                        image_kakao.setVisibility(View.GONE);
//                        break;
//
//                }
//            } else {
//                switch (SnsTypeCode.valueOf(sns.getType())) {
//
//                    case twitter:
//                        image_twitter.setVisibility(View.VISIBLE);
//                        image_twitter.setTag(sns);
//                        break;
//                    case facebook:
//                        image_facebook.setVisibility(View.VISIBLE);
//                        image_facebook.setTag(sns);
//                        break;
//                    case instagram:
//                        image_instagram.setVisibility(View.VISIBLE);
//                        image_instagram.setTag(sns);
//                        break;
//                    case kakao:
//                        image_kakao.setVisibility(View.VISIBLE);
//                        image_kakao.setTag(sns);
//                        break;
//                }
//            }
//        }
//    }
//
//    private View.OnClickListener onSnsClickListener = new View.OnClickListener(){
//
//        @Override
//        public void onClick(View v){
//
//            Sns sns = (Sns) v.getTag();
//            snsEvent(sns);
//        }
//    };
//
//    private View.OnClickListener tabClickListener = new View.OnClickListener(){
//
//        @Override
//        public void onClick(View view){
//
//            Intent intent = new Intent(ShopActivity.this, ShopDetailActivity.class);
//
//            switch (view.getId()) {
//                case R.id.text_shop_introduce:
//                    intent.putExtra(Const.TAB, 0);
//                    break;
//                case R.id.text_shop_promotion:
//                    intent.putExtra(Const.TAB, 1);
//                    break;
////                case R.id.text_shop_product:
////                    intent.putExtra(Const.TAB, 2);
////                    break;
//                case R.id.text_shop_coupon:
//                    intent.putExtra(Const.TAB, 2);
//                    break;
//            }
//
//            startActivity(intent);
//            overridePendingTransition(R.anim.bottom_up, R.anim.hold);
//        }
//    };
//
//    private void snsEvent(Sns sns){
//
//        // SNS 페이지 이동
//        if(StringUtils.isNotEmpty(sns.getUrl())) {
//            // 계정으로 이동
//            startActivity(PplusCommonUtil.getOpenSnsIntent(this, SnsTypeCode.valueOf(sns.getType()), sns.getUrl(), sns.isLinkage()));
//        }
//
//    }
//
//    @Override
//    public void onClick(View view){
//
//        switch (view.getId()) {
//            case R.id.image_shop_back:
//                onBackPressed();
//                break;
//            case R.id.image_shop_call:
//                PPlusPermission pPlusPermission = new PPlusPermission(this);
//                pPlusPermission.addPermission(Permission.PERMISSION_KEY.CONTACTS);
//                pPlusPermission.setPermissionListener(new PermissionListener(){
//
//                    @Override
//                    public void onPermissionGranted(){
//
//                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mPage.getPhone()));
//                        startActivity(intent);
//                    }
//
//                    @Override
//                    public void onPermissionDenied(ArrayList<String> deniedPermissions){
//
//                    }
//                });
//                pPlusPermission.checkPermission();
//                break;
//            case R.id.text_shop_review:
//                Intent intent = new Intent(this, ReviewActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivityForResult(intent, Const.REQ_POST);
//                break;
//            case R.id.text_shop_note:
//                intent = new Intent(this, NoteContainerActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivityForResult(intent, Const.REQ_POST);
//                break;
//            case R.id.text_shop_share:
//                share();
//                break;
//            case R.id.image_shop_camera:
//                AlertBuilder.Builder builder = new AlertBuilder.Builder();
//                builder.setStyle_alert(AlertBuilder.STYLE_ALERT.LIST_BOTTOM);
//
//                String[] contents = null;
//                if(mPage.getBackgroundImage() == null || StringUtils.isEmpty(mPage.getBackgroundImage().getUrl())) {
//                    contents = new String[]{getString(R.string.word_select_album), getString(R.string.word_select_default_image)};
//                } else {
//                    contents = new String[]{getString(R.string.word_select_album), getString(R.string.word_select_default_image), getString(R.string.word_delete)};
//                }
//
//                builder.setContents(contents);
//                builder.setLeftText(getString(R.string.word_cancel));
//                builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//                    @Override
//                    public void onCancel(){
//
//                    }
//
//                    @Override
//                    public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                        switch (event_alert) {
//                            case LIST:
//                                switch (event_alert.getValue()) {
//                                    case 1:
//
//                                        PPlusPermission pPlusPermission = new PPlusPermission(ShopActivity.this);
//                                        pPlusPermission.addPermission(Permission.PERMISSION_KEY.STORAGE);
//                                        pPlusPermission.setPermissionListener(new PermissionListener(){
//
//                                            @Override
//                                            public void onPermissionGranted(){
//
//                                                Intent intent = new Intent(ShopActivity.this, PhotoTakerActivity.class);
//                                                intent.putExtra("mode", "picture");
//                                                intent.putExtra("fix_ratio", false);
//                                                startActivityForResult(intent, Const.REQ_BACKGROUND_IMAGE);
//                                            }
//
//                                            @Override
//                                            public void onPermissionDenied(ArrayList<String> deniedPermissions){
//
//                                            }
//                                        });
//                                        pPlusPermission.checkPermission();
//
//                                        break;
//                                    case 2:
//                                        Intent intent = new Intent(ShopActivity.this, TemplateActivity.class);
//                                        startActivityForResult(intent, Const.REQ_BACKGROUND_DEFAULT_IMAGE);
//                                        break;
//                                    case 3:
//                                        LoginInfoManager.getInstance().getUser().getPage().setBackgroundImage(null);
//                                        LoginInfoManager.getInstance().save();
//                                        image_bg.setImageResource(0);
//                                        updateBackground(null);
//                                        break;
//                                }
//                                break;
//                        }
//                    }
//                }).builder().show(this);
//                break;
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//            case Const.REQ_POST:
//                getReviewCount();
//                break;
//            case Const.REQ_BACKGROUND_IMAGE:
//                if(resultCode == RESULT_OK) {
//                    if(data != null) {
//                        Uri selectImage = data.getData();
//                        LogUtil.e(LOG_TAG, "file : {}", selectImage.getPath());
//                        apiPageBackgroundUpload(selectImage.getPath(), AttachmentTargetTypeCode.pageBackground);
//                    }
//                }
//                break;
//            case Const.REQ_BACKGROUND_DEFAULT_IMAGE:
//                if(resultCode == RESULT_OK) {
//
//                    if(data != null) {
//                        Attachment attachment = data.getParcelableExtra(Const.DATA);
//                        LoginInfoManager.getInstance().getUser().getPage().setBackgroundImage(new ImgUrl(attachment.getNo(), attachment.getUrl()));
//                        LoginInfoManager.getInstance().save();
//                        mPage = LoginInfoManager.getInstance().getUser().getPage();
//                        Glide.with(this).load(attachment.getUrl()).apply(new RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_bg);
//                        updateBackground("" + attachment.getNo());
//                    }
//
//                }
//
//                break;
//        }
//    }
//
//    private DefaultUpload defaultUpload;
//
//    private void apiPageBackgroundUpload(final String filepath, AttachmentTargetTypeCode type){
//
//        ParamsAttachment attachment = new ParamsAttachment();
//        attachment.setTargetType(type);
//        attachment.setFile(filepath);
//        attachment.setTargetNo(LoginInfoManager.getInstance().getUser().getPage().getNo());
//
//        if(defaultUpload == null) {
//            defaultUpload = new DefaultUpload(new PplusUploadListener<Attachment>(){
//
//                @Override
//                public void onResult(String tag, NewResultResponse<Attachment> resultResponse){
//
//                    hideProgress();
//                    String url = resultResponse.getData().getUrl();
//                    Long no = resultResponse.getData().getNo();
//
//
//                    LoginInfoManager.getInstance().getUser().getPage().setBackgroundImage(new ImgUrl(no, url));
//                    LoginInfoManager.getInstance().save();
//                    updateBackground("" + no);
//                    mPage = LoginInfoManager.getInstance().getUser().getPage();
//                    Glide.with(ShopActivity.this).load(url).apply(new RequestOptions().centerCrop().placeholder(R.drawable.prnumber_default_img).error(R.drawable.prnumber_default_img)).into(image_bg);
//                    PplusCommonUtil.getFile(filepath).delete();
//                    PplusCommonUtil.deleteFromMediaScanner(filepath);
//                }
//
//                @Override
//                public void onFailure(String tag, NewResultResponse resultResponse){
//
//                    LogUtil.e(LOG_TAG, "onFailure");
//                    hideProgress();
//                    PplusCommonUtil.getFile(filepath).delete();
//                    PplusCommonUtil.deleteFromMediaScanner(filepath);
//                }
//            });
//        }
//
//        showProgress("");
//        defaultUpload.request(url, attachment);
//    }
//
//    private void updateBackground(String no){
//
//        Map<String, String> params = new HashMap<>();
//        params.put("no", "" + LoginInfoManager.getInstance().getUser().getPage().getNo());
//        if(StringUtils.isNotEmpty(no)) {
//            params.put("backgroundImage.no", no);
//        }
//        showProgress("");
//        ApiBuilder.create().updatePageBackgroundImage(params).setCallback(new PplusCallback<NewResultResponse<Object>>(){
//
//            @Override
//            public void onResponse(Call<NewResultResponse<Object>> call, NewResultResponse<Object> response){
//
//                hideProgress();
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
//    private void share(){
//
//        String subjectText = null;
//        Page page = LoginInfoManager.getInstance().getUser().getPage();
//        subjectText = page.getName();
//
//        String shareText = LoginInfoManager.getInstance().getUser().getPage().getCatchphrase() + "\n" + getString(R.string.format_msg_invite_url, "index.php?pageNo="+LoginInfoManager.getInstance().getUser().getPage().getNo());
//
//        Intent intent = new Intent(Intent.ACTION_SEND);
//        intent.setAction(Intent.ACTION_SEND);
//        intent.setType("text/plain");
//        //        intent.putExtra(Intent.EXTRA_SUBJECT, subjectText);
//        intent.putExtra(Intent.EXTRA_TEXT, shareText);
//
//        Intent chooserIntent = Intent.createChooser(intent, getString(R.string.word_share_page));
//        //        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, shareIntentList.toArray(new Parcelable[]{}));
//        startActivity(chooserIntent);
//    }
//}
