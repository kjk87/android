package com.pplus.prnumberbiz.apps.post.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.pple.pplus.utils.part.info.DeviceUtil;
import com.pple.pplus.utils.part.logs.LogUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.Const;
import com.pplus.prnumberbiz.R;
import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
import com.pplus.prnumberbiz.apps.common.builder.data.AlertData;
import com.pplus.prnumberbiz.apps.common.mgmt.LoginInfoManager;
import com.pplus.prnumberbiz.apps.common.toolbar.ImplToolbar;
import com.pplus.prnumberbiz.apps.common.toolbar.OnToolbarListener;
import com.pplus.prnumberbiz.apps.common.toolbar.ToolbarOption;
import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
import com.pplus.prnumberbiz.apps.post.data.PostWriteImagePagerAdapter;
import com.pplus.prnumberbiz.apps.post.data.UploadStatus;
import com.pplus.prnumberbiz.core.code.common.EnumData;
import com.pplus.prnumberbiz.core.code.custom.AttachmentTargetTypeCode;
import com.pplus.prnumberbiz.core.network.ApiBuilder;
import com.pplus.prnumberbiz.core.network.model.dto.No;
import com.pplus.prnumberbiz.core.network.model.dto.Post;
import com.pplus.prnumberbiz.core.network.model.dto.PostProperties;
import com.pplus.prnumberbiz.core.network.model.request.params.ParamsAttachment;
import com.pplus.prnumberbiz.core.network.model.response.NewResultResponse;
import com.pplus.prnumberbiz.core.network.model.dto.Attachment;
import com.pplus.prnumberbiz.core.network.upload.DefaultUpload;
import com.pplus.prnumberbiz.core.network.upload.PplusUploadListener;
import com.pplus.prnumberbiz.core.util.PplusCommonUtil;
import com.pplus.prnumberbiz.core.util.ToastUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import network.common.PplusCallback;
import retrofit2.Call;

public class PostWriteActivity extends BaseActivity implements ImplToolbar, View.OnClickListener {

    @Override
    public String getPID() {

        return null;
    }


    @Override
    public int getLayoutRes() {

        return R.layout.activity_post_write;
    }

    private ViewPager pager_post_image;
    private EditText edit_post_contents;
    private View layout_post_regPhoto;
    private EnumData.MODE mPostMode;
    private Post mPost;
    private PostWriteImagePagerAdapter mImageAdapter;
    private EnumData.PostType mPostType;
    private boolean mIsEditing = false;

    @Override
    public void initializeView(Bundle savedInstanceState) {

        mPostType = (EnumData.PostType) getIntent().getSerializableExtra(Const.TYPE);

        if (mPostType == null) {
            mPostType = EnumData.PostType.pr;
        }

        mPostMode = (EnumData.MODE) getIntent().getSerializableExtra(Const.MODE);
        uploadDataMap = new ConcurrentHashMap<>();

        layout_post_regPhoto = findViewById(R.id.layout_post_add_image);
        layout_post_regPhoto.setOnClickListener(this);
        pager_post_image = (ViewPager) findViewById(R.id.pager_post_image);
        edit_post_contents = (EditText) findViewById(R.id.edit_post_contents);

        ((RelativeLayout) findViewById(R.id.layout_post_image)).getLayoutParams().height = (int) ((DeviceUtil.DISPLAY.SCREEN_WIDTH_PIXELS - getResources().getDimensionPixelSize(R.dimen.width_144)) * 0.75);

        mImageAdapter = new PostWriteImagePagerAdapter(this);
        pager_post_image.setAdapter(mImageAdapter);
        pager_post_image.setOffscreenPageLimit(10);

        if (mPostMode.equals(EnumData.MODE.UPDATE)) {
            mPost = getIntent().getParcelableExtra(Const.POST);

            if (StringUtils.isNotEmpty(mPost.getContents())) {
                edit_post_contents.setText(mPost.getContents());
            }

            if (mPost.getAttachList() != null && mPost.getAttachList().size() > 0) {
                pager_post_image.setVisibility(View.VISIBLE);
            }
            mImageAdapter.setAttachedList(mPost.getAttachList());
        }
    }

    @Override
    public void onClick(View view) {

        Intent intent = null;

        switch (view.getId()) {
            case R.id.layout_post_add_image:
                goPostGallery();
                break;
        }
    }

    public void goPostGallery() {

        if (mImageAdapter.getCount() < 10) {
            Intent intent = new Intent(this, PostGalleryActivity.class);
            intent.putExtra(Const.COUNT, 10 - mImageAdapter.getCount());
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivityForResult(intent, Const.REQ_GALLERY);
        } else {
            AlertBuilder.Builder builder = new AlertBuilder.Builder();
            builder.setTitle(getString(R.string.word_notice_alert));
            builder.addContents(new AlertData.MessageData(getString(R.string.msg_max_image_count), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
            builder.setLeftText(getString(R.string.word_confirm));
            builder.setOnAlertResultListener(new OnAlertResultListener() {

                @Override
                public void onCancel() {

                }

                @Override
                public void onResult(AlertBuilder.EVENT_ALERT event_alert) {

                }
            }).builder().show(this);
        }

    }

    private PostProperties properties;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Const.REQ_POST_SNS) {
            for (String url : mImageAdapter.getDataList()) {
                PplusCommonUtil.Companion.deleteFromMediaScanner(url);
            }
            setResult(RESULT_OK);
            finish();
        }
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case Const.REQ_GALLERY:
                    if (data != null) {
                        ArrayList<Uri> dataList = data.getParcelableArrayListExtra(Const.CROPPED_IMAGE);
                        ArrayList<String> list = new ArrayList<>();
                        list.addAll(mImageAdapter.getDataList());

                        for (Uri uri : dataList) {
                            list.add(uri.getPath());
                            uploadFile(uri.getPath());
                        }
                        mImageAdapter.setDataList(list);

                        layout_post_regPhoto.setVisibility(View.GONE);
                        pager_post_image.setVisibility(View.VISIBLE);
                    }
                    break;

            }
        }

    }

    /**
     * 게시글 등록/수정
     */
    public void update() {

        if (isEmptyData()) {
            return;
        }

        if (uploadCheckThread == null || !uploadCheckThread.isAlive()) {
            uploadCheckThread = new UploadCheckThread();
            uploadCheckThread.setDaemon(true);
            uploadCheckThread.start();
        }

    }

    /**
     * 등록 가능 여부 체크
     *
     * @return
     */
    private boolean isEmptyData() {

        String contents = edit_post_contents.getText().toString();

        if (StringUtils.isEmpty(contents)) {
            ToastUtil.show(PostWriteActivity.this, R.string.msg_input_event_contents);
            return true;
        }

        if ((mImageAdapter.getAttachedList() == null || mImageAdapter.getAttachedList().size() == 0) && (mImageAdapter.getDataList() == null || mImageAdapter.getDataList().size() == 0)) {
            ToastUtil.show(PostWriteActivity.this, R.string.msg_reg_photo);
            return true;
        }
        return false;
    }

    private DefaultUpload defaultUpload;
    private ConcurrentHashMap<String, UploadStatus> uploadDataMap;
    private UploadCheckThread uploadCheckThread;
    private ArrayList<String> mAttachImageList;

    private void uploadFile(final String url) {

        ParamsAttachment paramsAttachment = new ParamsAttachment();
        paramsAttachment.setTargetType(AttachmentTargetTypeCode.postList);

        paramsAttachment.setFile(url);

        uploadDataMap.put(url, UploadStatus.UPLOAD);

        if (defaultUpload == null) {
            defaultUpload = new DefaultUpload(new PplusUploadListener<Attachment>() {

                @Override
                public void onResult(String tag, NewResultResponse<Attachment> resultResponse) {

                    LogUtil.e(LOG_TAG, "tag = {} result = {}", tag, resultResponse.getData().toString());

                    if (mAttachImageList == null) {
                        mAttachImageList = new ArrayList<>();
                    }
                    mImageAdapter.insertAttach(tag, resultResponse.getData());

                    mAttachImageList.add(resultResponse.getData().getUrl());
                    uploadDataMap.put(tag, UploadStatus.SUCCESS_UPLOAD);

                    if (uploadCheckThread != null) {
                        uploadCheckThread.interrupt();
                    }
                }

                @Override
                public void onFailure(String tag, NewResultResponse resultResponse) {

                    LogUtil.e(LOG_TAG, "file 전송 실패.. onFailure tag = {} result = {}", tag, resultResponse.toString());
                    uploadDataMap.put(tag, UploadStatus.ERROR_UPLOAD);
                }

            });
        }

        defaultUpload.request(url, paramsAttachment);

    }

    /**
     * 이미지 업로드가 정상적으로 이루졌는지 체크 후 포스트 등록 하는 쓰래드
     */
    public class UploadCheckThread extends Thread {

        public UploadCheckThread() {

            if (mPostMode == EnumData.MODE.WRITE) {
                showProgress(getString(R.string.msg_registration_of_post));
            } else {
                showProgress(getString(R.string.msg_modification_of_post));
            }
        }

        @Override
        public void run() {

            super.run();

            boolean isRun = true;
            ArrayList<String> requestUrls = new ArrayList<>();
            // 재시도 카운트
            int retryCount = 0;

            while (isRun) {

                // 업로드 진행중인 카운트
                int uploadCnt = 0;
                // 에러 카운트
                int errorCnt = 0;
//                int retryCnt = 0;
                int successCnt = 0;

                requestUrls.clear();

                Iterator<String> iterator = uploadDataMap.keySet().iterator();
                while (iterator.hasNext()) {

                    String key = iterator.next();
                    LogUtil.e(LOG_TAG, "uploadDataMap.get(key) = {}", uploadDataMap.get(key));

                    switch (uploadDataMap.get(key)) {
                        case UPLOAD:
                            uploadCnt++;
                            break;
                        case ERROR_UPLOAD:
                            errorCnt++;
                            requestUrls.add(key);
                            break;
                        case SUCCESS_UPLOAD:
                            successCnt++;
                            break;
                    }
                }

                if (uploadCnt == 0 && errorCnt == 0) {
                    // 정상적으로 모두 업로드가 되어있는 상태 이다.
                    LogUtil.e(LOG_TAG, "모든 파일이 정상적으로 업로드 되었다.");
                    break;
                } else {
                    if (!checkSelfNetwork()) {
                        return;
                    }
                    if (retryCount > 3) {
                        break;
                    }
                    for (String url : requestUrls) {
                        LogUtil.e(LOG_TAG, "upload 요청 = {}", url);
                        uploadFile(url);
                    }
                    while (isRun) {

                        LogUtil.e(LOG_TAG, "업로드중인 파일 체크");
                        // 첨부파일을 등록중 입니다.(%d/%d)
                        showProgress(String.format(getString(R.string.format_post_photo_register), successCnt, uploadDataMap.size()));
                        SystemClock.sleep(2 * 1000);
                        requestUrls.clear();

                        iterator = uploadDataMap.keySet().iterator();

                        uploadCnt = 0;
                        successCnt = 0;
                        errorCnt = 0;

                        while (iterator.hasNext()) {

                            String key = iterator.next();
                            LogUtil.e(LOG_TAG, "업로드 상태 체크 = {}", uploadDataMap.get(key));
                            switch (uploadDataMap.get(key)) {

                                case UPLOAD:
                                    uploadCnt++;
                                    break;
                                case ERROR_UPLOAD:
                                    errorCnt++;
                                    break;
                                case SUCCESS_UPLOAD:
                                    successCnt++;
                                    break;
                            }
                        }
                        if (uploadCnt == 0) {
                            break;
                        }
                    }
                    LogUtil.e(LOG_TAG, "업로드중인 파일 체크 종료");
                }
                retryCount++;
            }

            if (retryCount > 3) {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        showAlert(getString(R.string.msg_error_registration_of_photo));
                        hideProgress();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {

                        if (mPostMode == EnumData.MODE.WRITE) {
                            showProgress(getString(R.string.msg_registration_of_post));
                        } else {
                            showProgress(getString(R.string.msg_modification_of_post));
                        }
                        requestPost();
                    }
                });
            }

            uploadCheckThread = null;
        }

        private void showProgress(final String message) {

            runOnUiThread(new Runnable() {

                @Override
                public void run() {

                    PostWriteActivity.this.showProgress(message);
                }
            });
        }
    }

    /**
     * 네트워크 체크
     *
     * @return
     */
    public boolean checkSelfNetwork() {

        if (!PplusCommonUtil.Companion.hasNetworkConnection()) {
            AlertBuilder.Builder builder = new AlertBuilder.Builder();
            builder.setTitle(getString(R.string.word_notice_alert));
            builder.setStyle_alert(AlertBuilder.STYLE_ALERT.MESSAGE);
            builder.addContents(new AlertData.MessageData(getString(R.string.msg_disconnected_network), AlertBuilder.MESSAGE_TYPE.TEXT, 1));
            builder.addContents(new AlertData.MessageData(getString(R.string.msg_check_network_status), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
            builder.setLeftText(getString(R.string.word_confirm));
            builder.builder().show(this, true);
            return false;
        }
        return true;
    }

    @NonNull
    @Override
    public ToolbarOption getToolbarOption() {

        ToolbarOption toolbarOption = new ToolbarOption(this);
        toolbarOption.initializeDefaultToolbar(getString(R.string.msg_notify_news), ToolbarOption.ToolbarMenu.LEFT);
        toolbarOption.setToolbarMenu(ToolbarOption.ToolbarMenu.RIGHT, getString(R.string.word_reg));
        return toolbarOption;
    }

    private void requestPost() {

        String contents = edit_post_contents.getText().toString();

        if (StringUtils.isEmpty(contents)) {
            ToastUtil.show(PostWriteActivity.this, R.string.msg_input_event_contents);
            return;
        }

        final Post params = new Post();
        params.setBoard(new No(LoginInfoManager.getInstance().getUser().getPage().getPrBoard().getNo()));
        //        params.setSubject(title);
        params.setContents(contents);
        params.setType(mPostType.name());
        if (properties != null && StringUtils.isNotEmpty(properties.getLuckyBol())) {
            params.setProperties(properties);
        }
        switch (mPostMode) {

            case WRITE:
                if (mImageAdapter.getAttachList() != null && mImageAdapter.getAttachList().size() > 0) {
                    params.setAttachList(mImageAdapter.getAttachList());
                }

                showProgress("");

                ApiBuilder.create().insertPost(params).setCallback(new PplusCallback<NewResultResponse<Post>>() {

                    @Override
                    public void onResponse(Call<NewResultResponse<Post>> call, NewResultResponse<Post> response) {

                        hideProgress();
//                        final Post post = response.getData();

                        if (mPostMode == EnumData.MODE.WRITE) {
                            showAlert(R.string.msg_registed_post);
                        } else {
                            showAlert(R.string.msg_modified_post);
                        }
                        setResult(RESULT_OK);
                        finish();
//                        if(mPage.getProperties().getMultiPost()) {
//                            Intent intent = new Intent(PostWriteActivity.this, PostSnsActivity.class);
//                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                            intent.putExtra(Const.TITLE, params.getSubject());
//                            intent.putExtra(Const.CONTENTS, params.getContents());
//                            intent.putStringArrayListExtra(Const.PATH, mImageAdapter.getDataList());
//                            intent.putStringArrayListExtra(Const.IMAGE_URL, mAttachImageList);
//                            startActivityForResult(intent, Const.REQ_POST_SNS);
//                        } else {
//                            if(mPostMode == EnumData.MODE.WRITE) {
//                                showAlert(R.string.msg_registed_post);
//                            } else {
//                                showAlert(R.string.msg_modified_post);
//                            }
//
//                            setResult(RESULT_OK);
//                            finish();
//
//                        }


                    }

                    @Override
                    public void onFailure(Call<NewResultResponse<Post>> call, Throwable t, NewResultResponse<Post> response) {

                        hideProgress();
                    }
                }).build().call();
                break;
            case UPDATE:
                params.setNo(mPost.getNo());
                List<Attachment> list = new ArrayList<>();
                if (mImageAdapter.getAttachedList() != null && mImageAdapter.getAttachedList().size() > 0) {
                    list.addAll(mImageAdapter.getAttachedList());
                }

                if (mImageAdapter.getAttachList() != null && mImageAdapter.getAttachList().size() > 0) {
                    list.addAll(mImageAdapter.getAttachList());
                }

                if (list.size() > 0) {
                    params.setAttachList(list);
                }

                showProgress("");
                ApiBuilder.create().updatePost(params).setCallback(new PplusCallback<NewResultResponse<Post>>() {

                    @Override
                    public void onResponse(Call<NewResultResponse<Post>> call, NewResultResponse<Post> response) {

                        hideProgress();
                        Intent data = new Intent();
                        data.putExtra(Const.DATA, mPost);
                        data.putExtra(Const.POSITION, getIntent().getIntExtra(Const.POSITION, 0));
                        setResult(RESULT_OK, data);
                        finish();
                    }

                    @Override
                    public void onFailure(Call<NewResultResponse<Post>> call, Throwable t, NewResultResponse<Post> response) {

                        hideProgress();
                    }
                }).build().call();
                break;
        }

        for (String path : mImageAdapter.getDataList()) {
            PplusCommonUtil.Companion.deleteFromMediaScanner(path);
        }
    }

    @Override
    public OnToolbarListener getOnToolbarClickListener() {

        return new OnToolbarListener() {

            @Override
            public void onClick(View v, ToolbarOption.ToolbarMenu toolbarMenu, Object tag) {

                switch (toolbarMenu) {
                    case LEFT:
                        if (tag.equals(1)) {
                            onBackPressed();
                        }
                        break;
                    case RIGHT:
                        if (tag.equals(1)) {

                            if (isEmptyData()) {
                                return;
                            }
                            AlertBuilder.Builder builder = new AlertBuilder.Builder();
                            builder.setTitle(getString(R.string.word_notice_alert));
                            builder.addContents(new AlertData.MessageData(getString(R.string.msg_question_post_write), AlertBuilder.MESSAGE_TYPE.TEXT, 2));
                            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
                            builder.setOnAlertResultListener(new OnAlertResultListener() {

                                @Override
                                public void onCancel() {

                                }

                                @Override
                                public void onResult(AlertBuilder.EVENT_ALERT event_alert) {

                                    switch (event_alert) {
                                        case RIGHT:
                                            update();
                                            break;
                                    }
                                }
                            }).builder().show(PostWriteActivity.this);
                        }
                        break;
                }
            }
        };

    }

    @Override
    public void onBackPressed() {

        String contents = edit_post_contents.getText().toString().trim();
        boolean isImage = true;
        if ((mImageAdapter.getAttachedList() == null || mImageAdapter.getAttachedList().size() == 0) && (mImageAdapter.getDataList() == null || mImageAdapter.getDataList().size() == 0)) {
            isImage = false;
        }

        if (StringUtils.isNotEmpty(contents) || isImage) {
            AlertBuilder.Builder builder = new AlertBuilder.Builder();
            builder.setTitle(getString(R.string.word_notice_alert));
            builder.addContents(new AlertData.MessageData(getString(R.string.msg_question_post_back), AlertBuilder.MESSAGE_TYPE.TEXT, 3));
            builder.setLeftText(getString(R.string.word_cancel)).setRightText(getString(R.string.word_confirm));
            builder.setOnAlertResultListener(new OnAlertResultListener() {

                @Override
                public void onCancel() {

                }

                @Override
                public void onResult(AlertBuilder.EVENT_ALERT event_alert) {

                    switch (event_alert) {
                        case RIGHT:
                            finish();
                            break;
                    }
                }
            }).builder().show(this);
        } else {
            finish();
        }

    }

    @Override
    public void finish() {

        for (String path : mImageAdapter.getDataList()) {
            PplusCommonUtil.Companion.deleteFromMediaScanner(path);
        }
        super.finish();
    }
}
