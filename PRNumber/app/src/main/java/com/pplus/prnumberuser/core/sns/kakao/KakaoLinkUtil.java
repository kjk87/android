//package com.pplus.prnumberuser.core.sns.kakao;
//
//import android.content.Context;
//
//import com.kakao.kakaolink.AppActionBuilder;
//import com.kakao.kakaolink.AppActionInfoBuilder;
//import com.kakao.kakaolink.KakaoLink;
//import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
//import com.kakao.kakaolink.internal.Action;
//import com.kakao.kakaolink.v2.KakaoLinkResponse;
//import com.kakao.kakaolink.v2.KakaoLinkService;
//import com.kakao.message.template.ButtonObject;
//import com.kakao.message.template.ContentObject;
//import com.kakao.message.template.FeedTemplate;
//import com.kakao.message.template.LinkObject;
//import com.kakao.network.ErrorResult;
//import com.kakao.network.callback.ResponseCallback;
//import com.kakao.util.helper.log.Logger;
//import com.pplus.utils.part.logs.LogUtil;
//import com.pplus.utils.part.utils.StringUtils;
//import com.pplus.prnumberuser.R;
//import com.pplus.prnumberuser.apps.common.mgmt.CountryConfigManager;
//import com.pplus.prnumberuser.apps.common.mgmt.LoginInfoManager;
//
///**
// * Created by 김종경 on 2016-10-12.
// */
//
//public class KakaoLinkUtil{
//
//    private Context context;
//    private KakaoLink kakaoLink;
//    private KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder;
//
//    private static KakaoLinkUtil mKakaoLinkUtil;
//
//    public static KakaoLinkUtil getInstance(Context context){
//
//        if(mKakaoLinkUtil == null) {
//            mKakaoLinkUtil = new KakaoLinkUtil(context);
//        }
//        mKakaoLinkUtil = new KakaoLinkUtil(context);
//        return mKakaoLinkUtil;
//    }
//
//    private KakaoLinkUtil(Context context){
//
//        this.context = context;
//        try {
//            kakaoLink = KakaoLink.getKakaoLink(context);
//            //            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
//        } catch (Exception e) {
//            LogUtil.e("KakaoLinkUtil", e.toString());
//        }
//    }
//
//    public void feedKakaoTalk(Context context, String text){
//
//        String url = "";
//
//        if(CountryConfigManager.getInstance().getConfig() != null && CountryConfigManager.getInstance().getConfig().getProperties() != null){
//            url = CountryConfigManager.getInstance().getConfig().getProperties().getInviteImageUrl();
//        }
//
//        if(StringUtils.isEmpty(url)){
//            url = "http://mud-kage.kakao.co.kr/dn/NTmhS/btqfEUdFAUf/FjKzkZsnoeE4o19klTOVI1/openlink_640x640s.jpg";
//        }
//
//        FeedTemplate params = FeedTemplate
//                .newBuilder(ContentObject.newBuilder("",
//                        url, LinkObject.newBuilder()
//                                .build())
//                        .setDescrption(text)
//                        .build())
//                        .addButton(new ButtonObject("앱에서 보기", LinkObject.newBuilder()
//                        .build()))
//                .build();
//
//        KakaoLinkService.getInstance().sendDefault(context, params, new ResponseCallback<KakaoLinkResponse>() {
//            @Override
//            public void onFailure(ErrorResult errorResult) {
//                Logger.e(errorResult.toString());
//            }
//
//            @Override
//            public void onSuccess(KakaoLinkResponse result) {
//
//            }
//        });
//    }
//
//    public void sendKakaoTalkLink(String text){
//
//        try {
//            kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
//            if(!StringUtils.isEmpty(text)) kakaoTalkLinkMessageBuilder.addText(text);
//
//            String url = "";
//
//            if(CountryConfigManager.getInstance().getConfig() != null && CountryConfigManager.getInstance().getConfig().getProperties() != null){
//                url = CountryConfigManager.getInstance().getConfig().getProperties().getInviteImageUrl();
//            }
//
//            if(StringUtils.isNotEmpty(url)) {
//                kakaoTalkLinkMessageBuilder.addImage(url, 300, 300);
//            }
//
//            AppActionBuilder builder = new AppActionBuilder();
//
//            builder.addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder().setMarketParam("referrer=recommendKey=" + LoginInfoManager.getInstance().getUser().getRecommendKey()).build());//android
//            builder.addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder().setMarketParam("referrer=recommendKey=" + LoginInfoManager.getInstance().getUser().getRecommendKey()).build());//ios
//
//            Action action = builder.build();
//            kakaoTalkLinkMessageBuilder.addAppButton(context.getString(R.string.msg_go_app), action);
//
//            kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder, context);
//        } catch (Exception e) {
//            LogUtil.e("KakaoLinkUtil", e.toString());
//        }
//    }
//}
