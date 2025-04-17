package com.pplus.networks.common;

/**
 * Created by J2N on 16. 6. 17..
 */
public class NetworkConfig{

    private static NetworkInfo networkInfo;

    public static void setNetworkInfo(NetworkInfo networkInfo){

        NetworkConfig.networkInfo = networkInfo;
    }

    public enum SWITCH{
        ON, OFF
    }

    public enum DEVELOP_MODE{
        DEV, REAL
    }

    /**
     * network debug on/off
     */
    private static final SWITCH DEBUG_LOG = SWITCH.OFF;

    private static final DEVELOP_MODE SERVER_MODE = DEVELOP_MODE.REAL;

    public static final String API_END_DEV_POINT_URL = "http://116.125.126.35";
    private static final String API_FILE_DEV_URL = "http://pics-dev.p-ple.com";

    public static final String API_END_REAL_POINT_URL = "https://web.p-ple.com";
    private static final String API_FILE_REAL_URL = "http://web.p-ple.com";

    public static final String API_STAGING_POINT_URL = "https://web-staging.p-ple.com";


    public static SWITCH isDebugLog(){

        if(networkInfo != null) {
            return networkInfo.isDebug();
        }
        return DEBUG_LOG;
    }

    public static DEVELOP_MODE getServerMode(){

        if(networkInfo != null) {
            return networkInfo.getServiceMode();
        }
        return SERVER_MODE;
    }

    /**
     * change debug url
     *
     * @return BASE URL
     */
    public static String getBaseServerURL(){

        switch (getServerMode()) {
            case DEV:
                if(networkInfo != null) {
                    return networkInfo.BaseDevServerUrl();
                }
                return API_END_DEV_POINT_URL;
            case REAL:
                if(networkInfo != null) {
                    return networkInfo.BaseRealServerUrl();
                }
                return API_END_REAL_POINT_URL;
            default:
                return API_END_REAL_POINT_URL;
        }
    }

    /**
     * change file server url
     *
     * @return file server URL
     */
    public static String getFileServerURL(){

        switch (SERVER_MODE) {
            case DEV:
                return API_FILE_DEV_URL;
            case REAL:
                return API_FILE_REAL_URL;
            default:
                return API_FILE_REAL_URL;
        }
    }


}
