package com.pplus.luckybol.apps.common.mgmt;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.pplus.utils.part.logs.LogUtil;
import com.pplus.utils.part.pref.PreferenceUtil;
import com.pplus.utils.part.utils.StringUtils;
import com.pplus.luckybol.LuckyBolApplication;
import com.pplus.luckybol.core.code.custom.YnCode;

/**
 * Created by Administrator on 2016-09-08.
 */
public class AppInfoManager{

    private static final String LOG_TAG = AppInfoManager.class.getSimpleName();
    private static AppInfoManager mAppInfoManager;
    private static Context mContext;

    private static final String AUTO_SIGN_IN = "auto_sign_in"; // 자동로그인
    private static final String IS_GUEST_LOCATION = "is_guest_location"; // 게스트의 위치정보 동의 화면 노출 여부
    private static final String PUSH_TOKEN = "push_token"; // 푸쉬 Token
    private static final String CONTACT_VERSION = "contactVersion"; //연락처 버전
    private static final String IS_ALIM_AGREE = "is_alim_agress"; // 최초 알림 여부
    private static final String SERVER_DATE_TIME = "server_date_time"; // 서버 시간
    private static final String SNS_TOKEN = "sns_token"; // SNS Token
    private static final String REAL_EVENT_YN = "real_event_yn"; // 실시간 이벤트 여부
    private static final String LOAD_CONTACT_YN = "load_contact_result"; // 연락처 업데이트 여부
    private static final String SERVICE_INTRODUCE = "service_introduce";
    private static final String SB_UNREAD_COUNT = "sb_unread_count";

    public enum PROMOTION{
        OPEN, CLOSE
    }

    private PROMOTION promotion = PROMOTION.OPEN;

    private PROMOTION realPromotion = PROMOTION.OPEN;

    public PROMOTION getPromotion(){

        return promotion;
    }

    public void setPromotion(PROMOTION promotion){

        this.promotion = promotion;
    }

    public PROMOTION getRealPromotion(){

        return realPromotion;
    }

    public void setRealPromotion(PROMOTION realPromotion){

        this.realPromotion = realPromotion;
    }

    public static AppInfoManager getInstance(){

        if(mAppInfoManager == null) {
            mAppInfoManager = new AppInfoManager();
            mContext = LuckyBolApplication.getContext();
        }

        return mAppInfoManager;
    }

    public boolean isInitialize(){

        return mContext != null;
    }

    /**
     * 위치정보 동의 여부(게스트)
     *
     * @return
     */
    public boolean isGuestLocation(){

        return PreferenceUtil.getDefaultPreference(mContext).get(IS_GUEST_LOCATION, false);
    }

    public void setIsGustLocation(boolean value){

        PreferenceUtil.getDefaultPreference(mContext).put(IS_GUEST_LOCATION, value);

        LogUtil.d(LOG_TAG, "iSGusetLocation = " + isGuestLocation());
    }

    /**
     * 자동 로그인 여부
     *
     * @return
     */
    public boolean isAutoSingIn(){

        return PreferenceUtil.getDefaultPreference(mContext).get(AUTO_SIGN_IN, true);
    }

    public void setAutoSignIn(boolean value){

        PreferenceUtil.getDefaultPreference(mContext).put(AUTO_SIGN_IN, value);
    }

    /**
     * App Version을 가져옴.
     *
     * @return
     */
    public String getAppVersion(){

        try {
            PackageInfo pinfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            return pinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
    }

    /**
     * App Version을 가져옴.
     *
     * @return
     */
    public int getAppVersionCode(){

        try {
            PackageInfo pinfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
            return pinfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 1;
        }
    }

    /**
     * 연락처 버전 저장
     *
     * @return
     */

    public long getContactVersion(){

        return PreferenceUtil.getDefaultPreference(mContext).getLong(CONTACT_VERSION);
    }

    public void setContactVersion(long version){

        PreferenceUtil.getDefaultPreference(mContext).put(CONTACT_VERSION, version);
    }

    /**
     * 최초 알림동의 팝업 여부
     *
     * @return
     */
    public boolean isAlimAgree(){

        return PreferenceUtil.getDefaultPreference(mContext).get(IS_ALIM_AGREE, false);
    }

    public void setAlimAgree(boolean value){

        PreferenceUtil.getDefaultPreference(mContext).put(IS_ALIM_AGREE, value);
    }


    /**
     * Push Token 저장
     *
     * @return
     */
    public String getPushToken(){

        return PreferenceUtil.getDefaultPreference(mContext).get(PUSH_TOKEN, "");
    }

    public void setPushToken(String value){

        PreferenceUtil.getDefaultPreference(mContext).put(PUSH_TOKEN, value);
    }

    /**
     * 서버 시간을 저장
     */
    public String getServerDateTime(){

        return PreferenceUtil.getDefaultPreference(mContext).get(SERVER_DATE_TIME, "");
    }

    public void setServerDateTime(String value){

        PreferenceUtil.getDefaultPreference(mContext).put(SERVER_DATE_TIME, value);
    }

    /**
     * SNS Token를 가져옴.
     */
    public String getSnsToken(){

        return PreferenceUtil.getDefaultPreference(mContext).get(SNS_TOKEN, "");
    }

    /**
     * SNS Token를 저장함
     *
     * @param value
     */
    public void setSnsTokenToken(String value){

        PreferenceUtil.getDefaultPreference(mContext).put(SNS_TOKEN, value);
    }

    /**
     * RealEvent YN를 가져옴.
     */
    public YnCode getRealEventYn(){

        YnCode ynCode = YnCode.N;
        if(StringUtils.isNotEmpty(PreferenceUtil.getDefaultPreference(mContext).get(REAL_EVENT_YN, ""))) {
            ynCode = YnCode.valueOf(PreferenceUtil.getDefaultPreference(mContext).get(REAL_EVENT_YN, ""));
        }

        return ynCode;
    }

    /**
     * LoadContactResult YN를 저장함
     *
     * @param value
     */
    public void setLoadContactResult(YnCode value){

        PreferenceUtil.getDefaultPreference(mContext).put(LOAD_CONTACT_YN, value.name());
    }

    /**
     * LoadContactResult YN를 가져옴.
     */
    public YnCode getLoadContactResult(){

        YnCode ynCode = YnCode.N;
        if(StringUtils.isNotEmpty(PreferenceUtil.getDefaultPreference(mContext).get(LOAD_CONTACT_YN, ""))) {
            ynCode = YnCode.valueOf(PreferenceUtil.getDefaultPreference(mContext).get(LOAD_CONTACT_YN, ""));
        }

        return ynCode;
    }

    /**
     * RealEvent YN를 저장함
     *
     * @param value
     */
    public void setRealEventYn(YnCode value){

        PreferenceUtil.getDefaultPreference(mContext).put(REAL_EVENT_YN, value.name());
    }

    /**
     * 서비스 소개 url 가져옴
     */
    public String getServiceIntroduceUrl(){

        return PreferenceUtil.getDefaultPreference(mContext).get(SERVICE_INTRODUCE, "");
    }

    /**
     * 서비스 소개 url 저장함
     *
     * @param url
     */
    public void setServiceIntroduceUrl(String url){

        PreferenceUtil.getDefaultPreference(mContext).put(SERVICE_INTRODUCE, url);
    }

    /**
     * 샌드버드 UnReadCount를 가져옴
     */
    public int getSbUnreadCount(){

        return PreferenceUtil.getDefaultPreference(mContext).get(SB_UNREAD_COUNT, -1);
    }

    /**
     * 샌드버드 UnReadCount를 저장함
     *
     * @param sbUnreadCount
     */
    public void setSbUnreadCount(int sbUnreadCount){

        PreferenceUtil.getDefaultPreference(mContext).put(SB_UNREAD_COUNT, sbUnreadCount);
    }

    /**
     * 버전정보(String)을 int형식으로 변환
     *
     * @param verInfo 버전정보(String)
     *
     * @return
     */
    private int[] getIntegerVersion(String verInfo){

        int ver[] = new int[3];

        if(verInfo != null && verInfo.length() > 0 && verInfo.contains(".")) {
            String data[] = verInfo.split("\\.");

            for(int i = 0; i < data.length; i++) {
                if(data[i] != null && !data[i].equals("")) {
                    if(data[i].contains("(")) {
                        int position = data[i].indexOf("(");
                        data[i] = data[i].substring(0, position);
                    }
                    try {
                        ver[i] = Integer.valueOf(data[i]);
                    } catch (NumberFormatException e) {
                        ver[i] = 0;
                    }
                }
            }
        }

        return ver;
    }

    /**
     * update 여부: -1이면 동일, 0: minor update, 1: major update
     *
     * @param newVersion
     *
     * @return
     */
    public int isVersionUpdate(String newVersion){

        int serverVer[] = new int[3]; // 서버에서 받아온 버전값
        int curVer[] = new int[3]; // 현재 앱의 버전값

        int isUpdate = -1;

        serverVer = getIntegerVersion(newVersion);

        curVer = getIntegerVersion(AppInfoManager.getInstance().getAppVersion());

        int curVer_int = Integer.valueOf(curVer[0] + curVer[1] + curVer[2]);
        int minVer_int = Integer.valueOf(serverVer[0] + serverVer[1] + serverVer[2]);

        // 첫번재 자리를 체크해서 버전이 높으면 강제 업데이트
        if(serverVer[0] > curVer[0]) {
            isUpdate = 1;
        } else {
            // 첫번째 자리가 같고, 두번째 자리도 높으면 강제 업데이트
            if(serverVer[0] == curVer[0] && serverVer[1] > curVer[1]) {
                isUpdate = 1;
            }
            // 아닐 경우 마이너 업데이트
            else if(serverVer[0] == curVer[0] && serverVer[1] == curVer[1] && serverVer[2] > curVer[2]) {
                isUpdate = 0;
            }
        }
        return isUpdate;
    }
}
