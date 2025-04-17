//package com.pplus.prnumberbiz.apps.product;
//
//import android.content.ContentUris;
//import android.content.Intent;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.Bundle;
//import android.os.SystemClock;
//import android.provider.MediaStore;
//import android.support.annotation.NonNull;
//import android.support.v4.view.ViewPager;
//import android.view.View;
//import android.widget.EditText;
//
//import com.pple.pplus.utils.part.info.DeviceUtil;
//import com.pple.pplus.utils.part.logs.LogUtil;
//import com.pple.pplus.utils.part.pref.PreferenceUtil;
//import com.pple.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
//import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
//import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
//import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
//import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.apps.post.data.PostWriteImagePagerAdapter;
//import com.pplus.prnumberbiz.apps.post.data.UploadStatus;
//import com.pplus.prnumberbiz.apps.post.ui.PostGalleryActivity;
//import com.pplus.prnumberbiz.core.code.common.EnumData;
//import com.pplus.prnumberbiz.core.code.custom.AttachmentTargetTypeCode;
//import com.pplus.prnumberbiz.core.network.ApiBuilder;
//import com.pplus.prnumberbiz.core.network.model.dto.Attachment;
//import com.pplus.prnumberbiz.core.network.model.dto.Page;
//import com.pplus.prnumberbiz.core.network.model.dto.Post;
//import com.pplus.prnumberbiz.core.network.model.request.params.ParamsAttachment;
//import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
//import com.pplus.prnumberbiz.core.network.upload.DefaultUpload;
//import com.pplus.prnumberbiz.core.network.upload.PplusUploadListener;
//import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
//import com.pplus.prnumberbiz.core.util.ToastUtil;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//import java.util.concurrent.ConcurrentHashMap;
//
//import network.common.PplusCallback;
//import retrofit2.Call;
//
//public class ProductRegActivity extends BaseActivity implements ImplToolbar, View.OnClickListener{
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
//        return R.layout.activity_product_reg;
//    }
//
//    private ViewPager pager_image;
//    private View text_regPhoto;
//    private PostWriteImagePagerAdapter mImageAdapter;
//    private EditText edit_name, edit_description;
//
//    private EnumData.MODE mMode = EnumData.MODE.WRITE;
//
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//
//        boolean isLook = PreferenceUtil.getDefaultPreference(this).get(Const.PRODUCT_GUIDE, false);
//
//        if(isLook){
//            Intent intent = new Intent(this, ProductRegGuideActivity.class);
//            startActivity(intent);
//        }
//
//        uploadDataMap = new ConcurrentHashMap<>();
//
//        text_regPhoto = findViewById(R.id.text_product_regPhoto);
//        text_regPhoto.setOnClickListener(this);
//
//        findViewById(R.id.layout_product_image).getLayoutParams().height = (int) ((DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS - getResources().getDimensionPixelSize(R.dimen.width_144)) * 0.75);
//
//        pager_image = (ViewPager)findViewById(R.id.pager_product_image);
//        mImageAdapter = new PostWriteImagePagerAdapter(this);
//        pager_image.setAdapter(mImageAdapter);
//        pager_image.setOffscreenPageLimit(10);
//
//        edit_name = (EditText)findViewById(R.id.edit_product_name);
//        edit_description = (EditText)findViewById(R.id.edit_product_description);
//
//    }
//
//    @Override
//    public void onClick(View view){
//        switch (view.getId()) {
//            case R.id.text_product_regPhoto:
//                goPostGallery();
//                break;
//        }
//    }
//
//    public void goPostGallery(){
//
//        if(mImageAdapter.getCount() < 10) {
//            Intent intent = new Intent(this, PostGalleryActivity.class);
//            intent.putExtra(Const.COUNT, 10 - mImageAdapter.getCount());
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            startActivityForResult(intent, Const.REQ_GALLERY);
//        } else {
//            AlertBuilder.Builder builder = new AlertBuilder.Builder();
//            builder.setTitle(getString(R.string.word_notice_alert));
//            builder.addContents(new AlertData.MessageData(getString(R.string.msg_max_image_count), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//            builder.setLeftText(getString(R.string.word_confirm));
//            builder.setOnAlertResultListener(new OnAlertResultListener(){
//
//                @Override
//                public void onCancel(){
//
//                }
//
//                @Override
//                public void onResult(AlertBuilder.EVENT_ALERT event_alert){
//
//                }
//            }).builder().show(this);
//        }
//
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        super.onActivityResult(requestCode, resultCode, data);
//        if(resultCode == RESULT_OK) {
//            switch (requestCode) {
//                case Const.REQ_GALLERY:
//                    if(data != null) {
//                        ArrayList<Uri> dataList = data.getParcelableArrayListExtra(Const.CROPPED_IMAGE);
//                        for(Uri uri : dataList) {
//                            mImageAdapter.addData(uri.getPath());
//                            uploadFile(uri.getPath());
//                        }
//
//                        //                        for(Uri uri : dataList) {
//                        //                            mImageAdapter.addData(uri.getPath());
//                        //                        }
//                        text_regPhoto.setVisibility(View.GONE);
//                        pager_image.setVisibility(View.VISIBLE);
//                    }
//                    break;
//            }
//        }
//    }
//
//    /**
//     * 게시글 등록/수정
//     */
//    public void update(){
//
//        if(isEmptyData()) {
//            return;
//        }
//
//        if(uploadCheckThread == null || !uploadCheckThread.isAlive()) {
//            uploadCheckThread = new UploadCheckThread();
//            uploadCheckThread.setDaemon(true);
//            uploadCheckThread.start();
//        }
//
//    }
//
//    /**
//     * 등록 가능 여부 체크
//     *
//     * @return
//     */
//    private boolean isEmptyData(){
//
//        String title = edit_name.getText().toString().trim();
//
//        if(StringUtils.isEmpty(title)) {
//            ToastUtil.showAlert(this, R.string.msg_input_product_name);
//            return true;
//        }
//
//        String contents = edit_description.getText().toString();
//
//        if(StringUtils.isEmpty(contents)) {
//            ToastUtil.showAlert(this, R.string.msg_input_product_description);
//            return true;
//        }
//
//        if(mImageAdapter.getDataList() == null || mImageAdapter.getDataList().size() == 0) {
//            ToastUtil.show(ProductRegActivity.this, R.string.msg_reg_photo);
//            return true;
//        }
//        return false;
//    }
//
//    private DefaultUpload defaultUpload;
//    private ConcurrentHashMap<String, UploadStatus> uploadDataMap;
//    private UploadCheckThread uploadCheckThread;
//    private ArrayList<String> mAttachImageList;
//
//    private void uploadFile(final String url){
//
//        ParamsAttachment paramsAttachment = new ParamsAttachment();
//        paramsAttachment.setTargetType(AttachmentTargetTypeCode.postList);
//
//        paramsAttachment.setFile(url);
//
//        uploadDataMap.put(url, UploadStatus.UPLOAD);
//
//        if(defaultUpload == null) {
//            defaultUpload = new DefaultUpload(new PplusUploadListener<Attachment>(){
//
//                @Override
//                public void onResult(String tag, NewResultResponse<Attachment> resultResponse){
//
//                    LogUtil.e(LOG_TAG, "tag = {} result = {}", tag, resultResponse.getData().toString());
//
//                    if(mAttachImageList == null) {
//                        mAttachImageList = new ArrayList<>();
//                    }
//                    mImageAdapter.insertAttach(tag, resultResponse.getData());
//                    PplusCommonUtil.Companion.deleteFromMediaScanner(url);
//
//                    mAttachImageList.add(resultResponse.getData().getUrl());
//                    uploadDataMap.put(tag, UploadStatus.SUCCESS_UPLOAD);
//
//                    if(uploadCheckThread != null) {
//                        uploadCheckThread.interrupt();
//                    }
//                }
//
//                @Override
//                public void onFailure(String tag, NewResultResponse resultResponse){
//
//                    LogUtil.e(LOG_TAG, "file 전송 실패.. onFailure tag = {} result = {}", tag, resultResponse.toString());
//                    uploadDataMap.put(tag, UploadStatus.ERROR_UPLOAD);
//                }
//
//            });
//        }
//
//        defaultUpload.request(url, paramsAttachment);
//
//    }
//
//    public File getFile(String name){
//
//        File output = new File(name);
//        if(!output.exists()) {
//            try {
//                output.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return output;
//    }
//
//    public boolean deleteFromMediaScanner(String filePath){
//
//        Uri fileUri = Uri.parse(filePath);
//        filePath = fileUri.getPath();
//
//        Cursor c = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, "_data='" + filePath + "'", null, null);
//        if(c.moveToFirst()) {
//            int id = c.getInt(0);
//            Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
//
//            return getContentResolver().delete(uri, null, null) == 1;
//        }
//        return false;
//    }
//
//    /**
//     * 이미지 업로드가 정상적으로 이루졌는지 체크 후 포스트 등록 하는 쓰래드
//     */
//    public class UploadCheckThread extends Thread{
//
//        public UploadCheckThread(){
//
//            if(mMode == EnumData.MODE.WRITE) {
//                showProgress(getString(R.string.msg_registration_of_post));
//            } else {
//                showProgress(getString(R.string.msg_modification_of_post));
//            }
//        }
//
//        @Override
//        public void run(){
//
//            super.run();
//
//            boolean isRun = true;
//            ArrayList<String> requestUrls = new ArrayList<>();
//            // 재시도 카운트
//            int retryCount = 0;
//
//            while (isRun) {
//
//                // 업로드 진행중인 카운트
//                int uploadCnt = 0;
//                // 에러 카운트
//                int errorCnt = 0;
//                int retryCnt = 0;
//                int successCnt = 0;
//
//                requestUrls.clear();
//
//                Iterator<String> iterator = uploadDataMap.keySet().iterator();
//                while (iterator.hasNext()) {
//
//                    String key = iterator.next();
//                    LogUtil.e(LOG_TAG, "uploadDataMap.get(key) = {}", uploadDataMap.get(key));
//
//                    switch (uploadDataMap.get(key)) {
//                        case UPLOAD:
//                            uploadCnt++;
//                            break;
//                        case ERROR_UPLOAD:
//                            errorCnt++;
//                            requestUrls.add(key);
//                            break;
//                        case SUCCESS_UPLOAD:
//                            successCnt++;
//                            break;
//                    }
//                }
//
//                if(uploadCnt == 0 && errorCnt == 0 && retryCnt == 0) {
//                    // 정상적으로 모두 업로드가 되어있는 상태 이다.
//                    LogUtil.e(LOG_TAG, "모든 파일이 정상적으로 업로드 되었다.");
//                    break;
//                } else {
//                    if(!checkSelfNetwork()) {
//                        return;
//                    }
//                    if(retryCount > 3) {
//                        break;
//                    }
//                    for(String url : requestUrls) {
//                        LogUtil.e(LOG_TAG, "upload 요청 = {}", url);
//                        uploadFile(url);
//                    }
//                    while (isRun) {
//
//                        LogUtil.e(LOG_TAG, "업로드중인 파일 체크");
//                        // 첨부파일을 등록중 입니다.(%d/%d)
//                        showProgress(String.format(getString(R.string.format_post_photo_register), successCnt, uploadDataMap.size()));
//                        SystemClock.sleep(2 * 1000);
//                        requestUrls.clear();
//
//                        iterator = uploadDataMap.keySet().iterator();
//
//                        uploadCnt = 0;
//                        successCnt = 0;
//                        errorCnt = 0;
//
//                        while (iterator.hasNext()) {
//
//                            String key = iterator.next();
//                            LogUtil.e(LOG_TAG, "업로드 상태 체크 = {}", uploadDataMap.get(key));
//                            switch (uploadDataMap.get(key)) {
//
//                                case UPLOAD:
//                                    uploadCnt++;
//                                    break;
//                                case ERROR_UPLOAD:
//                                    errorCnt++;
//                                    break;
//                                case SUCCESS_UPLOAD:
//                                    successCnt++;
//                                    break;
//                            }
//                        }
//                        if(uploadCnt == 0) {
//                            break;
//                        }
//                    }
//                    LogUtil.e(LOG_TAG, "업로드중인 파일 체크 종료");
//                }
//                retryCount++;
//            }
//
//            if(retryCount > 3) {
//                runOnUiThread(new Runnable(){
//
//                    @Override
//                    public void run(){
//
//                        showAlert(getString(R.string.msg_error_registration_of_photo));
//                        hideProgress();
//                    }
//                });
//            } else {
//                runOnUiThread(new Runnable(){
//
//                    @Override
//                    public void run(){
//
//                        if(mMode == EnumData.MODE.WRITE) {
//                            showProgress(getString(R.string.msg_registration_of_post));
//                        } else {
//                            showProgress(getString(R.string.msg_modification_of_post));
//                        }
//                        requestPost();
//                    }
//                });
//            }
//
//            uploadCheckThread = null;
//        }
//
//        private void showProgress(final String message){
//
//            runOnUiThread(new Runnable(){
//
//                @Override
//                public void run(){
//
//                    ProductRegActivity.this.showProgress(message);
//                }
//            });
//        }
//    }
//
//    /**
//     * 네트워크 체크
//     *
//     * @return
//     */
//    public boolean checkSelfNetwork(){
//
//        if(!PplusCommonUtil.Companion.hasNetworkConnection()) {
//            AlertBuilder.Builder builder = new AlertBuilder.Builder();
//            builder.setTitle(getString(R.string.word_notice_alert));
//            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
//            builder.addContents(new AlertData.MessageData(getString(R.string.msg_disconnected_network), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
//            builder.addContents(new AlertData.MessageData(getString(R.string.msg_check_network_status), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
//            builder.setLeftText(getString(R.string.word_confirm));
//            builder.builder().show(this, true);
//            return false;
//        }
//        return true;
//    }
//
//    @NonNull
//    @Override
//    public ToolbarOption getToolbarOption(){
//
//        ToolbarOption toolbarOption = new ToolbarOption(this);
//        toolbarOption.initializeDefaultToolbar(getString(R.string.word_write_post), ToolbarOption.ToolbarMenu.LEFT);
//        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_reg));
//        return toolbarOption;
//    }
//
//    public void deleteAttachment(Attachment attachment){
//
//
//    }
//
//    private void requestPost(){
//
//        String title = edit_name.getText().toString().trim();
//
//        if(StringUtils.isEmpty(title)) {
//            ToastUtil.show(ProductRegActivity.this, R.string.msg_input_product_name);
//            return;
//        }
//
//        String contents = edit_description.getText().toString();
//
//        if(StringUtils.isEmpty(contents)) {
//            ToastUtil.show(ProductRegActivity.this, R.string.msg_input_product_description);
//            return;
//        }
//
//        Post params = new Post();
//        Page page = new Page();
//        page.setNo(LoginInfoManager.getInstance().getUser().getPage().getNo());
//        switch (mMode) {
//
//            case WRITE:
//                params.setPage(page);
//                params.setSubject(title);
//                params.setContents(contents);
//                params.setType(EnumData.PostType.pr.name());
//                params.setAttachList(mImageAdapter.getAttachList());
//                showProgress("");
//
//                ApiBuilder.create().insertPost(params).setCallback(new PplusCallback<NewResultResponse<Post>>(){
//
//                    @Override
//                    public void onResponse(Call<NewResultResponse<Post>> call, NewResultResponse<Post> response){
//
//                        hideProgress();
//                        LogUtil.e(LOG_TAG, response.getData().toString());
//                        setResult(RESULT_OK);
//                        finish();
//
//                    }
//
//                    @Override
//                    public void onFailure(Call<NewResultResponse<Post>> call, Throwable t, NewResultResponse<Post> response){
//
//                        hideProgress();
//                    }
//                }).build().call();
//                break;
//            case UPDATE:
//                params.setPage(page);
//                params.setSubject(title);
//                params.setContents(contents);
//                params.setType(EnumData.PostType.pr.name());
//                List<Attachment> list = new ArrayList<>();
//                list.addAll(mImageAdapter.getAttachedList());
//                list.addAll(mImageAdapter.getAttachList());
//                params.setAttachList(list);
//                showProgress("");
//                ApiBuilder.create().updatePost(params).setCallback(new PplusCallback<NewResultResponse<Post>>(){
//
//                    @Override
//                    public void onResponse(Call<NewResultResponse<Post>> call, NewResultResponse<Post> response){
//
//                        for(String url : mImageAdapter.getDataList()) {
//                            getFile(url).delete();
//                            deleteFromMediaScanner(url);
//                        }
//                        hideProgress();
//                        setResult(RESULT_OK);
//                        finish();
//                    }
//
//                    @Override
//                    public void onFailure(Call<NewResultResponse<Post>> call, Throwable t, NewResultResponse<Post> response){
//
//                        hideProgress();
//                    }
//                }).build().call();
//                break;
//        }
//
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
//                            update();
//                        }
//                        break;
//                }
//            }
//        };
//
//    }
//}
