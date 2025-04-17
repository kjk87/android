package com.pplus.luckybol.core.sns.kakao

import android.content.Context
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.KakaoSdk
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.navi.NaviClient
import com.kakao.sdk.navi.model.CoordType
import com.kakao.sdk.navi.model.Location
import com.kakao.sdk.navi.model.NaviOption
import com.kakao.sdk.user.UserApiClient
import com.pplus.luckybol.R
import com.pplus.utils.part.logs.LogUtil


/**
 * Created by 김종경 on 2016-07-06.
 */
class KakaoUtil {
    companion object {
        fun init(context: Context){
            KakaoSdk.init(context, context.getString(R.string.kakao_app_key))
        }

        fun logout() {
            UserApiClient.instance.logout { error ->
                if (error != null) {
                    LogUtil.e("KakaoUtil", "로그아웃 실패. SDK에서 토큰 삭제됨 :{}", error)
                }
                else {
                    LogUtil.e("KakaoUtil", "로그아웃 성공. SDK에서 토큰 삭제됨")
                }
            }
        }

        fun login(context: Context, callback: (token: OAuthToken?, error: Throwable?) -> Unit){
            // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
            if (LoginClient.instance.isKakaoTalkLoginAvailable(context)) {
                LoginClient.instance.loginWithKakaoTalk(context, callback = callback)
            } else {
                LoginClient.instance.loginWithKakaoAccount(context, callback = callback)
            }
        }

        fun navi(context: Context, name: String, lon: String, lat: String){
            if (NaviClient.instance.isKakaoNaviInstalled(context)) {
                LogUtil.e("kakonavi", "카카오내비 앱으로 길안내 가능")
                context.startActivity(NaviClient.instance.navigateIntent(Location(name, lon, lat), NaviOption(coordType = CoordType.WGS84)))
            } else {
                LogUtil.e("kakonavi", "카카오내비 미설치: 웹 길안내 사용 권장")

                // 웹 브라우저에서 길안내
                // 카카오내비가 설치되지 않은 곳에서 활용할 수 있습니다.
                val uri =
                    NaviClient.instance.navigateWebUrl(Location(name, lon, lat), NaviOption(coordType = CoordType.WGS84))

                // CustomTabs로 길안내
                KakaoCustomTabsClient.openWithDefault(context, uri)
            }
        }
    }


}