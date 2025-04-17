//package com.pplus.prnumberbiz.apps.post.ui;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.util.SparseArray;
//import android.view.View;
//
//import com.facebook.AccessToken;
//import com.facebook.FacebookException;
//import com.facebook.GraphRequest;
//import com.facebook.GraphResponse;
//import com.facebook.HttpMethod;
//import com.kakao.auth.AuthType;
//import com.kakao.auth.ErrorCode;
//import com.kakao.auth.ISessionCallback;
//import com.kakao.auth.Session;
//import com.kakao.kakaostory.KakaoStoryService;
//import com.kakao.kakaostory.callback.StoryResponseCallback;
//import com.kakao.kakaostory.request.PostRequest;
//import com.kakao.kakaostory.response.ProfileResponse;
//import com.kakao.kakaostory.response.model.MyStoryInfo;
//import com.kakao.network.ErrorResult;
//import com.kakao.usermgmt.callback.MeResponseCallback;
//import com.kakao.usermgmt.response.model.UserProfile;
//import com.kakao.util.KakaoParameterException;
//import com.kakao.util.exception.KakaoException;
//import com.kakao.util.helper.log.Logger;
//import com.pple.pplus.utils.part.logs.LogUtil;
//import com.pplus.prnumberbiz.Const;
//import com.pplus.prnumberbiz.R;
//import com.pplus.prnumberbiz.apps.common.builder.AlertBuilder;
//import com.pplus.prnumberbiz.apps.common.builder.OnAlertResultListener;
//import com.pplus.prnumberbiz.apps.common.ui.base.BaseActivity;
//import com.pplus.prnumberbiz.core.sns.facebook.FaceBookUtil;
//import com.pplus.prnumberbiz.core.sns.kakao.KakaoUtil;
//import com.pplus.prnumberbiz.core.sns.twitter.TwitterUtil;
//import com.twitter.sdk.android.core.Callback;
//import com.twitter.sdk.android.core.Result;
//import com.twitter.sdk.android.core.TwitterApiClient;
//import com.twitter.sdk.android.core.TwitterCore;
//import com.twitter.sdk.android.core.TwitterException;
//import com.twitter.sdk.android.core.TwitterSession;
//import com.twitter.sdk.android.core.models.Media;
//import com.twitter.sdk.android.core.models.Tweet;
//import com.twitter.sdk.android.core.services.MediaService;
//import com.twitter.sdk.android.core.services.StatusesService;
//
//import org.json.JSONObject;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import okhttp3.MediaType;
//import okhttp3.RequestBody;
//import retrofit2.Call;
//
//public class PostSnsActivity extends BaseActivity implements View.OnClickListener{
//
//    @Override
//    public String getPID(){
//
//        return null;
//    }
//
//    @Override
//    public int getLayoutRes(){
//
//        return R.layout.activity_post_sns;
//    }
//
//    private boolean mIsUploadTwitter, mIsUploadFacebook, mIsUploadKakao, mIsFinishFacebook,mIsFinishTwitter, mIsFinishKakao;
//    private Long mTwitterId;
//    private ArrayList<String> mImageList;
//    private ArrayList<String> mImageUrlList;
//    private SparseArray<String> mTweetMediaArray, mFaceBookMediaArray;
//    private View layout_facebook, layout_kakaostory, layout_twitter, image_facebook_check, image_kakaostory_check, image_twitter_check;
//    private String title, contents;
//
//    private AccessToken mFacebookAccessToken;
//
//    @Override
//    public void initializeView(Bundle savedInstanceState){
//
//        mImageList = getIntent().getStringArrayListExtra(Const.PATH);
//        mImageUrlList = getIntent().getStringArrayListExtra(Const.IMAGE_URL);
//        title = getIntent().getStringExtra(Const.TITLE);
//        contents = getIntent().getStringExtra(Const.CONTENTS);
//
//        layout_facebook = findViewById(R.id.layout_post_sns_facebook);
//        layout_kakaostory = findViewById(R.id.layout_post_sns_kakaostory);
//        layout_twitter = findViewById(R.id.layout_post_sns_twitter);
//        layout_facebook.setOnClickListener(this);
//        layout_kakaostory.setOnClickListener(this);
//        layout_twitter.setOnClickListener(this);
//        findViewById(R.id.text_post_sns_confirm).setOnClickListener(this);
//        mIsUploadTwitter = false;
//        mIsUploadFacebook = false;
//        mIsUploadKakao = false;
//        image_facebook_check = findViewById(R.id.image_post_sns_facebook_check);
//        image_kakaostory_check = findViewById(R.id.image_post_sns_kakaostory_check);
//        image_twitter_check = findViewById(R.id.image_post_sns_twitter_check);
//        image_facebook_check.setVisibility(View.GONE);
//        image_kakaostory_check.setVisibility(View.GONE);
//        image_twitter_check.setVisibility(View.GONE);
//    }
//
//    private void uploadFinish(){
//        if(mIsFinishFacebook && mIsFinishKakao && mIsFinishTwitter){
//            showAlert(R.string.msg_complete_upload_sns);
//            finish();
//        }
//    }
//
//    @Override
//    public void onClick(View view){
//
//        switch (view.getId()) {
//            case R.id.layout_post_sns_facebook:
//                if(!mIsUploadFacebook) {
//                    facebookLogin();
//                } else {
//                    mIsUploadFacebook = false;
//                    image_facebook_check.setVisibility(View.GONE);
//                }
//                break;
//            case R.id.layout_post_sns_kakaostory:
//                if(!mIsUploadKakao) {
//                    showProgress("");
//                    mKakaoSessionCallback = new KakaoSessionCallback();
//                    KakaoUtil.addCallback(mKakaoSessionCallback);
//                    Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, this);
//                } else {
//                    mIsUploadKakao = false;
//                    image_kakaostory_check.setVisibility(View.GONE);
//                }
//
//                break;
//            case R.id.layout_post_sns_twitter:
//                if(!mIsUploadTwitter) {
//                    twitterLogin();
//                } else {
//                    mIsUploadTwitter = false;
//                    image_twitter_check.setVisibility(View.GONE);
//                }
//                break;
//            case R.id.text_post_sns_confirm:
//                if(!mIsUploadFacebook && !mIsUploadKakao && !mIsUploadTwitter){
//                    finish();
//                    return;
//                }
//                mIsFinishFacebook = !mIsUploadFacebook;
//                mIsFinishTwitter = !mIsUploadTwitter;
//                mIsFinishKakao = !mIsUploadKakao;
//                if(mIsUploadFacebook) {
//                    facebookUpload();
//                }
//
//                if(mIsUploadTwitter) {
//                    twitterUpload();
//                }
//
//                if(mIsUploadKakao) {
//                    try {
//                        kakaoUpload(mImageList, title + "\n" + contents);
//                    } catch (Exception e) {
//
//                    }
//                }
//                break;
//        }
//    }
//
//    private void facebookLogin(){
//
//        showProgress("");
//        FaceBookUtil.registerCallback(new FaceBookUtil.FaceBookCallbackListener(){
//
//            @Override
//            public void onSuccess(com.facebook.login.LoginResult loginResult){
//
//                hideProgress();
//                //로그인 성공후 프로필 정보 요청
//                mFacebookAccessToken = loginResult.getAccessToken();
//                mIsUploadFacebook = true;
//                image_facebook_check.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onCancel(){
//
//                hideProgress();
//            }
//
//            @Override
//            public void onError(FacebookException exception){
//
//                hideProgress();
//            }
//
//            @Override
//            public void onCompleted(JSONObject object, GraphResponse response){
//
//                hideProgress();
//            }
//        });
//        FaceBookUtil.logInWithPublishActions(this);
//    }
//
//    private void facebookUpload(){
//
//        if(mIsUploadFacebook && mFacebookAccessToken != null) {
//
//            mFaceBookMediaArray = new SparseArray<String>();
//            for(int i = 0; i < mImageUrlList.size(); i++) {
//                showProgress("");
//                facebookImage(mFacebookAccessToken, i);
//            }
//        }
//    }
//
//    private void facebookPosting(AccessToken accessToken, String message, String object_attachment){
//
//        Bundle params = new Bundle();
//        params.putString("message", message);
//        params.putString("object_attachment", object_attachment);
//        /* make the API call */
//        showProgress("");
//        new GraphRequest(accessToken, "me/feed", params, HttpMethod.POST, new GraphRequest.Callback(){
//
//            public void onCompleted(GraphResponse response){
//                /* handle the result */
//                hideProgress();
//                LogUtil.e(LOG_TAG, response.toString());
//                mIsFinishFacebook = true;
//                uploadFinish();
//            }
//        }).executeAsync();
//    }
//
//    private void facebookImage(final AccessToken accessToken, final int position){
//
//        showProgress("");
//        Bundle params = new Bundle();
//        params.putString("url", mImageUrlList.get(position));
//                /* make the API call */
//        new GraphRequest(accessToken, accessToken.getUserId() + "/photos", params, HttpMethod.POST, new GraphRequest.Callback(){
//
//            public void onCompleted(GraphResponse response){
//                        /* handle the result */
//                hideProgress();
//                LogUtil.e(LOG_TAG, response.toString());
//                try {
//                    String id = response.getJSONObject().getString("id");
//                    mFaceBookMediaArray.put(position, id);
//                } catch (Exception e) {
//
//                }
//
//                if(mFaceBookMediaArray.size() == mImageUrlList.size()) {
//                    StringBuilder mediaIds = new StringBuilder();
//                    for(int i = 0; i < mFaceBookMediaArray.size(); i++) {
//                        mediaIds.append(mFaceBookMediaArray.get(i));
//                        if(i < mFaceBookMediaArray.size() - 1) {
//                            mediaIds.append(",");
//                        }
//                    }
//                    facebookPosting(accessToken, title + "\n" + contents, mediaIds.toString());
//                }
//
//            }
//        }).executeAsync();
//    }
//
//    private void kakaoUpload(ArrayList<String> imageList, final String storyContent) throws KakaoParameterException{
//
//        showProgress("");
//        List<File> fileList = new ArrayList<File>();
//        for(String path : imageList) {
//            fileList.add(new File(path));
//        }
//
//
//        PostRequest.StoryPermission permission = PostRequest.StoryPermission.PUBLIC;
//        KakaoStoryService.getInstance().requestPostPhoto(new StoryResponseCallback<MyStoryInfo>(){
//
//            @Override
//            public void onNotKakaoStoryUser(){
//
//                LogUtil.e(LOG_TAG, "onNotKakaoStoryUser");
//                hideProgress();
//            }
//
//            @Override
//            public void onSessionClosed(ErrorResult errorResult){
//
//                LogUtil.e(LOG_TAG, "onSessionClosed");
//                hideProgress();
//            }
//
//            @Override
//            public void onNotSignedUp(){
//
//                LogUtil.e(LOG_TAG, "onNotSignedUp");
//                hideProgress();
//            }
//
//            @Override
//            public void onSuccess(MyStoryInfo result){
//
//                LogUtil.e(LOG_TAG, "onSuccess");
//                hideProgress();
//                mIsFinishKakao = true;
//                uploadFinish();
//            }
//        }, fileList, storyContent, permission, true, "", "", null, null);
//    }
//
//    private KakaoSessionCallback mKakaoSessionCallback;
//
//    //카카오톡 콜백class
//    class KakaoSessionCallback implements ISessionCallback{
//
//        @Override
//        public void onSessionOpened(){
//
//            KakaoUtil.requestMe(new MeResponseCallback(){
//
//                @Override
//                public void onFailure(ErrorResult errorResult){
//
//                    LogUtil.e(LOG_TAG, "onFailure");
//                    hideProgress();
//                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
//                    if(result == ErrorCode.CLIENT_ERROR_CODE) {
//
//                    } else {
//
//                    }
//                }
//
//                @Override
//                public void onSessionClosed(ErrorResult errorResult){
//
//                    LogUtil.e(LOG_TAG, "onSessionClosed");
//                    hideProgress();
//                }
//
//                @Override
//                public void onSuccess(UserProfile userProfile){
//
//                    LogUtil.e(LOG_TAG, "onSuccess");
//                    KakaoUtil.requestIsStoryUser(new KakaoUtil.KakaoStoryListener(){
//
//                        @Override
//                        public void success(ProfileResponse result){
//
//                            LogUtil.e(LOG_TAG, "success");
//                            hideProgress();
//                            mIsUploadKakao = true;
//                            image_kakaostory_check.setVisibility(View.VISIBLE);
//
//                            KakaoUtil.remvoeCallback(mKakaoSessionCallback);
//                        }
//
//                        @Override
//                        public void failure(){
//
//                            LogUtil.e(LOG_TAG, "failure");
//                            hideProgress();
//                        }
//                    });
//                }
//
//                @Override
//                public void onNotSignedUp(){
//
//                    LogUtil.e(LOG_TAG, "onNotSignedUp");
//                    hideProgress();
//                }
//            });
//        }
//
//        @Override
//        public void onSessionOpenFailed(KakaoException exception){
//
//            hideProgress();
//            if(exception != null) {
//                Logger.e(exception);
//            }
//        }
//    }
//
//    private void twitterLogin(){
//
//        showProgress("");
//        TwitterUtil.auth(this, new TwitterUtil.TwitterAuthListener(){
//
//            @Override
//            public void success(Result<TwitterSession> result){
//
//                hideProgress();
//                mTwitterId = result.data.getUserId();
//                mIsUploadTwitter = true;
//                image_twitter_check.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void failure(TwitterException e){
//
//                hideProgress();
//                layout_twitter.setSelected(false);
//            }
//        });
//    }
//
//    private void twitterUpload(){
//
//        if(mIsUploadTwitter && mTwitterId != null) {
//
//            if(mImageList.size() < mTwitterMaxCount) {
//                mTwitterMaxCount = mImageList.size();
//            }
//
//            mTweetMediaArray = new SparseArray<String>();
//            for(int i = 0; i < mTwitterMaxCount; i++) {
//                showProgress("");
//                uploadTwitterImage(i);
//            }
//        }
//    }
//
//    private int mTwitterMaxCount = 4;
//
//    private void uploadTwitterImage(final int position){
//
//        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
//        MediaService mediaService = twitterApiClient.getMediaService();
//        Call<Media> call = mediaService.upload(RequestBody.create(MediaType.parse(mImageList.get(position)), new File(mImageList.get(position))), null, null);
//        call.enqueue(new Callback<Media>(){
//
//            @Override
//            public void success(Result<Media> result){
//
//                mTweetMediaArray.put(position, result.data.mediaIdString);
//                if(mTweetMediaArray.size() == mTwitterMaxCount) {
//                    StringBuilder mediaIds = new StringBuilder();
//                    for(int i = 0; i < mTweetMediaArray.size(); i++) {
//                        mediaIds.append(mTweetMediaArray.get(i));
//                        if(i < mTweetMediaArray.size() - 1) {
//                            mediaIds.append(",");
//                        }
//                    }
//                    updateTweet(mTwitterId, mediaIds.toString());
//                }
//            }
//
//            @Override
//            public void failure(TwitterException exception){
//
//            }
//        });
//    }
//
//
//    private void updateTweet(long id, String mediaIds){
//
//        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
//        // Can also use Twitter directly: Twitter.getApiClient()
//        StatusesService statusesService = twitterApiClient.getStatusesService();
//        Call<Tweet> call = statusesService.update(title + "\n" + contents, id, null, null, null, null, null, null, mediaIds);
//        call.enqueue(new Callback<Tweet>(){
//
//            @Override
//            public void success(Result<Tweet> result){
//                //Do something with result
//                hideProgress();
//                mIsFinishTwitter = true;
//                uploadFinish();
//            }
//
//            public void failure(TwitterException exception){
//                //Do something on failure
//                hideProgress();
//                mIsFinishTwitter = true;
//                uploadFinish();
//            }
//        });
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data){
//
//        super.onActivityResult(requestCode, resultCode, data);
//        FaceBookUtil.onActivityResult(requestCode, resultCode, data);
//        TwitterUtil.onActivityResult(requestCode, resultCode, data);
//        KakaoUtil.handleActivityResult(requestCode, resultCode, data);
//    }
//}
