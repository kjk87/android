package com.pplus.prnumberbiz.core.sns.kakao;

import android.content.Context;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.pple.pplus.utils.part.logs.LogUtil;
import com.pple.pplus.utils.part.utils.StringUtils;
import com.pplus.prnumberbiz.R;

/**
 * Created by 김종경 on 2016-10-12.
 */

public class KakaoLinkUtil{

    private Context context;

    private static KakaoLinkUtil mKakaoLinkUtil;

    public static KakaoLinkUtil getInstance(Context context){

        if(mKakaoLinkUtil == null) {
            mKakaoLinkUtil = new KakaoLinkUtil(context);
        }
        mKakaoLinkUtil = new KakaoLinkUtil(context);
        return mKakaoLinkUtil;
    }

    private KakaoLinkUtil(Context context){

        this.context = context;
    }

    public void sendKakaoUrl(String text, String imageUrl, String url){

        if(StringUtils.isEmpty(imageUrl)){
            imageUrl = "";
        }

        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder(context.getString(R.string.app_name),
                        imageUrl,
                        LinkObject.newBuilder().setWebUrl(url)
                                .setMobileWebUrl(url).build())
                        .setDescrption(text)
                        .build())
                .addButton(new ButtonObject("웹에서 보기", LinkObject.newBuilder().setWebUrl(url).setMobileWebUrl(url).build()))
                .build();

        KakaoLinkService.getInstance().sendDefault(context, params, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                LogUtil.e("KakaoLinkUtil", errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
            }
        });

    }
}
