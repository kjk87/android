package com.pplus.utils;

/**
 * Created by J2N on 16. 6. 24.. <br>
 *  - import 문 다음에 기술 <br>
 *  - 블록주석을 사용 <br>
 *  - 각 라인은 *로 시작 <br>
 *  - 해당 클래스에 대한 기능과 용도 기술 <br>
 *  - <'pre>...<'/pre> 내용은 수정하지 않음 <br> <br>
 *
 * <pre>
 *      utils 관련 하여 모든 설정값을 관리 하도록 합니다.
 * </pre>
 *
 *  <pre>
 * <b>History:</b>
 * 안명훈, 1.0, 2016.6.24 초기작성
 * 안명훈, 1.1, 2016.6.29
 * - HMac SecretKey 추가
 * - SERVICE DOMAIN 추가
 * - PREFERENCE LIMIT CNT 추가
 *
 * </pre>
 *
 * @author 안명훈
 * @version 1.1,
 */
public class Config {
    //TODO 빼먹지말고 설정정보 필히 이곳으로 주석도 철저히!

    public static String DEFAULT_NAME = "J2N";

    /**
     * ON , OFF enum value..
     * */
    public enum CONFIG {
        ON,OFF
    }

    /**
     * 로그 저장 on / off
     * */
    private static CONFIG LOG_FILE_SAVE = CONFIG.OFF;

    /**
     * 로그 on / off
     * */
    private static CONFIG LOG_ENABLE = CONFIG.OFF;

    /**
     * 내부 저장소 메모리 카운팅 갯수 정의
     * */
    public static long PREFERENCE_MAX_LIMIT = 3;

    /**
     * HMAC을 생성하는 암호키
     * */
    public static String SERVICE_HMAC_SECRET_KEY = "123456789";

    /**
     * 서비스 계정 도메인
     * */
    public static String SERVICE_ACCOUNT_DOMAIN = "kr.co.j2n";

    public static boolean isLogEnable() {
        return LOG_ENABLE == CONFIG.ON ? true : false;
    }

    public static boolean isLogSaveEnable() {
        return LOG_FILE_SAVE == CONFIG.ON ? true : false;
    }

    public static void setLogEnable(CONFIG logEnable){

        LOG_ENABLE = logEnable;
    }

    public static void setLogSaveEnable(CONFIG config) {
        LOG_FILE_SAVE = config;
    }

    /**
     * %s = [ 획득하지못한 권한 ]
     * */
    public static final String PERMISSION_MESSAGE = "%s";


}
