package com.pplus.prnumberuser.core.debug;

/**
 * define request code between clients
 */
public interface DebugConstant{

    public static final String DEBUG_SHARED_NAME = "pplus_debug";
    public static final String DEBUG_TOKEN = "token";

    // pplus to debug Request Code
    public static final int SEND_CONNECT_SERVICE = 1;
    public static final int SEND_TOKEN_DATA = 2;
    public static final int SEND_DISCONNET_SERVICE = 99;

    public static final int RECV_DEBUG_DATA = 100;

}
